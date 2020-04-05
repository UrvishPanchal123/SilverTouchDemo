package com.silvertouch.demo.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(indices = [Index(value = arrayOf("id"), unique = true)])
class Category {

    @NotNull
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var categoryName: String? = null
}

