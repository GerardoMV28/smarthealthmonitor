package mx.utng.smarthealthmonitor.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LecturaFC::class], version = 2, exportSchema = false)
abstract class SmartHealthDatabase : RoomDatabase() {
    abstract fun lecturaFcDao(): LecturaFcDao

    companion object {
        @Volatile
        private var INSTANCE: SmartHealthDatabase? = null

        fun getInstance(context: Context): SmartHealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartHealthDatabase::class.java,
                    "smarthealthmonitor_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
