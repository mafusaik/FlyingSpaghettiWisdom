package com.glazer.flying.spaghetti.monster.gospel.bible.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glazer.flying.spaghetti.monster.gospel.bible.data.model.GospelChapter
import kotlinx.coroutines.flow.Flow

@Dao
interface GospelDao {
    @Query("SELECT * FROM gospel_chapter ORDER BY id")
    fun getAllChapters(): Flow<List<GospelChapter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<GospelChapter>)
}