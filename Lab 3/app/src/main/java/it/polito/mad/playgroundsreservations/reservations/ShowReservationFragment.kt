package it.polito.mad.playgroundsreservations.reservations

import android.app.AlertDialog
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
                AlertDialog.Builder(context)
                    .setTitle("Cancellazione prenotazione")
                    .setMessage("Sei sicuro di voler cancellare la prenotazione?") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(
                        "cancella"
                    ) { dialog, which ->
                        // Continue with delete operation

                    } // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("annulla", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
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