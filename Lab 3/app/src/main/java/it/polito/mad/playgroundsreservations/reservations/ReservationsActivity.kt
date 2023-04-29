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

        val reservationFOOTBALL = Reservation(userId = 1, playgroundId = 1, sport = Sports.FOOTBALL, time = Date(123, 3, 12), duration = Duration.ofHours(1))
        val reservationBASKETBALL = Reservation(userId = 1, playgroundId = 1, sport = Sports.BASKETBALL, time = Date(123,3,1), duration = Duration.ofHours(1))
        val reservationGOLF = Reservation(userId = 1, playgroundId = 1, sport = Sports.GOLF, time = Date(123,3,10), duration = Duration.ofHours(1))
        val reservationTENNIS = Reservation(userId = 1, playgroundId = 1, sport = Sports.TENNIS, time = Date(123,3,22), duration = Duration.ofHours(1))
        val reservationVOLLEYBALL = Reservation(userId = 1, playgroundId = 1, sport = Sports.VOLLEYBALL, time = Date(123,3,26), duration = Duration.ofHours(1))

        val reservationStessoGiorno1 = Reservation(userId = 1, playgroundId = 1, sport = Sports.GOLF, time = Date(123,3,22), duration = Duration.ofHours(1))
        // val reservationStessoGiorno2 = Reservation(userId = 1, playgroundId = 1, sport = Sports.BASKETBALL, time = Date(123,3,26), duration = Duration.ofHours(1))

        val reservations = listOf(reservationFOOTBALL, reservationBASKETBALL, reservationGOLF, reservationTENNIS, reservationVOLLEYBALL, reservationStessoGiorno1)

        reservations.forEach { reservation ->
            reservationsViewModel.save(reservation)
        }
    }
}
