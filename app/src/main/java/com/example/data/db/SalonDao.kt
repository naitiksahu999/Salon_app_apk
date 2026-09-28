package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AmenityEntity
import com.example.data.model.BookingEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.SalonBreakEntity
import com.example.data.model.SalonConfigEntity
import com.example.data.model.SalonHolidayEntity
import com.example.data.model.SalonWorkingDayEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.StaffEntity
import com.example.data.model.SupportMessageEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SalonDao {

    // Config
    @Query("SELECT * FROM salon_config WHERE id = 1 LIMIT 1")
    fun getSalonConfig(): Flow<SalonConfigEntity?>

    @Query("SELECT * FROM salon_config WHERE id = 1 LIMIT 1")
    suspend fun getSalonConfigSync(): SalonConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateConfig(config: SalonConfigEntity)

    // Working Days
    @Query("SELECT * FROM salon_working_days ORDER BY dayOfWeek ASC")
    fun getAllWorkingDays(): Flow<List<SalonWorkingDayEntity>>

    @Query("SELECT * FROM salon_working_days ORDER BY dayOfWeek ASC")
    suspend fun getAllWorkingDaysSync(): List<SalonWorkingDayEntity>

    @Query("SELECT * FROM salon_working_days WHERE dayOfWeek = :dayOfWeek LIMIT 1")
    suspend fun getWorkingDaySync(dayOfWeek: Int): SalonWorkingDayEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkingDays(days: List<SalonWorkingDayEntity>)

    @Update
    suspend fun updateWorkingDay(day: SalonWorkingDayEntity)

    // Breaks
    @Query("SELECT * FROM salon_breaks ORDER BY startTime ASC")
    fun getAllBreaks(): Flow<List<SalonBreakEntity>>

    @Query("SELECT * FROM salon_breaks ORDER BY startTime ASC")
    suspend fun getAllBreaksSync(): List<SalonBreakEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreak(salonBreak: SalonBreakEntity): Long

    @Query("DELETE FROM salon_breaks WHERE id = :id")
    suspend fun deleteBreak(id: Long)

    // Holidays
    @Query("SELECT * FROM salon_holidays ORDER BY dateString ASC")
    fun getAllHolidays(): Flow<List<SalonHolidayEntity>>

    @Query("SELECT * FROM salon_holidays ORDER BY dateString ASC")
    suspend fun getAllHolidaysSync(): List<SalonHolidayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoliday(holiday: SalonHolidayEntity): Long

    @Query("DELETE FROM salon_holidays WHERE id = :id")
    suspend fun deleteHoliday(id: Long)

    // Services
    @Query("SELECT * FROM services ORDER BY category ASC, name ASC")
    fun getAllServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services ORDER BY category ASC, name ASC")
    suspend fun getAllServicesSync(): List<ServiceEntity>

    @Query("SELECT * FROM services WHERE isActive = 1 ORDER BY category ASC, name ASC")
    fun getActiveServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE id = :id LIMIT 1")
    suspend fun getServiceById(id: Long): ServiceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Delete
    suspend fun deleteService(service: ServiceEntity)

    @Query("DELETE FROM services WHERE id = :id")
    suspend fun deleteServiceById(id: Long)

    // Staff
    @Query("SELECT * FROM staff ORDER BY name ASC")
    fun getAllStaff(): Flow<List<StaffEntity>>

    @Query("SELECT * FROM staff WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveStaff(): Flow<List<StaffEntity>>

    @Query("SELECT * FROM staff WHERE isActive = 1 ORDER BY name ASC")
    suspend fun getActiveStaffSync(): List<StaffEntity>

    @Query("SELECT * FROM staff ORDER BY name ASC")
    suspend fun getAllStaffSync(): List<StaffEntity>

    @Query("SELECT * FROM staff WHERE id = :id LIMIT 1")
    suspend fun getStaffById(id: Long): StaffEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaff(staff: StaffEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaffList(staffList: List<StaffEntity>)

    @Update
    suspend fun updateStaff(staff: StaffEntity)

    @Delete
    suspend fun deleteStaff(staff: StaffEntity)

    @Query("DELETE FROM staff WHERE id = :id")
    suspend fun deleteStaffById(id: Long)

    // Users
    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    suspend fun getAllUsersSync(): List<UserEntity>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    suspend fun getUserByPhone(phone: String): UserEntity?

    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:identifier) OR phone = :identifier LIMIT 1")
    suspend fun getUserByIdentifier(identifier: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    // Bookings
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    suspend fun getAllBookingsSync(): List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE customerId = :customerId ORDER BY bookingDate DESC, startTime DESC")
    fun getBookingsForCustomer(customerId: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE bookingDate = :dateString AND (staffId = :staffId OR :staffId = -1) AND status NOT IN ('REJECTED', 'CANCELLED')")
    suspend fun getActiveBookingsForDateAndStaffSync(dateString: String, staffId: Long): List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE bookingDate = :dateString AND status NOT IN ('REJECTED', 'CANCELLED')")
    suspend fun getActiveBookingsForDateSync(dateString: String): List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    suspend fun getBookingById(id: Long): BookingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<BookingEntity>)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBookingById(id: Long)

    // Support Messages
    @Query("SELECT * FROM support_messages WHERE ticketCustomerId = :customerId ORDER BY timestamp ASC")
    fun getMessagesForCustomer(customerId: String): Flow<List<SupportMessageEntity>>

    @Query("SELECT * FROM support_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<SupportMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupportMessage(message: SupportMessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupportMessages(messages: List<SupportMessageEntity>)

    @Query("UPDATE support_messages SET isRead = 1 WHERE ticketCustomerId = :customerId")
    suspend fun markCustomerMessagesAsRead(customerId: String)

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE recipientRole = :role OR recipientUserId = :userId ORDER BY timestamp DESC")
    fun getNotificationsForRoleOrUser(role: String, userId: String?): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1 WHERE recipientRole = :role OR recipientUserId = :userId")
    suspend fun markAllNotificationsAsRead(role: String, userId: String?)

    // Amenities / Facilities (Adjustable by Admin)
    @Query("SELECT * FROM salon_amenities ORDER BY sortOrder ASC")
    fun getAllAmenities(): Flow<List<AmenityEntity>>

    @Query("SELECT * FROM salon_amenities WHERE isEnabled = 1 ORDER BY sortOrder ASC")
    fun getActiveAmenities(): Flow<List<AmenityEntity>>

    @Query("SELECT * FROM salon_amenities ORDER BY sortOrder ASC")
    suspend fun getAllAmenitiesSync(): List<AmenityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAmenities(amenities: List<AmenityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAmenity(amenity: AmenityEntity)

    @Update
    suspend fun updateAmenity(amenity: AmenityEntity)

    @Query("DELETE FROM salon_amenities WHERE id = :id")
    suspend fun deleteAmenity(id: String)
}
