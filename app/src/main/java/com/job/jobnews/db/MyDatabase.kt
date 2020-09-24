package com.job.jobnews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.job.jobnews.model.CategoryResponse
import com.job.jobnews.model.InfoResponse
import com.job.jobnews.model.JobResponse

/**
 * Created by Itz Shovon on 4/16/2020
 */
//database class should be abstract class
@Database(
    entities = [InfoResponse::class, CategoryResponse::class, JobResponse::class], //for multiple entity add in [] with comma
    version = 1
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun getInfoDao(): InfoDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getJobDao(): JobDao

    companion object {

        @Volatile
        private var instance: MyDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MyDatabase::class.java,
            "database"
        ).build()

    }
}
