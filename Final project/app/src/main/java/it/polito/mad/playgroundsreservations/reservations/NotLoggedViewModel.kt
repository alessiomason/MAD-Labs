package it.polito.mad.playgroundsreservations.reservations

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import it.polito.mad.playgroundsreservations.Global
import it.polito.mad.playgroundsreservations.database.Invitation
import it.polito.mad.playgroundsreservations.database.InvitationStatus
import it.polito.mad.playgroundsreservations.database.Playground
import it.polito.mad.playgroundsreservations.database.PlaygroundRating
import it.polito.mad.playgroundsreservations.database.Reservation
import it.polito.mad.playgroundsreservations.database.Sport
import it.polito.mad.playgroundsreservations.database.User
import it.polito.mad.playgroundsreservations.database.toPlayground
import it.polito.mad.playgroundsreservations.database.toPlaygroundRating
import it.polito.mad.playgroundsreservations.database.toReservation
import it.polito.mad.playgroundsreservations.database.toUser
import java.util.Date

// AGGIUNGERE controlli di conflitti fatti dal db in precedenza
// CAMBIARE value!! con controllo != null

class NotLoggedViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "VIEW_MODEL"
        const val reservationsCollectionPath = "reservations"
    }

    private val db = Firebase.firestore

    val playgrounds = MutableLiveData<List<Playground>>()
    val reservations = MutableLiveData<List<Reservation>>()

    init {
        // set listener for playgrounds
        db.collection(ViewModel.playgroundsCollectionPath)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(ViewModel.TAG, "Failed to read playgrounds.", error)
                    playgrounds.value = emptyList()
                    return@addSnapshotListener
                }

                val playgroundsList = mutableListOf<Playground>()
                for (doc in value!!) {
                    val playground = doc.toPlayground()
                    playgroundsList.add(playground)
                }

                playgrounds.value = playgroundsList
            }

        // set listener for reservations
        db.collection(ViewModel.reservationsCollectionPath)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(ViewModel.TAG, "Failed to read reservations.", error)
                    reservations.value = emptyList()
                    return@addSnapshotListener
                }

                val reservationsList = mutableListOf<Reservation>()
                for (doc in value!!) {
                    val reservation = doc.toReservation()
                    reservationsList.add(reservation)
                }

                reservations.value = reservationsList
            }
    }

    fun getReservedPlaygrounds(sport: Sport?): LiveData<Map<Reservation, Playground>> {
        val reservedPlaygrounds = MutableLiveData<Map<Reservation, Playground>>()
        // get Reservation by Sport
        if (sport != null) {
            db.collection(reservationsCollectionPath)
                .whereEqualTo("sport", sport.name.lowercase())
                .orderBy("time")
                .orderBy("duration")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w(TAG, "Failed to read reserved playgrounds.", error)
                        reservedPlaygrounds.value = emptyMap()
                        return@addSnapshotListener
                    }

                    val reservedPlaygroundsMap = mutableMapOf<Reservation, Playground>()
                    for (doc in value!!) {
                        val reservation = doc.toReservation()

                        reservation.playgroundId.get()
                            .addOnSuccessListener {
                                val playground = it.toPlayground()
                                reservedPlaygroundsMap[reservation] = playground
                                reservedPlaygrounds.value = reservedPlaygroundsMap
                            }
                    }
                }
        } else { // get all reservations
            db.collection(reservationsCollectionPath)
                .orderBy("time")
                .orderBy("duration")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w(TAG, "Failed to read reserved playgrounds.", error)
                        reservedPlaygrounds.value = emptyMap()
                        return@addSnapshotListener
                    }

                    val reservedPlaygroundsMap = mutableMapOf<Reservation, Playground>()
                    for (doc in value!!) {
                        val reservation = doc.toReservation()

                        reservation.playgroundId.get()
                            .addOnSuccessListener {
                                val playground = it.toPlayground()
                                reservedPlaygroundsMap[reservation] = playground
                                reservedPlaygrounds.value = reservedPlaygroundsMap
                            }
                    }
                }
        }

        return reservedPlaygrounds
    }
}

