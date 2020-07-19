package sk.alex_katona.sample1.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    @Inject
    lateinit var appActivityManager: AppActivityManager

    override fun onCreate() {
        super.onCreate()
        appActivityManager.registerActivityLifecycleHelper(this)
        Timber.plant(Timber.DebugTree())
    }
}