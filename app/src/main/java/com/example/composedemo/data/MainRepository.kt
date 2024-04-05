package com.example.composedemo.data

import com.example.composedemo.api.ApiServiceHelper
import com.example.composedemo.db.GitDao
import com.example.composedemo.model.GitResponse
import com.example.composedemo.model.LocalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val apiServiceHelper: ApiServiceHelper,
    private val gitDao: GitDao
) {

    suspend fun addRepo(localData: LocalData) = gitDao.addRepo(localData)


     fun getRepo(): List<LocalData>  = gitDao.getAllRepo()


    suspend fun getItem(page: Int, pageSize: Int, query: String): Result<List<GitResponse.Item>> {
        return try {
            val data = apiServiceHelper.getRepo(query, pageSize, page)
            if (!data.body()?.items.isNullOrEmpty()) Result.success(data.body()?.items!!)
            else Result.success(emptyList())

        } catch (e: Exception) {
            e.printStackTrace()
            Result.success(emptyList())
        }
    }


}