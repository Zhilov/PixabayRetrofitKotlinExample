package com.example.kotlinretrofit.retrofitInterface

import com.example.kotlinretrofit.model.Labels
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {
    @GET("api/?key=22773337-ca3caa766daa279b05a6882d5")
    fun getPicturesList(@Query("q") search: String, @Query("page") page: Int): Call<Labels>
}