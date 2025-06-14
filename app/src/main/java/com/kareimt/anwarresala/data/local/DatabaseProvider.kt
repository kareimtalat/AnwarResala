package com.kareimt.anwarresala.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kareimt.anwarresala.data.local.branch.BranchDatabase
import com.kareimt.anwarresala.data.local.course.CourseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseProvider {
    @Volatile
    private var INSTANCE: CourseDatabase? = null
    fun getDatabase(context: Context): CourseDatabase {
        return INSTANCE ?: synchronized (this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                CourseDatabase::class.java,
                "app_database"
            )
//                .fallbackToDestructiveMigration() // Handle migrations by recreating the database
                .addCallback(object : RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Use a coroutine to insert hardcoded courses
                        CoroutineScope(Dispatchers.IO).launch {
                            //getDatabase(context).courseDao().insertCourses(getHardcodedCourses(context))
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
        }
    }

    @Volatile
    private var INSTANCE_BRANCH: BranchDatabase? = null
    fun getBranchDatabase(context: Context): BranchDatabase {
        return INSTANCE_BRANCH ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                BranchDatabase::class.java,
                "branch_database"
            )
//                .fallbackToDestructiveMigration() // Handle migrations by recreating the database if there is a schema change
                .build()
            INSTANCE_BRANCH = instance
            instance
        }
    }
}