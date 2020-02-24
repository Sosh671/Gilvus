package com.soshdev.gilvus.di

import com.soshdev.gilvus.data.network.NetworkRepositoryImpl
import org.koin.dsl.module

val networkModule = module {
    single { NetworkRepositoryImpl() }
}