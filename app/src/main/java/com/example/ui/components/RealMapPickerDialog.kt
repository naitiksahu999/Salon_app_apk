package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.SalonGoldPrimary
import com.example.util.AppFeedbackHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

data class SelectedMapLocation(
    val streetAddress: String,
    val area: String,
    val city: String,
    val state: String,
    val country: String,
    val pincode: String,
    val latitude: Double,
    val longitude: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealMapPickerDialog(
    initialLatitude: Double,
    initialLongitude: Double,
    initialStreetAddress: String = "",
    initialCity: String = "",
    onDismiss: () -> Unit,
    onLocationConfirmed: (SelectedMapLocation) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentLat by remember { mutableDoubleStateOf(if (initialLatitude != 0.0) initialLatitude else 28.5583) }
    var currentLng by remember { mutableDoubleStateOf(if (initialLongitude != 0.0) initialLongitude else 77.2028) }

    var resolvedStreet by remember { mutableStateOf(initialStreetAddress) }
    var resolvedArea by remember { mutableStateOf("") }
    var resolvedCity by remember { mutableStateOf(initialCity) }
    var resolvedState by remember { mutableStateOf("") }
    var resolvedCountry by remember { mutableStateOf("India") }
    var resolvedPincode by remember { mutableStateOf("") }

    var isResolvingAddress by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    // Geocoding logic
    fun fetchAddressDetails(lat: Double, lng: Double) {
        isResolvingAddress = true
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                reverseGeocodeLocation(context, lat, lng)
            }
            isResolvingAddress = false
            result?.let { loc ->
                if (loc.streetAddress.isNotBlank()) resolvedStreet = loc.streetAddress
                if (loc.area.isNotBlank()) resolvedArea = loc.area
                if (loc.city.isNotBlank()) resolvedCity = loc.city
                if (loc.state.isNotBlank()) resolvedState = loc.state
                if (loc.country.isNotBlank()) resolvedCountry = loc.country
                if (loc.pincode.isNotBlank()) resolvedPincode = loc.pincode
            }
        }
    }

    // Initial fetch
    LaunchedEffect(Unit) {
        fetchAddressDetails(currentLat, currentLng)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SalonGoldPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SalonGoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Interactive Map Pin",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tap map or drag pin to select shop coordinates",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Search Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search locality, landmark, city...", fontSize = 13.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            if (searchQuery.isNotBlank()) {
                                coroutineScope.launch {
                                    isResolvingAddress = true
                                    val coords = withContext(Dispatchers.IO) {
                                        searchCoordinates(context, searchQuery)
                                    }
                                    isResolvingAddress = false
                                    coords?.let { (lat, lng) ->
                                        currentLat = lat
                                        currentLng = lng
                                        webViewRef?.evaluateJavascript(
                                            "moveToLocation($lat, $lng);",
                                            null
                                        )
                                        fetchAddressDetails(lat, lng)
                                    }
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SalonGoldPrimary, contentColor = Color.Black)
                    ) {
                        Text("Search", fontWeight = FontWeight.Bold)
                    }
                }

                // Live Map View (Leaflet OpenStreetMap)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                ) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            WebView(ctx).apply {
                                webViewRef = this
                                @SuppressLint("SetJavaScriptEnabled")
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                settings.useWideViewPort = true
                                settings.loadWithOverviewMode = true
                                webViewClient = WebViewClient()

                                addJavascriptInterface(object {
                                    @JavascriptInterface
                                    fun onPinMoved(lat: Double, lng: Double) {
                                        coroutineScope.launch(Dispatchers.Main) {
                                            AppFeedbackHelper.triggerSelection(context)
                                            currentLat = lat
                                            currentLng = lng
                                            fetchAddressDetails(lat, lng)
                                        }
                                    }
                                }, "AndroidBridge")

                                val html = buildLeafletMapHtml(currentLat, currentLng)
                                loadDataWithBaseURL("https://openstreetmap.org", html, "text/html", "UTF-8", null)
                            }
                        }
                    )

                    // Loading overlay for reverse geocoding
                    if (isResolvingAddress) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 12.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            tonalElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                                Text("Detecting address details...", fontSize = 12.sp)
                            }
                        }
                    }
                }

                // Bottom Detected Information Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Auto-Populated Location Details",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = SalonGoldPrimary
                            )
                            Text(
                                text = "%.4f, %.4f".format(currentLat, currentLng),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = if (resolvedStreet.isNotBlank()) resolvedStreet else "Pinpoint selected on map",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (resolvedArea.isNotBlank()) {
                                Text("Area: $resolvedArea", style = MaterialTheme.typography.bodySmall)
                            }
                            if (resolvedCity.isNotBlank()) {
                                Text("City: $resolvedCity", style = MaterialTheme.typography.bodySmall)
                            }
                            if (resolvedPincode.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SalonGoldPrimary.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "PIN: $resolvedPincode",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SalonGoldPrimary
                                    )
                                }
                            }
                        }

                        if (resolvedState.isNotBlank() || resolvedCountry.isNotBlank()) {
                            Text(
                                text = listOf(resolvedState, resolvedCountry).filter { it.isNotBlank() }.joinToString(", "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                AppFeedbackHelper.triggerSuccess(context)
                                onLocationConfirmed(
                                    SelectedMapLocation(
                                        streetAddress = resolvedStreet.ifBlank { "Shop 14, Main Market" },
                                        area = resolvedArea.ifBlank { "Green Park" },
                                        city = resolvedCity.ifBlank { "New Delhi" },
                                        state = resolvedState.ifBlank { "Delhi" },
                                        country = resolvedCountry.ifBlank { "India" },
                                        pincode = resolvedPincode.ifBlank { "110016" },
                                        latitude = currentLat,
                                        longitude = currentLng
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SalonGoldPrimary,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Set as Salon Location", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

private fun buildLeafletMapHtml(lat: Double, lng: Double): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                html, body, #map {
                    height: 100%;
                    width: 100%;
                    margin: 0;
                    padding: 0;
                    background: #1a1a1a;
                }
                .leaflet-control-attribution {
                    font-size: 8px !important;
                }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script>
                var map = L.map('map', {
                    zoomControl: true,
                    attributionControl: false
                }).setView([$lat, $lng], 15);

                L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19
                }).addTo(map);

                var marker = L.marker([$lat, $lng], {
                    draggable: true
                }).addTo(map);

                function notifyAndroid(lat, lng) {
                    if (window.AndroidBridge && window.AndroidBridge.onPinMoved) {
                        window.AndroidBridge.onPinMoved(lat, lng);
                    }
                }

                marker.on('dragend', function (e) {
                    var position = marker.getLatLng();
                    notifyAndroid(position.lat, position.lng);
                });

                map.on('click', function(e) {
                    marker.setLatLng(e.latlng);
                    notifyAndroid(e.latlng.lat, e.latlng.lng);
                });

                function moveToLocation(lat, lng) {
                    map.setView([lat, lng], 16);
                    marker.setLatLng([lat, lng]);
                }
            </script>
        </body>
        </html>
    """.trimIndent()
}

/**
 * Reverse geocodes coordinates into structured address using Android Geocoder and OpenStreetMap Nominatim API.
 */
private fun reverseGeocodeLocation(context: Context, lat: Double, lng: Double): SelectedMapLocation? {
    // 1. Try local Android Geocoder first
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        @Suppress("DEPRECATION")
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        if (!addresses.isNullOrEmpty()) {
            val addr = addresses[0]
            val street = addr.thoroughfare ?: addr.featureName ?: addr.getAddressLine(0) ?: ""
            val area = addr.subLocality ?: addr.subAdminArea ?: ""
            val city = addr.locality ?: addr.subAdminArea ?: ""
            val state = addr.adminArea ?: ""
            val country = addr.countryName ?: "India"
            val pincode = addr.postalCode ?: ""

            return SelectedMapLocation(
                streetAddress = street,
                area = area,
                city = city,
                state = state,
                country = country,
                pincode = pincode,
                latitude = lat,
                longitude = lng
            )
        }
    } catch (_: Exception) {}

    // 2. HTTP Fallback to OpenStreetMap Nominatim
    try {
        val url = URL("https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$lng&addressdetails=1")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("User-Agent", "SalonSyncApp/1.0")
        conn.connectTimeout = 4000
        conn.readTimeout = 4000

        if (conn.responseCode == 200) {
            val reader = BufferedReader(InputStreamReader(conn.inputStream))
            val sb = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
            reader.close()

            val json = JSONObject(sb.toString())
            val addressObj = json.optJSONObject("address")
            if (addressObj != null) {
                val street = addressObj.optString("road", addressObj.optString("suburb", ""))
                val area = addressObj.optString("suburb", addressObj.optString("neighbourhood", ""))
                val city = addressObj.optString("city", addressObj.optString("town", addressObj.optString("county", "")))
                val state = addressObj.optString("state", "")
                val country = addressObj.optString("country", "India")
                val pincode = addressObj.optString("postcode", "")

                return SelectedMapLocation(
                    streetAddress = street,
                    area = area,
                    city = city,
                    state = state,
                    country = country,
                    pincode = pincode,
                    latitude = lat,
                    longitude = lng
                )
            }
        }
    } catch (_: Exception) {}

    return null
}

/**
 * Forward geocoding search using Geocoder and Nominatim
 */
private fun searchCoordinates(context: Context, query: String): Pair<Double, Double>? {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        @Suppress("DEPRECATION")
        val list = geocoder.getFromLocationName(query, 1)
        if (!list.isNullOrEmpty()) {
            return Pair(list[0].latitude, list[0].longitude)
        }
    } catch (_: Exception) {}

    try {
        val encoded = URLEncoder.encode(query, "UTF-8")
        val url = URL("https://nominatim.openstreetmap.org/search?format=json&q=$encoded&limit=1")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("User-Agent", "SalonSyncApp/1.0")
        conn.connectTimeout = 4000
        conn.readTimeout = 4000

        if (conn.responseCode == 200) {
            val reader = BufferedReader(InputStreamReader(conn.inputStream))
            val text = reader.readText()
            reader.close()
            val array = org.json.JSONArray(text)
            if (array.length() > 0) {
                val first = array.getJSONObject(0)
                val lat = first.getDouble("lat")
                val lon = first.getDouble("lon")
                return Pair(lat, lon)
            }
        }
    } catch (_: Exception) {}

    return null
}
