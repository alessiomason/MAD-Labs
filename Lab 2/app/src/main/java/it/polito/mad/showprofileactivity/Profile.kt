package it.polito.mad.showprofileactivity

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
    var rating: Float?
)