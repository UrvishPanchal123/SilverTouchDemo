package com.silvertouch.demo.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.silvertouch.demo.R
import org.jetbrains.annotations.NotNull

@Entity(indices = [Index(value = arrayOf("id"), unique = true)])
class Contact {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var firstName: String? = null

    var lastName: String? = null

    var mobileNumber: String? = null

    var email: String? = null

    var categoryId: String? = null

    var profilePicturePath: String? = null

    companion object {

        @JvmStatic
        @BindingAdapter("bind:imageUrl")
        fun ImageView.loadImage(url: String?) {
            Glide.with(context)
                .load(url)
                .error(R.mipmap.ic_launcher_round)
                .fallback(R.mipmap.ic_launcher_round)
                .apply(RequestOptions().circleCrop())
                .into(this)
        }
    }
}

