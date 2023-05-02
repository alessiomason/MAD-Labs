package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sports
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

class EditReservationFragment: Fragment(R.layout.edit_reservation_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.edit_reservation_fragment, container, false)
    }

    private val args by navArgs<EditReservationFragmentArgs>()
    val zoneId = ZoneId.of("UTC+02:00")
    var aus = 0
    var hours = mutableListOf<String>()
    var duration = mutableListOf<String>()
    var myReservation = Reservation(
        userId = 0,
        playgroundId = 0,
        sport = Sports.VOLLEYBALL,
        time = ZonedDateTime.of(2023, 5, 26, 14, 0, 0, 0, zoneId),
        duration = Duration.ofHours(1)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reservationsViewModel by viewModels<ReservationsViewModel>()
        val reservations = reservationsViewModel.getUserReservations(1)
        val playgrounds = reservationsViewModel.playgrounds

        var arrayOccupated = mutableListOf<String>();


        var myPlayground = Playground(
            id = 0,
            name = "temp",
            address = "",
            sport = Sports.VOLLEYBALL
        )

        reservations.observe(viewLifecycleOwner) {
            it.forEach { r ->
                if (r.id == args.resId) {
                    myReservation = r
                }
            }
            it.forEach { r ->
                if (r.time.year == myReservation.time.year && r.time.month == myReservation.time.month && r.time.dayOfMonth == myReservation.time.dayOfMonth && r.id!=myReservation.id) {
                    arrayOccupated.add(r.time.hour.toString() + ":00");
                    aus = r.time.hour
                    for (i in 1 until r.duration.toHours()) {
                        arrayOccupated.add((aus + 1).toString() + ":00")
                        aus = aus + 1
                    }
                    Log.d("C", arrayOccupated.toString())
                }
            }
            Log.d("MY_RES", myReservation.toString())

            playgrounds.observe(viewLifecycleOwner) {
                it.forEach { p ->
                    if (p.id == myReservation.playgroundId) {
                        myPlayground = p
                    }
                }

                for (hour in 8..24) {
                    hours.add("$hour:00")
                }
                hours.removeAll(arrayOccupated);
                Log.d("listaFinale", hours.toString())

                val adapter =
                    activity?.let {
                        ArrayAdapter(
                            it.applicationContext,
                            android.R.layout.simple_spinner_dropdown_item,
                            hours.toTypedArray()
                        )
                    }
                val durationAdapter =
                    activity?.let {
                        ArrayAdapter(
                            it.applicationContext,
                            android.R.layout.simple_spinner_dropdown_item,
                           duration
                        )
                    }
                val sportName = myReservation.sport.toString().replaceFirstChar { c -> c.uppercase() }
                view.findViewById<TextView>(R.id.sportName).text = sportName
                view.findViewById<TextView>(R.id.playgroundName).text = myPlayground.name
                view.findViewById<CheckBox>(R.id.rentingEquipment).isChecked =
                    myReservation.rentingEquipment


                val spinner = view.findViewById<Spinner>(R.id.spinnerViewHours)
                spinner.adapter = adapter
                spinner.setSelection((myReservation.time.hour-8))
                val spinnerDuration=view.findViewById<Spinner>(R.id.spinnerDuration)
                spinnerDuration.adapter=durationAdapter

                var i=0
                var oraTotale=0
                var esci=false;
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        val selectedItem = parent.getItemAtPosition(position).toString()
                        Log.d("AAA", selectedItem.toString())
                        oraTotale=selectedItem.split(':')[0].toInt()
                        Log.d("Ora totale: ",oraTotale.toString());
                        esci=false
                        i=0;
                        while (i!=4 && oraTotale<24 && !esci)
                        {
                          i=i+1
                            if(parent.getItemAtPosition((position+i)).toString().split(":")[0].toInt()==oraTotale+1)
                            {
                                oraTotale=oraTotale+1
                            }
                            else
                                esci=true;
                        }
                        if(i==4)
                            i=3;
                        Log.d("BOH",i.toString());
                        duration.removeAll(duration)
                        for(j in 1 .. i)
                            duration.add(j.toString()+" h")
                        if (durationAdapter != null) {
                            durationAdapter.notifyDataSetChanged()
                        }




                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_profile, menu)
        super.onCreateOptionsMenu(menu!!, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.save_profile -> {
                return true
            }
            }
            return super.onOptionsItemSelected(item)
    }
}