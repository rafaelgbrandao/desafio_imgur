package com.godinho.desafioimgur.di

import com.godinho.desafioimgur.feature.home.repository.HomeRepository
import com.godinho.desafioimgur.feature.home.repository.HomeRepositoryImpl
import com.godinho.desafioimgur.feature.home.presentation.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get()) }

    single { HomeRepositoryImpl() as HomeRepository }
}