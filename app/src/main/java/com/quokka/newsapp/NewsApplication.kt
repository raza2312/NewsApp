package com.quokka.newsapp

import android.app.Application
import com.quokka.newsapp.API.RetrofitInstance
import com.quokka.newsapp.util.Constants.Companion.Api_Key

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitInstance.initialize(Api_Key)
    }
}