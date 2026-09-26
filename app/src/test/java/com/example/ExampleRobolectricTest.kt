package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.model.BookingStatus
import com.example.data.model.ServiceEntity
import com.example.data.model.StaffEntity
import com.example.data.repository.SalonRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: SalonRepository

    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        AppDatabase.seedInitialData(database.salonDao())
        repository = SalonRepository(database.salonDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context matches SalonSync`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Unisex Salon", appName)
    }

    @Test
    fun `booking engine prevents double booking on overlapping slot`() = runBlocking {
        val service = ServiceEntity(
            id = 101L,
            name = "Haute Couture Precision Cut",
            category = "Hair",
            description = "Cut",
            durationMinutes = 60,
            price = 95.0,
            isActive = true
        )
        val staff = StaffEntity(
            id = 101L,
            name = "Antoine Laurent",
            roleTitle = "Artistic Director",
            specialty = "Couture Cuts",
            rating = 4.98f,
            isActive = true
        )

        // 2026-10-15 is Thursday (open 09:00 - 20:00)
        // Book slot 10:00 - 11:00
        val booking1 = repository.validateAndCreateBooking(
            customerId = "cust_1",
            customerName = "Sophia Miller",
            customerPhone = "555-0100",
            service = service,
            staff = staff,
            bookingDate = "2026-10-15",
            startTime = "10:00"
        )
        assertTrue("Expected first booking to succeed, but was: ${booking1.exceptionOrNull()?.message}", booking1.isSuccess)

        // Try to book overlapping slot 10:30 with same staff on same date -> MUST FAIL
        val bookingOverlap = repository.validateAndCreateBooking(
            customerId = "cust_2",
            customerName = "Lucas Rivera",
            customerPhone = "555-0200",
            service = service,
            staff = staff,
            bookingDate = "2026-10-15",
            startTime = "10:30"
        )
        assertFalse("Expected overlapping booking to fail", bookingOverlap.isSuccess)
        assertNotNull(bookingOverlap.exceptionOrNull())
        assertTrue(bookingOverlap.exceptionOrNull()?.message?.contains("Double-booking prevented") == true)
    }
}
