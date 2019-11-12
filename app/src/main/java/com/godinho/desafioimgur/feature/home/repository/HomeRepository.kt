package com.godinho.desafioimgur.feature.home.repository

interface HomeRepository {

    suspend fun getCatList(): List<String>?
}