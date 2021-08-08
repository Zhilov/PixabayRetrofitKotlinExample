package com.example.kotlinretrofit.Common

import com.example.kotlinretrofit.Interface.RetrofitServices
import com.example.kotlinretrofit.Retrofit.RetrofitClient

object Common {
    private const val BASE_URL = "https://pixabay.com/"
    val retrofitService: RetrofitServices get() =
        RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}