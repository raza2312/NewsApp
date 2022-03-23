package com.quokka.newsapp.ui

import NewsRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quokka.newsapp.models.Article
import com.quokka.newsapp.models.NewsResponse
import com.quokka.newsapp.util.GenericResponse
import kotlinx.coroutines.launch
import retrofit2.Response


class NewsViewModel(
    val newsRepository: NewsRepository,
) : ViewModel() {
    val searchNews: MutableLiveData<GenericResponse<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(GenericResponse.PreExecute())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(response?.let { handleSearchNewsResponse(it) })
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): GenericResponse<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                searchNewsResponse = resultResponse
                return GenericResponse.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return GenericResponse.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteSavedNews(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}




