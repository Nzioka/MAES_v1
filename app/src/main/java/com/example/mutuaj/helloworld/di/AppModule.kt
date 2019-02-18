package com.example.mutuaj.helloworld.di

import android.app.Application
import android.content.Context
import com.example.mutuaj.helloworld.api.MaesService
import com.example.mutuaj.helloworld.repository.CropsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(MaesService.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideMaesService(retrofit: Retrofit): MaesService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideCropsRepository(service: MaesService): CropsRepository {
        return CropsRepository(service)
    }

}