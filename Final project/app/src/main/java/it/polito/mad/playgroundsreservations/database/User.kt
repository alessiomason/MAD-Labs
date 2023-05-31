package it.polito.mad.playgroundsreservations.database

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

data class User(
    val id: String,
    var fullName: String,
    var bio: String,
    var dateOfBirth: Timestamp?,
    var gender: Gender?,
    var phone: String,
    var location: String,
    var rating: Float = 0.0f,
    var selectedSports: MutableSet<Sport> = mutableSetOf(),
    var friends: List<DocumentReference>,
    var recentlyInvited: List<DocumentReference>,
    var alreadyShownTutorial: Boolean = false,
    var mySports: MutableMap<String, Float> = mutableMapOf()
) {
    val age: Int?
        get() {
            if (dateOfBirth == null) return null

            val age = Period.between(
                dateOfBirth!!.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalDate.now()
            )
            return age.years
        }
}

fun DocumentSnapshot.toUser(): User {
    val fullName = this.get("fullName", String::class.java)
    val bio = this.get("bio", String::class.java)
    val gender = this.get("gender", String::class.java)?.toGender()
    val phone = this.get("phone", String::class.java)
    val location = this.get("location", String::class.java)
    val dateOfBirth = this.get("dateOfBirth", Timestamp::class.java)
    val rating = this.get("rating", Float::class.java)
    val selectedSportsStrings = this.get("selectedSports") as? List<String>
    val friends = this.get("friends") as? List<DocumentReference> ?: emptyList()
    val recentlyInvited = this.get("recentlyInvited") as? List<DocumentReference> ?: emptyList()
    val alreadyShownTutorial = this.get("alreadyShownTutorial", Boolean::class.java)

    val mySportsStrings = this.get("mySports") as? Map<String, Float>

    val selectedSports = selectedSportsStrings?.map { it.toSport() }?.toMutableSet() ?: mutableSetOf()
    val mySports = mySportsStrings?.mapValues { (_, rating) -> rating }?.toMutableMap() ?: mutableMapOf()

    return User(
        id,
        fullName ?: "",
        bio ?: "",
        dateOfBirth,
        gender,
        phone ?: "",
        location ?: "",
        rating ?: (0.0).toFloat(),
        selectedSports,
        friends,
        recentlyInvited,
        alreadyShownTutorial ?: false,
        mySports
    )
}
