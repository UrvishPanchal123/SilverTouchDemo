package com.silvertouch.demo.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silvertouch.demo.databinding.ItemCategoryBinding
import com.silvertouch.demo.model.Category
import com.silvertouch.demo.ui.activity.MainActivity
import java.util.*

class CategoryAdapter(var mContext: MainActivity) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categoryList: List<Category> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val mBinding = ItemCategoryBinding.inflate(mContext.layoutInflater, parent, false)
        return CategoryViewHolder(mBinding)

        /*val mBinding: ItemCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_category,
            parent,
            false
        )
        return CategoryViewHolder(mBinding)*/
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindData(categoryList[holder.adapterPosition])

        holder.mBinding.imgEdit.setOnClickListener {
            mContext.updateCategory(categoryList[holder.adapterPosition])
        }

        holder.mBinding.imgDelete.setOnClickListener {
            mContext.showDeleteCategoryConfirmationDialog(categoryList[holder.adapterPosition])
        }
    }

    fun setCategory(categoryList: List<Category>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    class CategoryViewHolder(var mBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bindData(category: Category) {
            mBinding.data = category
            mBinding.executePendingBindings()
        }
    }
}