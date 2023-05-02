package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.R


class ShowReservationFragment: Fragment(R.layout.show_reservation_fragment) {
    private val args by navArgs<ShowReservationFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.show_reservation_fragment, container, false)
    }
   /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  view.findViewById<TextView>(R.id.singleReservationTextView).text = "Single reservation " + args.reservationId
    }
    */
   override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.menu_edit_reservation, menu)
       super.onCreateOptionsMenu(menu!!, inflater)
   }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.cancelReservation -> {
                println("ciao")
                true
            }
            R.id.editReservation -> {
                println("ciao")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}