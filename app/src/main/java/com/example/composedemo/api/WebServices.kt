package com.example.composedemo.api

import com.example.composedemo.model.GitResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    @GET("repositories")
    suspend fun getRepo(@Query("q") input:String,@Query("per_page") count:Int,@Query("page")page:Int):Response<GitResponse>

}