package com.example.kotlinretrofit.Interface

import com.example.kotlinretrofit.Model.Labels
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {
    @GET("api/?key=22773337-ca3caa766daa279b05a6882d5&image_type=photo&orientation=horizontal")
    fun getPicturesList(@Query("q") search: String, @Query("page") page: Int): Call<Labels>
}