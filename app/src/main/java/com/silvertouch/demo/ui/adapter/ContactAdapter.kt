package com.silvertouch.demo.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.silvertouch.demo.databinding.ItemContactBinding
import com.silvertouch.demo.model.Contact
import com.silvertouch.demo.ui.activity.AddNewContactActivity
import com.silvertouch.demo.ui.activity.ContactListActivity
import com.silvertouch.demo.utils.Constants
import java.util.*

class ContactAdapter(private val mContext: ContactListActivity) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(), Filterable {

    private var contactList: List<Contact> = ArrayList()
    private var filteredList: List<Contact>

    init {
        filteredList = contactList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val mBinding = ItemContactBinding.inflate(mContext.layoutInflater, parent, false)
        return ContactViewHolder(mBinding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bindData(filteredList[holder.adapterPosition])

        holder.mBinding.imgEdit.setOnClickListener {
            val mIntent = Intent(mContext, AddNewContactActivity::class.java)
            mIntent.putExtra(Constants.INTENT_CONTACT_ID, filteredList[holder.adapterPosition].id)
            mContext.startActivity(mIntent)
            mContext.finish()
        }

        holder.mBinding.imgDelete.setOnClickListener {
            mContext.showDeleteContactConfirmationDialog(filteredList[holder.adapterPosition])
        }
    }

    fun setContact(contactList: List<Contact>) {
        this.contactList = contactList
        filteredList = contactList
        notifyDataSetChanged()
    }

    class ContactViewHolder(var mBinding: ItemContactBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bindData(contact: Contact) {
            mBinding.data = contact
            mBinding.executePendingBindings()
        }
    }

    override fun getFilter(): Filter {

        return object : Filter() {

            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    filteredList = contactList

                } else {

                    val dataList = arrayListOf<Contact>()

                    for (row in contactList) {

                        if (row.firstName?.toLowerCase()?.contains(charString.toLowerCase())!!
                            || row.lastName?.toLowerCase()?.contains(charString.toLowerCase())!!
                            || row.mobileNumber?.toLowerCase()?.contains(charString.toLowerCase())!!
                            || row.email?.toLowerCase()?.contains(charString.toLowerCase())!!
                        ) {
                            dataList.add(row)
                        }
                    }

                    filteredList = dataList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredList = filterResults.values as List<Contact>
                notifyDataSetChanged()
            }
        }
    }
}