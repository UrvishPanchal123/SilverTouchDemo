package com.silvertouch.demo.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.silvertouch.demo.R
import com.silvertouch.demo.databinding.ActivityMainBinding
import com.silvertouch.demo.model.Category
import com.silvertouch.demo.ui.BaseActivity
import com.silvertouch.demo.ui.adapter.CategoryAdapter
import com.silvertouch.demo.viewmodel.CategoryViewModel
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val categoryViewModel: CategoryViewModel by inject()

    private lateinit var categoryAdapter: CategoryAdapter

    private var isEditable = false

    private var category = Category()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_main)

        init()
    }

    private fun init() {

        setSupportActionBar(mBinding.toolbar)
        mBinding.txtHeaderTitle.text = getString(R.string.app_name)

        setUpNavigationView()

        categoryAdapter = CategoryAdapter(this@MainActivity)
        mBinding.recyclerViewCategory.adapter = categoryAdapter

        getCategoryList()

        mBinding.btnSubmit.setOnClickListener {
            saveCategory()
        }
    }

    private fun setUpNavigationView() {

        mBinding.imgMenu.setOnClickListener {
            drawerToggle()
        }

        mBinding.navView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {

                R.id.menu_add_category -> {
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }

                R.id.menu_add_contact -> {
                    val i = Intent(this, AddNewContactActivity::class.java)
                    startActivity(i)
                }

                R.id.menu_contact_list -> {
                    val i = Intent(this, ContactListActivity::class.java)
                    startActivity(i)
                }
            }

            drawerToggle()
            true
        }
    }

    private fun drawerToggle() {

        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawers()
        } else {
            mBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {

        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerToggle()
        } else
            super.onBackPressed()
    }

    private fun getCategoryList() {

        categoryViewModel.getAllCategory().observe(this,
            Observer { list ->
                list?.let {
                    categoryAdapter.setCategory(it)
                }
            })
    }

    private fun saveCategory() {

        if (getTrimString(mBinding.edtCategory).isNotEmpty()) {

            if (isEditable) {

                category.categoryName = getTrimString(mBinding.edtCategory)
                categoryViewModel.update(category)
                displayShortToast("Category Updated")
                isEditable = false
                mBinding.btnSubmit.text = getString(R.string.title_submit)

            } else {

                category = Category()
                category.categoryName = getTrimString(mBinding.edtCategory)
                categoryViewModel.insert(category)
                displayShortToast("Category Inserted")
            }

            mBinding.edtCategory.text = null

        } else {
            displayShortToast("Enter Category Name")
        }
    }

    fun updateCategory(category: Category) {

        isEditable = true
        mBinding.edtCategory.setText(category.categoryName)
        mBinding.edtCategory.text?.length?.let { mBinding.edtCategory.setSelection(it) }
        mBinding.btnSubmit.text = getString(R.string.title_update)

        this.category = category
    }

    fun showDeleteCategoryConfirmationDialog(category: Category) {

        val alertDialog = AlertDialog.Builder(this)

        alertDialog.setTitle(getString(R.string.title_dialog_confirmation))
            .setMessage(getString(R.string.title_dialog_msg_category_delete))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.title_proceed)) { _, _ ->
                deleteCategory(category)
            }
            .setNegativeButton(getString(R.string.title_cancel)) { dialog, _ ->
                dialog.dismiss()
            }

        alertDialog.create().show()
    }

    private fun deleteCategory(category: Category) {
        categoryViewModel.delete(category)
        displayShortToast("Category Deleted")
    }
}
