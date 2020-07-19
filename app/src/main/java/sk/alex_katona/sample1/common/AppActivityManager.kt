package sk.alex_katona.sample1.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference
import javax.inject.Inject

interface AppActivityManager {
    fun getCurrentActivity(): BaseActivity?
    fun setCurrentActivity(baseActivity: BaseActivity?)
    fun registerActivityLifecycleHelper(application: BaseApplication)
}

class AppActivityManagerImpl @Inject constructor(

) : AppActivityManager {

    private var currentActivity: WeakReference<BaseActivity?>? = null

    override fun getCurrentActivity(): BaseActivity? {
        return currentActivity?.get()
    }

    override fun setCurrentActivity(baseActivity: BaseActivity?) {
        this.currentActivity = WeakReference(baseActivity)
    }

    override fun registerActivityLifecycleHelper(application: BaseApplication) {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStarted(activity: Activity) {
                setCurrentActivity(activity as? BaseActivity?)
            }

            override fun onActivityDestroyed(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                setCurrentActivity(activity as? BaseActivity?)
            }

            override fun onActivityResumed(activity: Activity) {
                setCurrentActivity(activity as? BaseActivity?)
            }

        })
    }
}