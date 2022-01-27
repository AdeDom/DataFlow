package com.adedom.dataflow

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<SharedPref> { SharedPrefImpl(get()) }

    single<DefaultRepository> { DefaultRepositoryImpl(get()) }

    viewModel { SharedPreferencesV2ViewModel(get()) }

}