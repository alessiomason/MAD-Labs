package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.polito.mad.playgroundsreservations.R

class Global {
    companion object {
        const val userId = "2IffoI0i9sA6ZgERXOMl"
    }
}

class ReservationsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservations)
    }
}
