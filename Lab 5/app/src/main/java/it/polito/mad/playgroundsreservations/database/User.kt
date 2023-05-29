package it.polito.mad.playgroundsreservations.database

import com.google.firebase.firestore.DocumentSnapshot
import it.polito.mad.playgroundsreservations.profile.Gender
import it.polito.mad.playgroundsreservations.profile.toGender

data class User(
    val id: String,
    val username: String,
    var firstName: String,
    var lastName: String,
    var bio: String?,
    var gender: Gender?,
    var phone: String?,
    var location: String?,
    var rating: Float = 0.0f,
    var dateOfBirth: String
)

fun DocumentSnapshot.toUser(): User {
    val username = this.get("username", String::class.java)!!
    val firstName = this.get("firstName", String::class.java)!!
    val lastName = this.get("lastName", String::class.java)!!
    val bio = this.get("bio", String::class.java)!!
    val gender = this.get("gender", String::class.java)!!.toGender()
    val phone = this.get("phone", String::class.java)!!
    val location = this.get("location", String::class.java)!!
    val dateOfBirth = this.get("dateOfBirth", String::class.java)!!
    val rating = this.get("rating", Float::class.java)!!

    return User(id, username, firstName, lastName, bio, gender, phone, location, rating, dateOfBirth)

}
