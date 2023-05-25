package it.polito.mad.playgroundsreservations.database

import com.google.firebase.firestore.DocumentSnapshot

data class Playground(
    val id: String,
    val name: String,
    val address: String,
    val sport: Sport,
    val averageRating: Float? = null
)

fun DocumentSnapshot.toPlayground(): Playground {
    val name = this.get("name", String::class.java)
    val address = this.get("address", String::class.java)
    val sport = this.get("sport", String::class.java)!!.toSport()
    val averageRating = this.get("averageRating", Float::class.java)

    return Playground(id,name ?: "", address ?: "", sport, averageRating)
}