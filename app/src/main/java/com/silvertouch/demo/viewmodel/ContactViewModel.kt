package com.silvertouch.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.silvertouch.demo.model.Contact
import com.silvertouch.demo.repository.ContactRepository

class ContactViewModel(private var contactRepository: ContactRepository) : ViewModel() {

    private var contactList: LiveData<List<Contact>> = contactRepository.getAllContact()

    fun getAllContact(): LiveData<List<Contact>> {
        return contactList
    }

    fun insert(contact: Contact) {
        contactRepository.insert(contact)
    }

    fun update(contact: Contact) {
        contactRepository.update(contact)
    }

    fun delete(contact: Contact) {
        contactRepository.delete(contact)
    }
}