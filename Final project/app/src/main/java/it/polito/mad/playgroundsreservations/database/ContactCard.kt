package it.polito.mad.playgroundsreservations.database

data class ContactCard(
    val id: String,
    var profileName: String?,
    var profileEmail: String?,
    var profileBio: String?,
    var isFriend: Boolean,
)