package com.silvertouch.demo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.silvertouch.demo.model.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM Contact")
    fun getContact(): LiveData<List<Contact>>

    @Query("SELECT * FROM Contact WHERE id = :contactId")
    fun getContactById(contactId: Int): Contact

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: Contact)

    @Delete
    fun deleteContact(contact: Contact)

    @Update
    fun updateContact(contact: Contact)
}
