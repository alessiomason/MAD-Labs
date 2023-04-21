package it.polito.mad.playgroundsreservations.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(entities = [Reservation::class, User::class], version = 2/*, autoMigrations = [AutoMigration(from = 1, to = 2)]*/)
abstract class Database: RoomDatabase() {
    abstract fun reservationsDao(): ReservationsDao

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database =
            (INSTANCE ?:
            synchronized(this) {
                val i = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = i
                INSTANCE
            })!!
    }
}