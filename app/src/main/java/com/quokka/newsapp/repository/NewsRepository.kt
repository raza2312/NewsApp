import com.quo.newsapp.database.ArticleDatabase
import com.quokka.newsapp.API.RetrofitInstance
import com.quokka.newsapp.models.Article

class NewsRepository(
    val db: ArticleDatabase,
) {

    suspend fun searchNews(searchQery: String, page: Int) =
        RetrofitInstance.api?.searchForNews(searchQery, page)

    suspend fun insert(article: Article) = db.getArticleDao().insert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArtcile(article)


}