package it.polito.mad.playgroundsreservations.reservations

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.R

class AddReservationFragment: Fragment(R.layout.add_reservation_fragment) {
    // private val args by navArgs<ShowReservationFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view.findViewById<TextView>(R.id.editTextFullName).text = "Ciao"
    }
}