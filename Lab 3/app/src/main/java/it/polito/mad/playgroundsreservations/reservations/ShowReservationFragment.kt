package it.polito.mad.playgroundsreservations.reservations

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.R

class ShowReservationFragment: Fragment(R.layout.show_reservation_fragment) {
    private val args by navArgs<ShowReservationFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  view.findViewById<TextView>(R.id.singleReservationTextView).text = "Single reservation " + args.reservationId
    }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.show_reservation_fragment, container, false)

        // Registra il menu contestuale per la view
      //  registerForContextMenu(view)

        return view
    }


    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.menu_edit_reservation, menu)
    }
  /*  override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        var inflater: MenuInflater? = activity?.menuInflater
        println(inflater);
        if (inflater != null) {
            inflater.inflate(R.menu.menu_edit_profile, menu )
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // Gestisci l'azione dell'utente in base alla voce del menu selezionata
        when (item.itemId) {
            1 -> {
                // Azione per la voce 1
                return true
            }
            2 -> {
                // Azione per la voce 2
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }
    */
}