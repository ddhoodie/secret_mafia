package com.secretmafia

import android.app.Application
import com.secretmafia.data.SettingsStore

class MafiaApp : Application() {
    lateinit var settingsStore: SettingsStore
        private set

    override fun onCreate() {
        super.onCreate()
        settingsStore = SettingsStore(this)
    }
}
