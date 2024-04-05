package com.example.composedemo.api

import android.content.Context
import androidx.room.Room
import com.example.composedemo.db.GitDao
import com.example.composedemo.db.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn( SingletonComponent::class)
object NetworkModule {



    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(25, TimeUnit.SECONDS) // connect timeout
            .readTimeout(25, TimeUnit.SECONDS)
            .build()
    }



    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.github.com/search/")
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit):WebServices{
        return retrofit.create(WebServices::class.java)
    }

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImp): ApiServiceHelper = apiHelper



    //DB
    @Singleton
    @Provides
    fun provideUserDao(localDatabase: LocalDatabase):GitDao{
        return localDatabase.gitDao()
    }


   @Singleton
   @Provides
   fun provideAppDatabase(@ApplicationContext context: Context):
           LocalDatabase{
       return Room.databaseBuilder(context,LocalDatabase::class.java,"git_data").allowMainThreadQueries().build()
   }


}