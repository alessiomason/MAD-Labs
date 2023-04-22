package it.polito.mad.playgroundsreservations.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.util.*

enum class Sports {
    TENNIS, BASKETBALL, FOOTBALL, VOLLEYBALL, GOLF
}

@Entity(tableName = "reservations")
data class Reservation (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val playgroundId: Int,
    val sport: Sports,
    val time: Date,
    val duration: Duration
)