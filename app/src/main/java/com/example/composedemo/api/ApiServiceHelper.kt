package com.example.composedemo.api

import com.example.composedemo.model.GitResponse
import retrofit2.Response

interface ApiServiceHelper {
    suspend fun getRepo(input:String,count:Int,page:Int):Response<GitResponse>
}