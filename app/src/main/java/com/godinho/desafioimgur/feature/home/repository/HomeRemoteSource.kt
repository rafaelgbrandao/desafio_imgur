package com.godinho.desafioimgur.feature.home.repository

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface HomeRemoteSource {

    @GET("gallery/search/?q=cats")
    fun getCatListAsync(): Deferred<ResponseObject>?
}