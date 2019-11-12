package com.godinho.desafioimgur.feature.home.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class HomeRepositoryImpl(private val homeRemoteSource: HomeRemoteSource) : HomeRepository {

    override suspend fun getCatList() = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = homeRemoteSource.getCatListAsync()?.await()
            convertResponseIntoVO(response)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun convertResponseIntoVO(response: ResponseObject?): List<String>? {
        val list = mutableListOf<String>()
        response?.data?.forEach{ galleries ->
            galleries.images?.forEach { images ->
                images.link?.let {
                    list.add(it)
                }
            }
        }
        return list
    }
}