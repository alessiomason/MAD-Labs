package it.polito.mad.playgroundsreservations.database

import com.google.firebase.firestore.DocumentSnapshot
import it.polito.mad.playgroundsreservations.profile.Gender
import it.polito.mad.playgroundsreservations.profile.toGender

data class User(
    val id: String,
    val username: String,
    var fullName: String,
    var bio: String,
    var dateOfBirth: String,
    var gender: Gender?,
    var phone: String,
    var location: String,
    var rating: Float = 0.0f
)

fun DocumentSnapshot.toUser(): User {
    val username = this.get("username", String::class.java)
    val fullName = this.get("fullName", String::class.java)
    val bio = this.get("bio", String::class.java)
    val gender = this.get("gender", String::class.java)?.toGender()
    val phone = this.get("phone", String::class.java)
    val location = this.get("location", String::class.java)
    val dateOfBirth = this.get("dateOfBirth", String::class.java)
    val rating = this.get("rating", Float::class.java)

    return User(
        id,
        username ?: "",
        fullName ?: "",
        bio ?: "",
        dateOfBirth ?: "",
        gender,
        phone ?: "",
        location ?: "",
        rating ?: (0.0).toFloat(),
    )
}
