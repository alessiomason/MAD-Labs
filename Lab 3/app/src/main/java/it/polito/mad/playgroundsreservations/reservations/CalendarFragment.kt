package it.polito.mad.playgroundsreservations.reservations

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import com.stacktips.view.DayDecorator
import com.stacktips.view.DayView
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sports
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarFragment: Fragment(R.layout.calendar_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reservationsViewModel by viewModels<ReservationsViewModel>()
        val listOfReservations = reservationsViewModel.getUserReservations(1)

        //Initialize CustomCalendarView from layout
        val calendarView = view.findViewById<View>(R.id.calendar_view) as CustomCalendarView
        //Initialize calendar with date
        val currentCalendar = Calendar.getInstance(Locale.getDefault())
        val decorators: MutableList<DayDecorator> = ArrayList()

        listOfReservations.observe(viewLifecycleOwner) { reservations ->
            decorators.add(DisabledColorDecorator(reservations))
            calendarView.decorators = decorators
            calendarView.refreshCalendar(currentCalendar)
        }

        //Show Monday as first date of week
        calendarView.firstDayOfWeek = Calendar.MONDAY


        //Handling custom calendar events
   /*     calendarView.setCalendarListener(object : CalendarListener {
            override fun onDateSelected(date: Date) {
                val df = SimpleDateFormat("dd-MM-yyyy")
                Toast.makeText(activity?.applicationContext, df.format(date), Toast.LENGTH_SHORT).show()
            }

            override fun onMonthChanged(date: Date) {
                val df = SimpleDateFormat("MM-yyyy")
                Toast.makeText(activity?.applicationContext, df.format(date), Toast.LENGTH_SHORT).show()
            }
        })
*/

        val recyclerView = view.findViewById<RecyclerView>(R.id.listOfReservations)
        listOfReservations.observe(viewLifecycleOwner) {
            recyclerView.adapter = MyAdapter(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)


    }

    class MyViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private val tv: TextView = v.findViewById(R.id.reservation_box_text)

        fun bind(r: Reservation, pos: Int, onTap: (Int) -> Unit) {
            tv.text = r.sport.toString()
            super.itemView.setOnClickListener { onTap(pos) }
        }

        fun unbind() {
            super.itemView.setOnClickListener(null)
        }
    }

    class MyAdapter(val listOfReservations: List<Reservation>): RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
            return MyViewHolder(v)
        }

        override fun getItemCount(): Int {
            return listOfReservations.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(listOfReservations[position], position) {  }
        }

        override fun onViewRecycled(holder: MyViewHolder) {
            holder.unbind()
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.reservation_box_layout
        }
    }
}

private class DisabledColorDecorator(val reservations:List<Reservation>) : DayDecorator {
    override fun decorate(dayView: DayView) {
        var coincidenze = 0
        reservations.forEach {
            if(it.time.date == dayView.date.date &&
                it.time.month == dayView.date.month &&
                it.time.year == dayView.date.year
            ) {
                coincidenze++
                if (coincidenze > 1) {
                    dayView.setBackgroundResource(R.color.MULTIPLE_COLOR)
                    coincidenze--
                }
                else if (it.sport == Sports.TENNIS) {
                    dayView.setBackgroundResource(R.color.TENNIS_COLOR)
                } else if (it.sport == Sports.BASKETBALL) {
                    dayView.setBackgroundResource(R.color.BASKETBALL_COLOR)
                } else if (it.sport == Sports.FOOTBALL) {
                    dayView.setBackgroundResource(R.color.FOOTBALL_COLOR)
                } else if (it.sport == Sports.VOLLEYBALL) {
                    dayView.setBackgroundResource(R.color.VOLLEYBALL_COLOR)
                } else if (it.sport == Sports.GOLF) {
                    dayView.setBackgroundResource(R.color.GOLF_COLOR)
                }
            }
        }


    }
}




