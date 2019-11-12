package com.godinho.desafioimgur.feature.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.godinho.desafioimgur.feature.home.repository.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository): ViewModel() {

    private val job = SupervisorJob()

    fun fetchListWithData(): LiveData<List<String>> = lvCatList
    private val lvCatList = MutableLiveData<List<String>>()

    fun showNoContentDialog(): LiveData<Unit> = lvEmptyContentList
    private val lvEmptyContentList = MutableLiveData<Unit>()

    fun showErrorDialog(): LiveData<Unit> = lvErrorOnRequest
    private val lvErrorOnRequest = MutableLiveData<Unit>()

    fun onRequestCatList() {
        CoroutineScope(Dispatchers.IO + job).launch {
            homeRepository.getCatList()?.let {
                when {
                    it.isEmpty() -> lvEmptyContentList.postValue(Unit)
                    else -> lvCatList.postValue(it)
                }
            } ?: lvErrorOnRequest.postValue(Unit)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}