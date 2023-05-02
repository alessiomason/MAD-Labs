package it.polito.mad.playgroundsreservations.reservations

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sports
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class PlaygroundsAvailabilityFragment: Fragment(R.layout.fragment_playgrounds_availability), AdapterView.OnItemSelectedListener {

    val sports = Sports.values()
    private lateinit var weekCalendarView: WeekCalendarView
    private var selectedSport = MutableLiveData(Sports.TENNIS)
    private var selectedDate = LocalDate.now()
    private lateinit var reservedPlaygrounds: LiveData<Map<Reservation, Playground>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reservationsViewModel by viewModels<ReservationsViewModel>()

        val spinner = view.findViewById<Spinner>(R.id.spinner)
        activity?.let { activity ->
            ArrayAdapter(
                activity.applicationContext,
                android.R.layout.simple_spinner_item,
                sports.map { it.name.substring(0 until 1).uppercase() + it.name.substring(1).lowercase() }
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
        spinner.onItemSelectedListener = this

        selectedSport.observe(viewLifecycleOwner) { sport ->
            reservedPlaygrounds = reservationsViewModel.getReservedPlaygrounds(sport)

            reservedPlaygrounds.observe(viewLifecycleOwner) {
                view.findViewById<TextView>(R.id.test).text = it.toString()
            }
        }

        weekCalendarView = view.findViewById(R.id.week_calendar_view)

        val currentDate = LocalDate.now()
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(12).atStartOfMonth()
        val endDate = currentMonth.plusMonths(12).atEndOfMonth()
        val daysOfWeek = daysOfWeek()

        // change month title
        weekCalendarView.weekScrollListener = { newWeek ->
            val monthHeaderTextView = view.findViewById<TextView>(R.id.month_header_text)
            val formatter = DateTimeFormatter.ofPattern("MMM uuuu", Locale.getDefault())
            monthHeaderTextView.text = newWeek.days[0].date.yearMonth.format(formatter)
        }

        // handles the newly selected date
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

        // shows the days names
        weekCalendarView.weekHeaderBinder = object: WeekHeaderFooterBinder<WeekViewContainer> {
            override fun create(view: View) = WeekViewContainer(view)
            override fun bind(container: WeekViewContainer, data: Week) {

                // Remember that the header is reused so this will be called for each month.
                // However, the first day of the week will not change so no need to bind
                // the same view every time it is reused.
                if (container.titlesContainer.tag == null) {
                    container.titlesContainer.tag = data.days
                    container.titlesContainer.children.map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek[index]
                            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = title
                            // In the code above, we use the same `daysOfWeek` list
                            // that was created when we set up the calendar.
                            // However, we can also get the `daysOfWeek` list from the month data:
                            // val daysOfWeek = data.weekDays.first().map { it.date.dayOfWeek }
                            // Alternatively, you can get the value for this specific index:
                            // val dayOfWeek = data.weekDays.first()[index].date.dayOfWeek
                        }
                }
            }
        }

        weekCalendarView.setup(startDate, endDate, daysOfWeek.first())
        weekCalendarView.scrollToWeek(currentDate)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // val selected = parent.getItemAtPosition(pos) as String
        selectedSport.postValue(sports[pos])
    }

    override fun onNothingSelected(parent: AdapterView<*>) {

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

    class WeekViewContainer(view: View) : ViewContainer(view) {
        // Alternatively, you can add an ID to the container layout and use findViewById()
        val monthTitleContainer = view as ViewGroup
        val titlesContainer = view as ViewGroup
    }
}