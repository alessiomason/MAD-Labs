package it.polito.mad.playgroundsreservations.reservations

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sports
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ReservationsActivity: AppCompatActivity() {


    private val reservationsViewModel by viewModels<ReservationsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservations)

        val navController = (supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment)
            .navController

        val reservation = Reservation(userId = 1, playgroundId = 1, sport = Sports.FOOTBALL, time = Date(), duration = Duration.ofHours(1))

        reservationsViewModel.save(reservation)
    }
}
