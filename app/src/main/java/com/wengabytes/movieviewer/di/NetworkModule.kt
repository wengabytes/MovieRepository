package com.wengabytes.movieviewer.di

import android.content.Context
import com.wengabytes.movieviewer.services.MovieAPI
import com.wengabytes.movieviewer.utils.network.ConnectivityInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideBaseUrl() }
    single { provideConnectivityInterceptor(get()) }
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get(), get()) }
    single { provideRetrofit(get(), get()) }
    single { provideBaseAPI(get()) }
}

fun provideBaseUrl(): String =
        "http://ec2-52-76-75-52.ap-southeast-1.compute.amazonaws.com/"

fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

fun provideConnectivityInterceptor(context: Context): ConnectivityInterceptor =
        ConnectivityInterceptor(context)

fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                        connectivityInterceptor: ConnectivityInterceptor): OkHttpClient =
        OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(connectivityInterceptor)
                .readTimeout(1000 * 60, TimeUnit.MILLISECONDS)
                .connectTimeout(1000 * 60, TimeUnit.MILLISECONDS)
                .callTimeout(1000 * 60, TimeUnit.MILLISECONDS)
                .build()

fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

fun provideBaseAPI(retrofit: Retrofit): MovieAPI =
        retrofit.create(MovieAPI::class.java)