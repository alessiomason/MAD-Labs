package it.polito.mad.playgroundsreservations.reservations

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import it.polito.mad.playgroundsreservations.Global
import it.polito.mad.playgroundsreservations.R
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sport
import it.polito.mad.playgroundsreservations.profile.SpinnerFragment
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ShowReservationFragment: Fragment(R.layout.show_reservation_fragment) {
    private val args by navArgs<ShowReservationFragmentArgs>()
    private val viewModel by viewModels<ViewModel>()
    private lateinit var myReservation: Reservation
    private lateinit var myPlayground: Playground

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.show_reservation_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<ViewModel>()
        val reservations = viewModel.getUserReservations(Global.userId!!)
        val playgrounds = viewModel.playgrounds

        val loading = view.findViewById<FragmentContainerView>(R.id.loadingShowReservationFragment)
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(R.id.loadingShowReservationFragment, SpinnerFragment()).commit()

        // ACTIVITY TITLE
        activity?.title = activity?.resources?.getString(R.string.reservation)

        reservations.observe(viewLifecycleOwner) { reservationsList ->
                reservationsList.forEach { r ->
                    if (r.id == args.reservationId) {
                        myReservation = r
                        // invalidate the menu to see whether editing and canceling is still possible
                        invalidateOptionsMenu(activity)
                    }
                }

            playgrounds.observe(viewLifecycleOwner) { playgroundsList ->
                playgroundsList.forEach { p ->
                    if (p.id == myReservation.playgroundId.id) {
                         myPlayground = p
                        loading.visibility = GONE
                    } else {
                        loading.visibility = VISIBLE
                    }
                }

                val sportName = when (myReservation.sport) {
                    Sport.BASKETBALL -> resources.getString(R.string.sport_basketball)
                    Sport.TENNIS -> resources.getString(R.string.sport_tennis)
                    Sport.FOOTBALL -> resources.getString(R.string.sport_football)
                    Sport.VOLLEYBALL -> resources.getString(R.string.sport_volleyball)
                    Sport.GOLF -> resources.getString(R.string.sport_golf)
                }

                val btnRateCourt = view.findViewById<Button>(R.id.btnRateCourt)
                val myReviewLayout = view.findViewById<LinearLayout>(R.id.myReviewLayout)
                // myReviewLayout.visibility = GONE
                // display rate court button only for past reservations and if not already rated
                val previousRatingForReservation = viewModel.getRatingByReservation(myReservation.id)

                previousRatingForReservation.observe(viewLifecycleOwner) { rating ->
                    if (myReservation.time.plus(myReservation.duration).isBefore(Instant.now().atZone(myReservation.time.zone))
                        && rating == null
                    ) {
                        btnRateCourt.visibility = VISIBLE
                        btnRateCourt.setOnClickListener {
                            val navController = view.findNavController()
                            val action = ShowReservationFragmentDirections.actionShowReservationFragmentToRatingPlaygrounds(myReservation.playgroundId.id, myReservation.id)
                            navController.navigate(action)
                        }
                    } else {
                        btnRateCourt.visibility = GONE
                        if (rating != null) {
                            myReviewLayout.visibility = VISIBLE
                            val myRatingBar = view.findViewById<RatingBar>(R.id.myReviewScore)
                            myRatingBar.setIsIndicator(true)
                            myRatingBar.rating = rating.rating
                            val myDescription = view.findViewById<TextView>(R.id.myReviewDescription)
                            if (rating.description == "") {
                                myDescription.text = resources.getString(R.string.optional_description_entered)
                            } else {
                                myDescription.text = "\"" + rating.description + "\""
                            }
                        }
                    }
                }

                val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
                ratingBar.setIsIndicator(true)

                var totalRating = 0.0f
                val ratingBarValue = viewModel.getRatingsByPlaygroundIdFragment(myPlayground.id)
                ratingBarValue.observe(viewLifecycleOwner) { ratingPlaygroundsList ->
                    if (ratingPlaygroundsList.isEmpty()){
                        ratingBar.rating = (0.0).toFloat()
                    } else {
                        ratingPlaygroundsList.forEach { r ->
                            totalRating += r?.rating!!
                        }
                        ratingBar.rating = totalRating / ratingPlaygroundsList.size
                    }
                }

                view.findViewById<TextView>(R.id.playgroundName).text = myPlayground.name
                view.findViewById<TextView>(R.id.sportName).text = sportName

                view.findViewById<TextView>(R.id.timeInfo).text = myReservation.time.toLocalDate().format(DateTimeFormatter.ofLocalizedDate((FormatStyle.FULL))) + " - " +
                        myReservation.time.toLocalTime().format(DateTimeFormatter.ofLocalizedTime((FormatStyle.SHORT)))


                view.findViewById<TextView>(R.id.durationInfo).text = resources.getString(R.string.duration) + ": "+ myReservation.duration.toHours().toString() + "h"

                if (myReservation.rentingEquipment) {
                    view.findViewById<ImageView>(R.id.rentingEquipmentIconTrue).visibility = VISIBLE
                    view.findViewById<ImageView>(R.id.rentingEquipmentIconFalse).visibility = GONE
                } else {
                    view.findViewById<ImageView>(R.id.rentingEquipmentIconTrue).visibility = GONE
                    view.findViewById<ImageView>(R.id.rentingEquipmentIconFalse).visibility = VISIBLE
                }
                val image = view.findViewById<ImageView>(R.id.reservationImage)
                val sportIcon = view.findViewById<ImageView>(R.id.sportNameIcon)

                when (myReservation.sport) {
                    Sport.TENNIS -> { image.setImageResource(R.drawable.tennis_court); sportIcon.setImageResource(R.drawable.tennis_ball) }
                    Sport.BASKETBALL -> { image.setImageResource(R.drawable.basketball_court); sportIcon.setImageResource(R.drawable.basketball_ball) }
                    Sport.FOOTBALL -> { image.setImageResource(R.drawable.football_pitch); sportIcon.setImageResource(R.drawable.football_ball) }
                    Sport.VOLLEYBALL -> { image.setImageResource(R.drawable.volleyball_court); sportIcon.setImageResource(R.drawable.volleyball_ball) }
                    Sport.GOLF -> { image.setImageResource(R.drawable.golf_field); sportIcon.setImageResource(R.drawable.golf_ball) }
                }
            }
        }
     }

   @Deprecated("Deprecated in Java")
   override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       super.onCreateOptionsMenu(menu, inflater)
       inflater.inflate(R.menu.menu_edit_reservation, menu)

       val editMenuItem = menu.findItem(R.id.editReservation)
       val cancelMenuItem = menu.findItem(R.id.cancelReservation)

       // don't allow editing and canceling for past reservations
       if (this::myReservation.isInitialized && myReservation.time.isBefore(Instant.now().atZone(myReservation.time.zone))) {
           editMenuItem.isEnabled = false
           editMenuItem.isVisible = false
           cancelMenuItem.isEnabled = false
           cancelMenuItem.isVisible = false
       }
   }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController= view?.findNavController()
        var action=ShowReservationFragmentDirections.actionShowReservationFragmentToCalendarFragment()
        // Handle item selection
        return when (item.itemId) {
            R.id.cancelReservation -> {
                AlertDialog.Builder(context)
                    .setTitle(resources.getString(R.string.delete_reservation_title))
                    .setMessage(resources.getString(R.string.delete_reservation_message)) // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(
                        resources.getString(R.string.delete_reservation_delete_button)
                    ) { _, _ ->
                        val fragmentManager = requireFragmentManager()
                        fragmentManager.popBackStack()
                        // Continue with delete operation
                        viewModel.deleteReservation(myReservation)
                        navController?.navigate(action)
                    } // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(R.string.delete_reservation_cancel_button, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
                true
            }
            R.id.editReservation -> {
                if (navController != null) {
                    action = ShowReservationFragmentDirections.actionShowReservationFragmentToEditReservationFragment(myReservation.id)
                    navController.navigate(action)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}