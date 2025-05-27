package com.procore.playground

import android.app.Application
import com.google.android.play.core.splitcompat.SplitCompat

class PlaygroundApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SplitCompat.install(this)
    }
}