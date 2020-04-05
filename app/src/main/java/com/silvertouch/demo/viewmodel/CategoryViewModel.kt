package com.silvertouch.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.silvertouch.demo.model.Category
import com.silvertouch.demo.repository.CategoryRepository

class CategoryViewModel(private var categoryRepository: CategoryRepository) : ViewModel() {

    private var categoryList: LiveData<List<Category>> = categoryRepository.getAllCategory()

    fun getAllCategory(): LiveData<List<Category>> {
        return categoryList
    }

    fun insert(category: Category) {
        categoryRepository.insert(category)
    }

    fun update(category: Category) {
        categoryRepository.update(category)
    }

    fun delete(category: Category) {
        categoryRepository.delete(category)
    }
}