package com.silvertouch.demo.repository

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.silvertouch.demo.database.dao.CategoryDao
import com.silvertouch.demo.model.Category

class CategoryRepository(private var categoryDao: CategoryDao) {

    private val categoryList: LiveData<List<Category>> = categoryDao.getCategory()

    fun getAllCategory(): LiveData<List<Category>> {
        return categoryList
    }

    fun insert(category: Category) {
        InsertCategoryAsyncTask(
            categoryDao
        ).execute(category)
    }

    private class InsertCategoryAsyncTask(val categoryDao: CategoryDao) :
        AsyncTask<Category, Unit, Unit>() {

        override fun doInBackground(vararg category: Category?) {
            categoryDao.insertCategory(category[0]!!)
        }
    }

    fun update(category: Category) {
        UpdateCategoryAsyncTask(
            categoryDao
        ).execute(category)
    }

    private class UpdateCategoryAsyncTask(val categoryDao: CategoryDao) :
        AsyncTask<Category, Unit, Unit>() {

        override fun doInBackground(vararg category: Category?) {
            categoryDao.updateCategory(category[0]!!)
        }
    }

    fun delete(category: Category) {
        DeleteCategoryAsyncTask(
            categoryDao
        ).execute(category)
    }

    private class DeleteCategoryAsyncTask(val categoryDao: CategoryDao) :
        AsyncTask<Category, Unit, Unit>() {

        override fun doInBackground(vararg category: Category?) {
            categoryDao.deleteCategory(category[0]!!)
        }
    }
}