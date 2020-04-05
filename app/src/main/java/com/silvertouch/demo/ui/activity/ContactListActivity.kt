package com.silvertouch.demo.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.silvertouch.demo.R
import com.silvertouch.demo.databinding.ActivityContactListBinding
import com.silvertouch.demo.model.Contact
import com.silvertouch.demo.ui.BaseActivity
import com.silvertouch.demo.ui.adapter.ContactAdapter
import com.silvertouch.demo.viewmodel.ContactViewModel
import org.koin.android.ext.android.inject

class ContactListActivity : BaseActivity<ActivityContactListBinding>() {

    private val contactViewModel: ContactViewModel by inject()

    private lateinit var contactAdapter: ContactAdapter

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_contact_list)

        init()
    }

    private fun init() {

        initToolbar(
            mBinding.toolbar.toolbar,
            R.string.header_title_contacts,
            mBinding.toolbar.txtHeaderTitle,
            true
        )

        contactAdapter = ContactAdapter(this@ContactListActivity)
        mBinding.recyclerViewContacts.adapter = contactAdapter

        getContactList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_contacts, menu)

        val myActionMenuItem = menu?.findItem(R.id.menu_search)

        searchView = myActionMenuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {

                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }

                myActionMenuItem.collapseActionView()

                return false
            }

            override fun onQueryTextChange(s: String): Boolean {

                if (::contactAdapter.isInitialized) {
                    contactAdapter.filter.filter(s)
                }

                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {

            android.R.id.home -> onBackPressed()

            R.id.menu_filter -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getContactList() {

        contactViewModel.getAllContact().observe(this,
            Observer { list ->
                list?.let {
                    contactAdapter.setContact(it)
                }
            })
    }

    fun showDeleteContactConfirmationDialog(contact: Contact) {

        val alertDialog = AlertDialog.Builder(this)

        alertDialog.setTitle(getString(R.string.title_dialog_confirmation))
            .setMessage(getString(R.string.title_dialog_msg_contact_delete))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.title_proceed)) { _, _ ->
                deleteContact(contact)
            }
            .setNegativeButton(getString(R.string.title_cancel)) { dialog, _ ->
                dialog.dismiss()
            }

        alertDialog.create().show()
    }

    private fun deleteContact(contact: Contact) {
        contactViewModel.delete(contact)
        displayShortToast("Contact Deleted")
    }
}
