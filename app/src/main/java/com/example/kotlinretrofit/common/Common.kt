package com.example.kotlinretrofit.common

import com.example.kotlinretrofit.retrofitInterface.RetrofitServices
import com.example.kotlinretrofit.retrofit.RetrofitClient

object Common {
    private const val BASE_URL = "https://pixabay.com/"
    val retrofitService: RetrofitServices get() =
        RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}