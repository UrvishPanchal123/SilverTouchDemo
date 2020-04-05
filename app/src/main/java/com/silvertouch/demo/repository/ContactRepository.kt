package com.silvertouch.demo.repository

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.silvertouch.demo.database.dao.ContactDao
import com.silvertouch.demo.model.Contact

class ContactRepository(private val contactDao: ContactDao) {

    private val contactList: LiveData<List<Contact>> = contactDao.getContact()

    fun getAllContact(): LiveData<List<Contact>> {
        return contactList
    }

    fun insert(contact: Contact) {
        InsertContactAsyncTask(
            contactDao
        ).execute(contact)
    }

    private class InsertContactAsyncTask(val contactDao: ContactDao) :
        AsyncTask<Contact, Unit, Unit>() {

        override fun doInBackground(vararg contact: Contact?) {
            contactDao.insertContact(contact[0]!!)
        }
    }

    fun update(contact: Contact) {
        UpdateContactAsyncTask(
            contactDao
        ).execute(contact)
    }

    private class UpdateContactAsyncTask(val contactDao: ContactDao) :
        AsyncTask<Contact, Unit, Unit>() {

        override fun doInBackground(vararg contact: Contact?) {
            contactDao.updateContact(contact[0]!!)
        }
    }

    fun delete(contact: Contact) {
        DeleteContactAsyncTask(
            contactDao
        ).execute(contact)
    }

    private class DeleteContactAsyncTask(val contactDao: ContactDao) :
        AsyncTask<Contact, Unit, Unit>() {

        override fun doInBackground(vararg contact: Contact?) {
            contactDao.deleteContact(contact[0]!!)
        }
    }
}