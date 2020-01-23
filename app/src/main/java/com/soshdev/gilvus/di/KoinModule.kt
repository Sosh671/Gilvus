package com.soshdev.gilvus.di

import com.soshdev.gilvus.data.DataRepository
import com.soshdev.gilvus.data.MockedDataRepositoryImpl
import com.soshdev.gilvus.ui.chatlist.ChatListViewModel
import org.koin.dsl.module

val appModule = module {

    single<DataRepository> { MockedDataRepositoryImpl() }

    factory { ChatListViewModel(get()) }
}