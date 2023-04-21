package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Reservation

class ReservationsActivity: AppCompatActivity() {
    private val reservationsViewModel by viewModels<ReservationsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val r = Reservation(userId = 1, fieldId = 1)

        reservationsViewModel.save(r)

        reservationsViewModel.reservations.observe(this) {
            println("RESERVATIONS: $it")
        }

        reservationsViewModel.delete(r)
    }
}