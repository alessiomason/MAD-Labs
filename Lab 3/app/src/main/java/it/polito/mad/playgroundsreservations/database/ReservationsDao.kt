package it.polito.mad.playgroundsreservations.database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import androidx.room.Update

@androidx.room.Dao
interface ReservationsDao {
    @Query("SELECT * FROM reservations")
    fun getAllReservations(): LiveData<List<Reservation>>

    @Insert(onConflict = ABORT)
    suspend fun save(reservation: Reservation)

    @Update
    suspend fun update(reservation: Reservation)

    @Delete
    suspend fun delete(reservation: Reservation)
}