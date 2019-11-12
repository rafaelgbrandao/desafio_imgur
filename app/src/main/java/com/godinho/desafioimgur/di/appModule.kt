package com.godinho.desafioimgur.di

import com.godinho.desafioimgur.BuildConfig
import com.godinho.desafioimgur.factory.ServiceFactory
import com.godinho.desafioimgur.feature.home.repository.HomeRepository
import com.godinho.desafioimgur.feature.home.repository.HomeRepositoryImpl
import com.godinho.desafioimgur.feature.home.presentation.HomeViewModel
import com.godinho.desafioimgur.feature.home.repository.HomeRemoteSource
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val OKHTTP_CLIENT = "OkHttpClient"
private const val OKHTTP_INTERCEPTOR = "OkHttpInterceptor"
private const val HEADER_KEY_AUTHORIZATION = "Authorization"

val appModule = module {

    viewModel { HomeViewModel(get()) }

    single { HomeRepositoryImpl(get()) as HomeRepository }

    single { ServiceFactory.createService(get(), HomeRemoteSource::class.java) }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.IMGUR_URL)
            .client(get(named(OKHTTP_CLIENT)))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    single(named(OKHTTP_CLIENT)) {
        OkHttpClient
            .Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(get(named(OKHTTP_INTERCEPTOR)))
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    single(named(OKHTTP_INTERCEPTOR)) {
        Interceptor { chain ->
            val requestOriginal = chain.request()
            val newRequest = requestOriginal.newBuilder()
                .header(HEADER_KEY_AUTHORIZATION, BuildConfig.IMGUR_CLIENT_ID)
                .url(requestOriginal.url().newBuilder().build())

            return@Interceptor chain.proceed(newRequest.build())
        }
    }
}