package com.soshdev.gilvus.di

import com.soshdev.gilvus.data.network.GilvusRepository
import com.soshdev.gilvus.data.network.NetworkRepositoryImpl
import com.soshdev.gilvus.util.Constants
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideGilvusApi(get()) }
    single { NetworkRepositoryImpl() }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(Constants.baseUrl).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder().build()
}

fun provideGilvusApi(retrofit: Retrofit): GilvusRepository = retrofit.create(GilvusRepository::class.java)