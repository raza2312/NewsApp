package com.quokka.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.quokka.newsapp.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArtcile(article: Article)
}