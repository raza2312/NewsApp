package com.quokka.newsapp.activities

import NewsRepository
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oguzhanturkmen.newsapp.ui.NewsViewModelProviderFactory
import com.quo.newsapp.database.ArticleDatabase
import com.quokka.newsapp.R
import com.quokka.newsapp.adapters.NewsAdapter
import com.quokka.newsapp.models.NewsResponse
import com.quokka.newsapp.ui.NewsViewModel
import com.quokka.newsapp.util.GenericResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val newsRepository = NewsRepository(ArticleDatabase.invoke(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        setupRecylerView()
        viewModel.getSavedNews().observe(this, Observer { articles ->
            newsAdapter.listDiffer.submitList(articles)
        })
        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(5000)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(this, Observer { response ->
            when (response) {
                is GenericResponse.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        for (i in 0 until newsAdapter.listDiffer.currentList.size - 1) {
                            viewModel.deleteSavedNews(newsAdapter.listDiffer.currentList[i])
                        }
                        newsAdapter.listDiffer.submitList(newsResponse.articles)
                        for (i in 0 until newsResponse.articles.size - 1) {
                            viewModel.saveArticle(newsResponse.articles[i])
                        }
                    }

                }
                is GenericResponse.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                    }
                }
                is GenericResponse.PreExecute<NewsResponse> -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }


    private fun setupRecylerView() {
        newsAdapter = NewsAdapter()
        recycler_view.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }


}