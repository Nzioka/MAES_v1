package com.example.mutuaj.helloworld.di

import android.app.Application
import com.example.mutuaj.helloworld.MaesApplication
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(maesApplication: MaesApplication)

}