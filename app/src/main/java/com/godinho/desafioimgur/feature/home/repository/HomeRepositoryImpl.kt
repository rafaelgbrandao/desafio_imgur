package com.godinho.desafioimgur.feature.home.repository

import com.godinho.desafioimgur.feature.home.repository.remote.HomeRemoteSource
import com.godinho.desafioimgur.feature.home.repository.response.ResponseObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class HomeRepositoryImpl(private val homeRemoteSource: HomeRemoteSource) : HomeRepository {

    override suspend fun getContentList() = withContext(Dispatchers.IO) {
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
            galleries.images
                ?.filter { it.type == IMG_MINE_TYPE }
                ?.forEach { images ->
                    images.link?.let {
                    list.add(it)
                }
            }
        }
        return list
    }

    companion object{
        internal const val IMG_MINE_TYPE = "image/jpeg"
    }
}