package it.polito.mad.playgroundsreservations.database

import com.google.firebase.firestore.DocumentSnapshot

data class User(
    val id: String,
    //val username: String,
    var fullName: String,
    var bio: String,
    var dateOfBirth: String,
    var gender: Gender?,
    var phone: String,
    var location: String,
    var rating: Float = 0.0f,
    var selectedSports: MutableSet<Sport> = mutableSetOf(),
    var alreadyShownTutorial: Boolean = false
)

fun DocumentSnapshot.toUser(): User {

  //  val username = this.get("username", String::class.java)
    val fullName = this.get("fullName", String::class.java)
    val bio = this.get("bio", String::class.java)
    val gender = this.get("gender", String::class.java)?.toGender()
    val phone = this.get("phone", String::class.java)
    val location = this.get("location", String::class.java)
    val dateOfBirth = this.get("dateOfBirth", String::class.java)
    val rating = this.get("rating", Float::class.java)
    val selectedSportsStrings = this.get("selectedSports") as? List<String>
    val alreadyShownTutorial = this.get("alreadyShownTutorial", Boolean::class.java)

    val selectedSports = selectedSportsStrings?.map { it.toSport() }?.toMutableSet() ?: mutableSetOf()

    return User(
        id,
      //  username ?: "",
        fullName ?: "",
        bio ?: "",
        dateOfBirth ?: "",
        gender,
        phone ?: "",
        location ?: "",
        rating ?: (0.0).toFloat(),
        selectedSports,
        alreadyShownTutorial ?: false
    )
}
