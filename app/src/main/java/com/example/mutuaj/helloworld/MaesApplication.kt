package com.example.mutuaj.helloworld

import android.app.Application
import com.example.mutuaj.helloworld.di.AppComponent
import com.example.mutuaj.helloworld.di.DaggerAppComponent

class MaesApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()

    }


    private fun initDagger() {
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)

    }
}