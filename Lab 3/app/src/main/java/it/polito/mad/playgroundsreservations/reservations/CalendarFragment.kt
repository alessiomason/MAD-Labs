package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sports
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarFragment: Fragment(R.layout.calendar_fragment) {
    private val zoneId = ZoneId.systemDefault()
    private var tappedDay = MutableLiveData(Instant.now().atZone(zoneId).toLocalDate())
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        val reservationsViewModel by viewModels<ReservationsViewModel>()
        val reservations = reservationsViewModel.getUserReservations(1)

        //Initialize CustomCalendarView from layout
        val calendarView = view.findViewById<View>(R.id.calendar_view) as CustomCalendarView
        //Initialize calendar with date
        val currentCalendar = Calendar.getInstance(Locale.getDefault())
        val decorators: MutableList<DayDecorator> = ArrayList()

        reservations.observe(viewLifecycleOwner) {
            decorators.add(DisabledColorDecorator(it))
            calendarView.decorators = decorators
            calendarView.refreshCalendar(currentCalendar)
        }

        //Show Monday as first date of week
        calendarView.firstDayOfWeek = Calendar.MONDAY

        //Handling custom calendar events
        calendarView.setCalendarListener(object : CalendarListener {
            override fun onDateSelected(date: Date?) {
                if (date != null)
                    tappedDay.postValue(date.toInstant().atZone(zoneId).toLocalDate())
            }

            override fun onMonthChanged(date: Date?) { }
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.listOfReservations)

        tappedDay.observe(viewLifecycleOwner) { tappedDayDate ->
            reservations.observe(viewLifecycleOwner) {
                val displayedReservations = it.filter { r ->
                    val reservationLocalDate = r.time.withZoneSameInstant(zoneId).toLocalDate()
                    reservationLocalDate.isEqual(tappedDayDate)
                }
                recyclerView.adapter = MyAdapter(displayedReservations, navController)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
    }

    class MyViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private val titleTextView = view.findViewById<TextView>(R.id.reservation_box_title)
        private val sportTextView = view.findViewById<TextView>(R.id.reservation_box_sport)
        private val durationTextView = view.findViewById<TextView>(R.id.reservation_box_duration)
        private val playgroundTextView = view.findViewById<TextView>(R.id.reservation_box_playground)

        fun bind(r: Reservation, pos: Int, onTap: (Int) -> Unit) {
            titleTextView.text = view.context.getString(R.string.reservation_box_title, r.time.toLocalDate(), r.time.toLocalTime())

            val sportText = when (r.sport) {
                Sports.TENNIS -> R.string.sport_tennis
                Sports.BASKETBALL -> R.string.sport_basketball
                Sports.FOOTBALL -> R.string.sport_football
                Sports.VOLLEYBALL -> R.string.sport_volleyball
                Sports.GOLF -> R.string.sport_golf
            }
            sportTextView.text = view.context.getString(sportText)

            durationTextView.text = view.context.getString(R.string.reservation_box_duration, r.duration.toHours())
            playgroundTextView.text = view.context.getString(R.string.reservation_box_playground_name, r.playgroundId.toString())

            super.itemView.setOnClickListener { onTap(pos) }
        }

        fun unbind() {
            super.itemView.setOnClickListener(null)
        }
    }

    class MyAdapter(
        private val reservations: List<Reservation>,
        private val navController: NavController
        ): RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
            return MyViewHolder(v)
        }

        override fun getItemCount(): Int {
            return reservations.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(reservations[position], position) {
                val action = CalendarFragmentDirections.actionCalendarFragmentToShowReservationFragment(reservations[position].id)
                navController.navigate(action)
            }
        }

        override fun onViewRecycled(holder: MyViewHolder) {
            holder.unbind()
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.reservation_box_layout
        }
    }
}

private class DisabledColorDecorator(val reservations: List<Reservation>) : DayDecorator {
    private val zoneId = ZoneId.systemDefault()

    override fun decorate(dayView: DayView) {
        var coincidenze = 0
        reservations.forEach { reservation ->
            val reservationDate = reservation.time.withZoneSameInstant(zoneId).toLocalDate()
            val dayViewDate = dayView.date.toInstant().atZone(zoneId).toLocalDate()

            if(reservationDate.isEqual(dayViewDate)) {
                coincidenze++
                if (coincidenze > 1) {
                    dayView.setBackgroundResource(R.color.MULTIPLE_COLOR)
                    coincidenze--
                }
                else if (reservation.sport == Sports.TENNIS) {
                    dayView.setBackgroundResource(R.color.TENNIS_COLOR)
                } else if (reservation.sport == Sports.BASKETBALL) {
                    dayView.setBackgroundResource(R.color.BASKETBALL_COLOR)
                } else if (reservation.sport == Sports.FOOTBALL) {
                    dayView.setBackgroundResource(R.color.FOOTBALL_COLOR)
                } else if (reservation.sport == Sports.VOLLEYBALL) {
                    dayView.setBackgroundResource(R.color.VOLLEYBALL_COLOR)
                } else if (reservation.sport == Sports.GOLF) {
                    dayView.setBackgroundResource(R.color.GOLF_COLOR)
                }
            }
        }
    }
}
