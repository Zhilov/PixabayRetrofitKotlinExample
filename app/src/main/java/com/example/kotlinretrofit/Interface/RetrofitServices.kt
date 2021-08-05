package com.example.kotlinretrofit.Interface

import com.example.kotlinretrofit.Model.Labels
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitServices {
    @GET("api/?key=22773337-ca3caa766daa279b05a6882d5&q=yellow+flowers&image_type=photo&pretty=true")
    fun getPicturesList(): Call<Labels>
}