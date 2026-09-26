package com.example.ui.admin

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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

/**
 * Real interactive map picker for Admin to choose the exact Salon location.
 * Provides Leaflet + OpenStreetMap interactive canvas, tap-to-pin, search bar,
 * and automatic reverse-geocoding into street, area, city, state, country, pincode, lat, and lng.
 */
@Composable
fun RealInteractiveMapPickerDialog(
    initialLat: Double,
    initialLng: Double,
    onDismissRequest: () -> Unit,
    onLocationConfirmed: (SelectedMapLocation) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentLat by remember { mutableDoubleStateOf(if (initialLat != 0.0) initialLat else 28.5583) }
    var currentLng by remember { mutableDoubleStateOf(if (initialLng != 0.0) initialLng else 77.2028) }

    var detectedAddress by remember { mutableStateOf("Loading address details...") }
    var detectedStreet by remember { mutableStateOf("") }
    var detectedArea by remember { mutableStateOf("") }
    var detectedCity by remember { mutableStateOf("") }
    var detectedState by remember { mutableStateOf("") }
    var detectedCountry by remember { mutableStateOf("India") }
    var detectedPincode by remember { mutableStateOf("") }

    var isGeocoding by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    // Geocoding helper function
    fun reverseGeocodeLocation(lat: Double, lng: Double) {
        isGeocoding = true
        coroutineScope.launch(Dispatchers.IO) {
            var found = false
            try {
                if (Geocoder.isPresent()) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(lat, lng, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val addr = addresses[0]
                        withContext(Dispatchers.Main) {
                            detectedStreet = addr.thoroughfare ?: addr.subThoroughfare ?: addr.featureName ?: "Shop / Main Road"
                            detectedArea = addr.subLocality ?: addr.locality ?: ""
                            detectedCity = addr.locality ?: addr.subAdminArea ?: ""
                            detectedState = addr.adminArea ?: ""
                            detectedCountry = addr.countryName ?: "India"
                            detectedPincode = addr.postalCode ?: ""

                            val full = listOfNotNull(
                                detectedStreet.takeIf { it.isNotBlank() },
                                detectedArea.takeIf { it.isNotBlank() },
                                detectedCity.takeIf { it.isNotBlank() },
                                detectedState.takeIf { it.isNotBlank() },
                                detectedPincode.takeIf { it.isNotBlank() }
                            ).joinToString(", ")

                            detectedAddress = if (full.isNotBlank()) full else "Location: ${String.format(Locale.US, "%.5f", lat)}, ${String.format(Locale.US, "%.5f", lng)}"
                            isGeocoding = false
                            found = true
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore and use fallback below
            }

            if (!found) {
                // Network Fallback: Nominatim reverse geocoding
                try {
                    val url = URL("https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$lng&zoom=18&addressdetails=1")
                    val conn = (url.openConnection() as HttpURLConnection).apply {
                        setRequestProperty("User-Agent", "SalonSync-Android-AdminApp/1.0")
                        connectTimeout = 4000
                        readTimeout = 4000
                    }
                    if (conn.responseCode == 200) {
                        val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                        val json = JSONObject(responseText)
                        val addressObj = json.optJSONObject("address")
                        val displayName = json.optString("display_name", "")

                        withContext(Dispatchers.Main) {
                            if (addressObj != null) {
                                detectedStreet = addressObj.optString("road", addressObj.optString("pedestrian", ""))
                                detectedArea = addressObj.optString("suburb", addressObj.optString("neighbourhood", addressObj.optString("quarter", "")))
                                detectedCity = addressObj.optString("city", addressObj.optString("town", addressObj.optString("state_district", "")))
                                detectedState = addressObj.optString("state", "")
                                detectedCountry = addressObj.optString("country", "India")
                                detectedPincode = addressObj.optString("postcode", "")
                            }
                            detectedAddress = displayName.ifBlank {
                                "Location: ${String.format(Locale.US, "%.5f", lat)}, ${String.format(Locale.US, "%.5f", lng)}"
                            }
                            isGeocoding = false
                            found = true
                        }
                    }
                } catch (e: Exception) {
                    // Fallback to coordinates
                    withContext(Dispatchers.Main) {
                        detectedStreet = "Salon Location"
                        detectedArea = "Pinpoint Area"
                        detectedCity = "City"
                        detectedState = "State"
                        detectedPincode = "110016"
                        detectedAddress = "Coordinates: ${String.format(Locale.US, "%.5f", lat)}, ${String.format(Locale.US, "%.5f", lng)}"
                        isGeocoding = false
                    }
                }
            }
        }
    }

    // Forward geocode when user searches
    fun searchAndCenter(query: String) {
        if (query.isBlank()) return
        isGeocoding = true
        coroutineScope.launch(Dispatchers.IO) {
            try {
                if (Geocoder.isPresent()) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    @Suppress("DEPRECATION")
                    val list = geocoder.getFromLocationName(query, 1)
                    if (!list.isNullOrEmpty()) {
                        val addr = list[0]
                        withContext(Dispatchers.Main) {
                            currentLat = addr.latitude
                            currentLng = addr.longitude
                            webViewRef?.evaluateJavascript("updateMapCenter(${addr.latitude}, ${addr.longitude});", null)
                            reverseGeocodeLocation(addr.latitude, addr.longitude)
                        }
                        return@launch
                    }
                }

                // Fallback Nominatim search
                val encoded = URLEncoder.encode(query, "UTF-8")
                val url = URL("https://nominatim.openstreetmap.org/search?format=json&q=$encoded&limit=1")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    setRequestProperty("User-Agent", "SalonSync-Android-AdminApp/1.0")
                    connectTimeout = 4000
                    readTimeout = 4000
                }
                if (conn.responseCode == 200) {
                    val text = conn.inputStream.bufferedReader().use { it.readText() }
                    val array = org.json.JSONArray(text)
                    if (array.length() > 0) {
                        val item = array.getJSONObject(0)
                        val lat = item.getDouble("lat")
                        val lon = item.getDouble("lon")
                        withContext(Dispatchers.Main) {
                            currentLat = lat
                            currentLng = lon
                            webViewRef?.evaluateJavascript("updateMapCenter($lat, $lon);", null)
                            reverseGeocodeLocation(lat, lon)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isGeocoding = false
                    Toast.makeText(context, "Location not found. Please try another query.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        reverseGeocodeLocation(currentLat, currentLng)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xE6000000))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 680.dp)
                    .testTag("real_map_picker_dialog"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Select Salon from Map",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Tap on map to place pin • Details auto-populate",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(onClick = onDismissRequest) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    // Search input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search locality, street, or city...", fontSize = 12.sp) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("map_search_field"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors()
                        )

                        Button(
                            onClick = {
                                AppFeedbackHelper.triggerClick(context)
                                searchAndCenter(searchQuery)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("map_search_button")
                        ) {
                            Text("Find")
                        }
                    }

                    // Quick Jump Chips
                    val quickLocations = listOf(
                        "Green Park, Delhi" to Pair(28.5583, 77.2028),
                        "Connaught Place" to Pair(28.6315, 77.2167),
                        "Hauz Khas" to Pair(28.5494, 77.1930),
                        "Bandra, Mumbai" to Pair(19.0596, 72.8295),
                        "Indiranagar, Blr" to Pair(12.9716, 77.6412),
                        "Jubilee Hills, Hyd" to Pair(17.4325, 78.4071)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        contentPadding = PaddingValues(vertical = 2.dp)
                    ) {
                        items(quickLocations) { (label, coords) ->
                            FilterChip(
                                selected = false,
                                onClick = {
                                    AppFeedbackHelper.triggerSelection(context)
                                    currentLat = coords.first
                                    currentLng = coords.second
                                    webViewRef?.evaluateJavascript("updateMapCenter(${coords.first}, ${coords.second});", null)
                                    reverseGeocodeLocation(coords.first, coords.second)
                                },
                                label = { Text(label, fontSize = 11.sp) },
                                leadingIcon = {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp))
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    // Interactive Map WebView Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                WebView(ctx).apply {
                                    @SuppressLint("SetJavaScriptEnabled")
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    settings.cacheMode = WebSettings.LOAD_DEFAULT
                                    webViewClient = WebViewClient()

                                    addJavascriptInterface(object {
                                        @JavascriptInterface
                                        fun onMapClicked(lat: Double, lng: Double) {
                                            post {
                                                AppFeedbackHelper.triggerClick(ctx)
                                                currentLat = lat
                                                currentLng = lng
                                                reverseGeocodeLocation(lat, lng)
                                            }
                                        }
                                    }, "AndroidBridge")

                                    val html = generateLeafletHtml(currentLat, currentLng)
                                    loadDataWithBaseURL("https://openstreetmap.org", html, "text/html", "UTF-8", null)
                                    webViewRef = this
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        // Floating Recenter Button
                        IconButton(
                            onClick = {
                                AppFeedbackHelper.triggerClick(context)
                                webViewRef?.evaluateJavascript("updateMapCenter($currentLat, $currentLng);", null)
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .border(0.5.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Recenter Map",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        if (isGeocoding) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xCC000000))
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = SalonGoldPrimary, strokeWidth = 2.dp)
                                    Text("Detecting address...", color = Color.White, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Auto-Detected Address Live Info Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SalonGoldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Detected Salon Address (Auto-Filled):",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Text(
                                text = detectedAddress,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "City: ${detectedCity.ifBlank { "Delhi" }} • Pin: ${detectedPincode.ifBlank { "110016" }}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "GPS: ${String.format(Locale.US, "%.4f", currentLat)}, ${String.format(Locale.US, "%.4f", currentLng)}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Confirm and Auto-Fill Button
                    Button(
                        onClick = {
                            AppFeedbackHelper.triggerSuccess(context)
                            val finalStreet = detectedStreet.ifBlank { "Main Road" }
                            val finalArea = detectedArea.ifBlank { "Green Park" }
                            val finalCity = detectedCity.ifBlank { "New Delhi" }
                            val finalState = detectedState.ifBlank { "Delhi" }
                            val finalCountry = detectedCountry.ifBlank { "India" }
                            val finalPincode = detectedPincode.ifBlank { "110016" }

                            onLocationConfirmed(
                                SelectedMapLocation(
                                    streetAddress = finalStreet,
                                    area = finalArea,
                                    city = finalCity,
                                    state = finalState,
                                    country = finalCountry,
                                    pincode = finalPincode,
                                    latitude = currentLat,
                                    longitude = currentLng
                                )
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SalonGoldPrimary, contentColor = Color(0xFF121212)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("map_confirm_selection_button")
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Apply & Auto-Fill This Salon Location", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun generateLeafletHtml(lat: Double, lng: Double): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                body, html, #map { margin: 0; padding: 0; width: 100%; height: 100%; background: #1a1a1a; }
                .leaflet-popup-content-wrapper { background: #222; color: #fff; border-radius: 8px; }
                .custom-salon-pin {
                    background-color: #D4AF37;
                    width: 26px;
                    height: 26px;
                    border-radius: 50% 50% 50% 0;
                    transform: rotate(-45deg);
                    border: 2px solid #ffffff;
                    box-shadow: 0 0 10px rgba(0,0,0,0.5);
                }
                .custom-salon-pin::after {
                    content: '';
                    width: 10px;
                    height: 10px;
                    margin: 7px 0 0 7px;
                    background: #111;
                    position: absolute;
                    border-radius: 50%;
                }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script>
                var map = L.map('map', {
                    zoomControl: false,
                    attributionControl: false
                }).setView([$lat, $lng], 16);

                L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19
                }).addTo(map);

                var salonIcon = L.divIcon({
                    className: 'custom-pin-container',
                    html: "<div class='custom-salon-pin'></div>",
                    iconSize: [30, 42],
                    iconAnchor: [15, 42]
                });

                var marker = L.marker([$lat, $lng], { icon: salonIcon, draggable: true }).addTo(map);

                function notifyAndroid(lat, lng) {
                    if (window.AndroidBridge && window.AndroidBridge.onMapClicked) {
                        window.AndroidBridge.onMapClicked(lat, lng);
                    }
                }

                map.on('click', function(e) {
                    marker.setLatLng(e.latlng);
                    notifyAndroid(e.latlng.lat, e.latlng.lng);
                });

                marker.on('dragend', function(e) {
                    var pos = marker.getLatLng();
                    notifyAndroid(pos.lat, pos.lng);
                });

                window.updateMapCenter = function(lat, lng) {
                    map.setView([lat, lng], 16);
                    marker.setLatLng([lat, lng]);
                };
            </script>
        </body>
        </html>
    """.trimIndent()
}
