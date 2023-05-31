package it.polito.mad.playgroundsreservations.database

enum class Sport {
    TENNIS, BASKETBALL, FOOTBALL, VOLLEYBALL, GOLF
}

const val tennis = "tennis"
const val basketball = "basketball"
const val football = "football"
const val volleyball = "volleyball"
const val golf = "golf"

fun String.toSport(): Sport {
    return when (this) {
        "tennis" -> Sport.TENNIS
        "basketball" -> Sport.BASKETBALL
        "football" -> Sport.FOOTBALL
        "volleyball" -> Sport.VOLLEYBALL
        "golf" -> Sport.GOLF
        else -> throw Exception("Non-existing sport!")
    }
}