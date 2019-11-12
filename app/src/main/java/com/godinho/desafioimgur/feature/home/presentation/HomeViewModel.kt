package com.godinho.desafioimgur.feature.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.godinho.desafioimgur.feature.home.repository.HomeRepository
import kotlinx.coroutines.*

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val dispatcher: CoroutineDispatcher,
    private val job: Job): ViewModel() {

    fun fetchListWithData(): LiveData<List<String>> = lvCatList
    private val lvCatList = MutableLiveData<List<String>>()

    fun showNoContentDialog(): LiveData<Unit> = lvEmptyContentList
    private val lvEmptyContentList = MutableLiveData<Unit>()

    fun showErrorDialog(): LiveData<Unit> = lvErrorOnRequest
    private val lvErrorOnRequest = MutableLiveData<Unit>()

    fun showLoadingBar(): LiveData<Boolean> = lvShowLoadingBar
    private val lvShowLoadingBar = MutableLiveData<Boolean>()

    fun onRequestContentList() {
        CoroutineScope(dispatcher + job).launch {
            lvShowLoadingBar.postValue(true)
            homeRepository.getContentList()?.let {
                when {
                    it.isEmpty() -> lvEmptyContentList.postValue(Unit)
                    else -> lvCatList.postValue(it)
                }
            } ?: lvErrorOnRequest.postValue(Unit)
            lvShowLoadingBar.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}