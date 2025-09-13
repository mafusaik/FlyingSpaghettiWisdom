package com.glazer.flying.spaghetti.monster.gospel.bible.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.glazer.flying.spaghetti.monster.gospel.bible.data.model.GospelChapter

@Database(entities = [GospelChapter::class], version = 1, exportSchema = false)
abstract class GospelDatabase : RoomDatabase() {
    abstract fun gospelDao(): GospelDao
}