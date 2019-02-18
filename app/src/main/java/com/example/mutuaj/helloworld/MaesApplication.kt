package com.example.mutuaj.helloworld

import android.app.Application
import com.example.mutuaj.helloworld.di.AppComponent
import com.example.mutuaj.helloworld.di.DaggerAppComponent

class MaesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initDagger()

    }


    private fun initDagger() {
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)

    }

    companion object {

        @JvmStatic
        lateinit var appComponent: AppComponent
    }
}