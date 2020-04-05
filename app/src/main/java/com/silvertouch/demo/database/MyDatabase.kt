package com.silvertouch.demo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.silvertouch.demo.database.dao.CategoryDao
import com.silvertouch.demo.database.dao.ContactDao
import com.silvertouch.demo.model.Category
import com.silvertouch.demo.model.Contact
import com.silvertouch.demo.utils.Constants

@Database(
    entities = [Contact::class, Category::class],
    version = 1
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun daoCategory(): CategoryDao
    abstract fun daoContact(): ContactDao

    companion object {

        private var ourInstance: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase? {

            if (ourInstance == null) {

                synchronized(MyDatabase::class.java) {

                    if (ourInstance == null) {

                        ourInstance = Room.databaseBuilder(
                            context,
                            MyDatabase::class.java,
                            Constants.DB_NAME
                        )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .setJournalMode(JournalMode.TRUNCATE)
                            .build()
                    }
                }
            }

            return ourInstance
        }
    }
}
