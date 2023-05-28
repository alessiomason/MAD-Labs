package it.polito.mad.playgroundsreservations.profile

enum class Gender {
    MALE, FEMALE, OTHER
}

data class Profile(
    var name: String?,
    var nickname: String?,
    var bio: String?,
    var age: Int?,
    var gender: Gender?,
    var phone: String?,
    var location: String?,
    var rating: Float?,
    var userProfileImageUriString: String?
)

fun String.toGender(): Gender {
    return when (this) {
        "MALE" -> Gender.MALE
        "FEMAL" -> Gender.FEMALE
        "OTHER" -> Gender.OTHER
        "MASCHIO" -> Gender.MALE
        "FEMMINA" -> Gender.FEMALE
        "ALTRO" -> Gender.OTHER
        "M" -> Gender.MALE
        "F" -> Gender.FEMALE
        "O" -> Gender.OTHER
        "A" -> Gender.OTHER
        else -> throw Exception("Non-existing gender!")
    }
}
