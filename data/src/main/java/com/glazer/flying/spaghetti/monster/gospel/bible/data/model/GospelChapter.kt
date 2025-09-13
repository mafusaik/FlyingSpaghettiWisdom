package com.glazer.flying.spaghetti.monster.gospel.bible.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gospel_chapter")
data class GospelChapter(
    @PrimaryKey val id: Int,
    val title: String,
    val content: String
)