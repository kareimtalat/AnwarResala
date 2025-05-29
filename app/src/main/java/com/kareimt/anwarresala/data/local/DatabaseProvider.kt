package com.kareimt.anwarresala.data.local

import androidx.room.Room
import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kareimt.anwarresala.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase{
        return INSTANCE ?: synchronized (this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            )
                //                .addCallback(object : RoomDatabase.Callback(){
                //                    override fun onCreate(db: SupportSQLiteDatabase) {
                //                        super.onCreate(db)
                //                        // Use a coroutine to insert hardcoded courses
                //                        CoroutineScope(Dispatchers.IO).launch{
                //                            //getDatabase(context).courseDao().insertCourses(getHardcodedCourses(context))
                //                        }
                //                    }
                //                })

                // Use the next line if changed the database schema and there is
                // problems i can't handel .. this will delete all data in the database
                //.fallbackToDestructiveMigration(false)
                .build()
                INSTANCE = instance
                instance
        }
    }
}

//fun getHardcodedCourses(context: Context): List<CourseEntity> = listOf(
//    CourseEntity(
//        id = 1,
//        branch = context.getString(R.string.tenth_of_ramadan),
//        imagePath = R.drawable.before_marraige,
//        category = context.getString(R.string.human_development),
//        title = context.getString(R.string.before_marriage_we_should),
//        type = "ONLINE",
//        instructorName = context.getString(R.string.dr_layla_saad),
//        instructorBio = context.getString(R.string.dr_layla_saad_bio),
//        instructorImageResId = 0,
//        startDate = "2025-02-07",
//        wGLink = "https://chat.whatsapp.com/CcsREQlt8qL55hkBWzuCoj",
//        courseDetails = context.getString(R.string.before_marriage_de),
//        totalLectures = 4,
//        noOfLiteraturesFinished = 3,
//        nextLecture = "2025-02-28 20:00",
//        organizerName = context.getString(R.string.fatema_rabee3),
//        organizerWhatsapp = "+201093715627"
//    ),
//    CourseEntity(
//        id = 2,
//        branch = context.getString(R.string.tenth_of_ramadan),
//        category = context.getString(R.string.religious),
//        title = context.getString(R.string.how_to_raise_your_son),
//        type = "ONLINE",
//        instructorName = context.getString(R.string.ms_reham_hamdey),
//        instructorBio = context.getString(R.string.ms_reham_hamdey_bio),
//        instructorImageResId = 0,
//        startDate = "2025-02-09",
//        wGLink = "https://chat.whatsapp.com/GtTsCKUZWOnKa9ChVWCoq4",
//        courseDetails = context.getString(R.string.how_to_raise_your_son_de),
//        totalLectures = 4,
//        noOfLiteraturesFinished = 2,
//        nextLecture = "2025-02-23 20:00",
//        organizerName = context.getString(R.string.tasnim),
//        organizerWhatsapp = "+201032097183"
//    ),
//    CourseEntity(
//        id = 3,
//        branch = context.getString(R.string.tenth_of_ramadan),
//        imagePath = R.drawable.soft_skills,
//        category = context.getString(R.string.human_development),
//        title = context.getString(R.string.soft_skills),
//        type = "ONLINE",
//        instructorName = context.getString(R.string.ms_ragaa_mohamed),
//        instructorBio = context.getString(R.string.ms_ragaa_mohamed_bio),
//        instructorImageResId = 0,
//        startDate = "2025-02-15",
//        wGLink = "https://chat.whatsapp.com/JNVyhzuzvAV2KqT5PTnoSk",
//        courseDetails = context.getString(R.string.soft_skills_de),
//        totalLectures = 1,
//        noOfLiteraturesFinished = 0,
//        nextLecture = "2025-02-15 20:00",
//        organizerName = context.getString(R.string.ziko),
//        organizerWhatsapp = "+201068484875"
//    ),
//    CourseEntity(
//        id = 4,
//        branch = context.getString(R.string.tenth_of_ramadan),
//        category = context.getString(R.string.human_development),
//        title = context.getString(R.string.interview_skills),
//        type = "OFFLINE",
//        instructorName = context.getString(R.string.mr_hossam_elsayed),
//        instructorBio = context.getString(R.string.mr_hossam_elsayed_bio),
//        instructorImageResId = R.drawable.hossam_istractor,
//        startDate = "2025-02-22",
//        wGLink = "https://chat.whatsapp.com/EnYYN2MxPbsAtyiQjqJqWs",
//        courseDetails = context.getString(R.string.interview_skills_de),
//        totalLectures = 1,
//        noOfLiteraturesFinished = 1,
//        nextLecture = "2025-02-22 14:30",
//        organizerName = context.getString(R.string.kareim_talat),
//        organizerWhatsapp = "+201030843508"
//    )
//)
