package it.polito.mad.playgroundsreservations.reservations

import android.content.res.Resources.Theme
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import it.polito.mad.playgroundsreservations.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class PlaygroundsAvailabilityFragment: Fragment(R.layout.fragment_playgrounds_availability) {

    private lateinit var weekCalendarView: WeekCalendarView
    private var selectedDate: LocalDate? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weekCalendarView = view.findViewById(R.id.week_calendar_view)

        weekCalendarView.dayBinder = object: WeekDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: WeekDay) {
                container.day = data
                container.t = view.findViewById(R.id.test)
                container.textView.text = data.date.dayOfMonth.toString()

                if (data.date == selectedDate) {
                    container.textView.setTextColor(Color.WHITE)
                    container.textView.setBackgroundResource(R.drawable.rounded_shape)
                } else {
                    container.textView.setTextColor(Color.BLACK)
                    container.textView.background = null
                }
            }
        }

        val currentDate = LocalDate.now()
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(12).atStartOfMonth() // Adjust as needed
        val endDate = currentMonth.plusMonths(12).atEndOfMonth()  // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        weekCalendarView.setup(startDate, endDate, firstDayOfWeek)
        weekCalendarView.scrollToWeek(currentDate)
    }

    inner class DayViewContainer(view: View): ViewContainer(view) {
        val textView = view.findViewById<TextView>(R.id.calendarDayText)
        lateinit var day: WeekDay
        lateinit var t: TextView

        init {
            view.setOnClickListener {
                // show date clicked
                val currentSelection = selectedDate
                selectedDate = day.date
                weekCalendarView.notifyDateChanged(day.date)
                if (currentSelection != null) {
                    weekCalendarView.notifyDateChanged(currentSelection)
                }

                t.text = day.toString()
            }
        }
    }
}