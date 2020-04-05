package com.silvertouch.demo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.silvertouch.demo.model.Category

@Dao
interface CategoryDao {

    @Query("Select * from Category")
    fun getCategory(): LiveData<List<Category>>

    @Query("Select * from Category")
    fun getAllCategory(): List<Category>

    @Query("SELECT Category.categoryName FROM Category WHERE id = :categoryId")
    fun getCategoryNameById(categoryId: Int): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Update
    fun updateCategory(category: Category)
}
