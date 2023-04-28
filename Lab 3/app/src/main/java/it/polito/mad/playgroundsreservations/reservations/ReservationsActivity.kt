package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.stacktips.view.CustomCalendarView
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



        //Initialize CustomCalendarView from layout
        var calendarView = findViewById<View>(R.id.calendar_view) as CustomCalendarView

        //Initialize calendar with date
        val currentCalendar = Calendar.getInstance(Locale.getDefault())
//Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY)



    }
}