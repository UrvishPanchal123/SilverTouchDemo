package com.silvertouch.demo.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kotlinpermissions.KotlinPermissions
import com.robertlevonyan.components.picker.ItemModel
import com.robertlevonyan.components.picker.PickerDialog
import com.silvertouch.demo.R
import com.silvertouch.demo.databinding.ActivityAddNewContactBinding
import com.silvertouch.demo.model.Category
import com.silvertouch.demo.model.Contact
import com.silvertouch.demo.ui.BaseActivity
import com.silvertouch.demo.utils.Constants
import com.silvertouch.demo.viewmodel.ContactViewModel
import org.koin.android.ext.android.inject

class AddNewContactActivity : BaseActivity<ActivityAddNewContactBinding>() {

    private val contactViewModel: ContactViewModel by inject()

    private lateinit var askPermission: KotlinPermissions.PermissionCore

    private var isEditable = false
    private var contactId = 0
    private var selectedCategoryId = 0

    private var filePath = ""

    private var contact = Contact()

    private var categoryList: List<Category> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_add_new_contact)

        init()
    }

    private fun init() {

        initToolbar(
            mBinding.toolbar.toolbar,
            R.string.header_title_add_contact,
            mBinding.toolbar.txtHeaderTitle,
            true
        )

        if (intent.hasExtra(Constants.INTENT_CONTACT_ID)) {
            isEditable = true
            contactId = intent.getIntExtra(Constants.INTENT_CONTACT_ID, 0)
        }

        if (isEditable && contactId > 0) {

            mBinding.btnSubmit.text = getString(R.string.title_update)
            contact = mDatabase.daoContact().getContactById(contactId)
            setContactDetails(contact)

        } else {
            prepareCategoryList()
        }

        mBinding.btnSubmit.setOnClickListener {
            if (checkValidations()) {
                saveContact()
            }
        }

        mBinding.imgProfile.setOnClickListener {
            openImagePicker()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setContactDetails(contact: Contact) {

        mBinding.data = contact
        mBinding.executePendingBindings()

        selectedCategoryId = contact.categoryId?.toInt()!!
        prepareCategoryList()
    }

    private fun prepareCategoryList() {

        categoryList = mDatabase.daoCategory().getAllCategory()

        val categoryArray = arrayListOf<String>()

        for (element in categoryList) {
            categoryArray.add(element.categoryName!!)
        }

        if (isEditable) {
            val categoryName = mDatabase.daoCategory().getCategoryNameById(selectedCategoryId)
            mBinding.edtCategory.setText(categoryName)
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categoryArray
        )
        mBinding.edtCategory.threshold = 0
        mBinding.edtCategory.setAdapter(adapter)
        mBinding.edtCategory.setOnClickListener { mBinding.edtCategory.showDropDown() }

        mBinding.edtCategory.setOnItemClickListener { parent, view, position, id ->
            selectedCategoryId = categoryList[position].id!!
        }
    }

    private fun checkValidations(): Boolean {

        if (getTrimString(mBinding.edtFirstName).isNotEmpty()) {

            if (getTrimString(mBinding.edtLastName).isNotEmpty()) {

                if (getTrimString(mBinding.edtMobileNumber).isNotEmpty()) {

                    if (getTrimString(mBinding.edtMobileNumber).length == 10) {

                        return if (getTrimString(mBinding.edtEmail).isNotEmpty()) {

                            if (android.util.Patterns.EMAIL_ADDRESS.matcher(getTrimString(mBinding.edtEmail))
                                    .matches()
                            ) {
                                true
                            } else {
                                displayShortToast("Invalid Email Address")
                                false
                            }

                        } else {
                            displayShortToast("Email is mandatory")
                            false
                        }

                    } else {
                        displayShortToast("Invalid Mobile Number")
                        return false
                    }

                } else {
                    displayShortToast("Mobile no. is mandatory")
                    return false
                }

            } else {
                displayShortToast("Last name is mandatory")
                return false
            }

        } else {
            displayShortToast("First name is mandatory")
            return false
        }
    }

    private fun saveContact() {

        if (isEditable) {

            contact.firstName = getTrimString(mBinding.edtFirstName)
            contact.lastName = getTrimString(mBinding.edtLastName)
            contact.mobileNumber = getTrimString(mBinding.edtMobileNumber)
            contact.email = getTrimString(mBinding.edtEmail)
            contact.categoryId = selectedCategoryId.toString()

            if (filePath.isNotEmpty())
                contact.profilePicturePath = filePath

            contactViewModel.update(contact)
            displayShortToast("Contact Updated")

        } else {

            contact = Contact()

            contact.firstName = getTrimString(mBinding.edtFirstName)
            contact.lastName = getTrimString(mBinding.edtLastName)
            contact.mobileNumber = getTrimString(mBinding.edtMobileNumber)
            contact.email = getTrimString(mBinding.edtEmail)
            contact.categoryId = selectedCategoryId.toString()
            contact.profilePicturePath = filePath

            contactViewModel.insert(contact)
            displayShortToast("Contact Inserted")
        }

        startActivity(Intent(this, ContactListActivity::class.java))
        finish()
    }

    private fun openImagePicker() {

        val itemModel = ItemModel(ItemModel.ITEM_CAMERA)
        val itemModel1 = ItemModel(ItemModel.ITEM_GALLERY)
        val itemList = arrayListOf(itemModel, itemModel1)

        val pickerDialog =
            PickerDialog.Builder(this@AddNewContactActivity)// Activity or Fragment
                .setTitle("Picker")          // String value or resource ID
                //.setTitleTextColor(...) // Color of title text
                .setListType(PickerDialog.TYPE_GRID)       // Type of the picker, must be PickerDialog.TYPE_LIST or PickerDialog.TYPE_Grid
                .setItems(itemList)          // List of ItemModel-s which should be in picker
                .setDialogStyle(PickerDialog.DIALOG_STANDARD)    // PickerDialog.DIALOG_STANDARD (square corners) or PickerDialog.DIALOG_MATERIAL (rounded corners)
                .create()               // Create picker

        pickerDialog.setPickerCloseListener { type, uri ->

            when (type) {

                ItemModel.ITEM_CAMERA -> {

                    Glide.with(this@AddNewContactActivity)
                        .load(uri)
                        .error(R.mipmap.ic_launcher_round)
                        .fallback(R.mipmap.ic_launcher_round)
                        .apply(RequestOptions().circleCrop())
                        .into(mBinding.imgProfile)
                }

                ItemModel.ITEM_GALLERY -> {

                    Glide.with(this@AddNewContactActivity)
                        .load(uri)
                        .error(R.mipmap.ic_launcher_round)
                        .fallback(R.mipmap.ic_launcher_round)
                        .apply(RequestOptions().circleCrop())
                        .into(mBinding.imgProfile)
                }

                ItemModel.ITEM_VIDEO -> {

                }

                ItemModel.ITEM_VIDEO_GALLERY -> {

                }

                ItemModel.ITEM_FILES -> {

                }
            }

            @Suppress("UselessCallOnNotNull")
            if (!uri.toString().isNullOrEmpty()) {

                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(uri, filePathColumn, null, null, null)

                if (cursor!!.moveToFirst()) {

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    filePath = cursor.getString(columnIndex)
                    Log.e("Real Path", filePath)
                }

                cursor.close()
            }
        }

        askPermission = KotlinPermissions.with(this@AddNewContactActivity)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .onAccepted {
                if (it.size == 2) {
                    pickerDialog.show()
                }
            }
            .onDenied {
                askPermission.ask()
            }
            .onForeverDenied {

            }

        askPermission.ask()
    }
}
