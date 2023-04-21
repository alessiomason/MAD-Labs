package it.polito.mad.playgroundsreservations.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.util.*

@Entity(tableName = "reservations")
data class Reservation (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val fieldId: Int/*,
    val time: Date,
    val duration: Duration*/
)