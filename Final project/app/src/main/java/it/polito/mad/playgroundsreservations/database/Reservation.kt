package it.polito.mad.playgroundsreservations.database

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import it.polito.mad.playgroundsreservations.reservations.ViewModel
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

data class Reservation(
    val id: String,
    val userId: DocumentReference,
    var playgroundId: DocumentReference,
    var sport: Sport,
    var time: ZonedDateTime,
    var duration: Duration,
    var rentingEquipment: Boolean = false,
    var invitations: SnapshotStateList<Invitation>
)

fun DocumentSnapshot.toReservation(): Reservation {
    val userId = this.get("userId", DocumentReference::class.java)!!
    val playgroundId = this.get("playgroundId", DocumentReference::class.java)!!
    val sport = this.get("sport", String::class.java)!!.toSport()
    val time = this.get("time", Timestamp::class.java)!!.toDate().toInstant().atZone(ZoneId.systemDefault())
    val duration = Duration.ofHours(this.get("duration", Long::class.java)!!)
    val rentingEquipment = this.get("rentingEquipment", Boolean::class.java)
    val invitations = this.get("invitations") as? Map<String, Map<String, String>> ?: emptyMap()

    return Reservation(
        id,
        userId,
        playgroundId,
        sport,
        time,
        duration,
        rentingEquipment ?: false,
        invitations.map {
            Invitation(
                userId = it.key,
                fullName = it.value["fullName"] ?: "",
                invitationStatus = it.value["status"]?.toInvitationStatus() ?: InvitationStatus.PENDING
            )
        }.toMutableStateList()
    )
}