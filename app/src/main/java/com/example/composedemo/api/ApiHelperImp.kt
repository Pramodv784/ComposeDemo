package com.example.composedemo.api

import com.example.composedemo.model.GitResponse
import retrofit2.Response
import javax.inject.Inject


class ApiHelperImp @Inject constructor(private val webServices: WebServices) : ApiServiceHelper {
    override suspend fun getRepo(input:String,count:Int,page:Int):Response<GitResponse>{
       return webServices.getRepo(input,count,page)
    }

}