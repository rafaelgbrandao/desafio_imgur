package com.godinho.desafioimgur.feature.home.repository

interface HomeRepository {

    suspend fun getContentList(): List<String>?
}