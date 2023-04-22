package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sports
import java.time.Duration
import java.util.*

class ReservationsActivity: AppCompatActivity() {
    private val reservationsViewModel by viewModels<ReservationsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservations)

        val tv = findViewById<TextView>(R.id.reservationsList)
        reservationsViewModel.getReservationsBySport(Sports.FOOTBALL).observe(this) {
            tv.text = it.toString()
        }

        val b = findViewById<Button>(R.id.addReservationButton)
        b.setOnClickListener {
            val r = Reservation(
                userId = 1,
                playgroundId = 1,
                sport = Sports.FOOTBALL,
                time = Date(),
                duration = Duration.ofHours(1))
            reservationsViewModel.save(r)
        }
    }
}