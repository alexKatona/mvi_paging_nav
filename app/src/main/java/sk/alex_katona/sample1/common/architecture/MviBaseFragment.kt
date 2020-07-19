package sk.alex_katona.sample1.common.architecture

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.hannesdorfmann.mosby3.MviDelegateCallback
import com.hannesdorfmann.mosby3.mvi.MviPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import sk.alex_katona.sample1.common.BaseFragment
import timber.log.Timber

/**
 * Using custom fragment and delegates since the MVI fragment from mosby is not using androidX yet
 *
 * @param V
 * @param M
 * @param P
 */
abstract class MviBaseFragment<V : MvpView, M, P : MviPresenter<V, M>> : BaseFragment(), MvpView,
    MviDelegateCallback<V, P>,
    MviView<M> {

    private var isRestoringViewState = false

    @Volatile
    private var mvpDelegateField: FragmentMviDelegate<V, P>? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMvpDelegate().onCreate(savedInstanceState)
    }

    @CallSuper
    override fun onDestroy() {
        getMvpDelegate().onDestroy()
        super.onDestroy()
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getMvpDelegate().onSaveInstanceState(outState)
    }

    @CallSuper
    override fun onPause() {
        getMvpDelegate().onPause()
        super.onPause()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        getMvpDelegate().onResume()
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        getMvpDelegate().onStart()
    }

    @CallSuper
    override fun onStop() {
        getMvpDelegate().onStop()
        super.onStop()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMvpDelegate().onViewCreated(view, savedInstanceState)
    }

    @CallSuper
    override fun onDestroyView() {
        getMvpDelegate().onDestroyView()
        super.onDestroyView()
    }

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMvpDelegate().onActivityCreated(savedInstanceState)
    }

    @CallSuper
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        getMvpDelegate().onAttach(activity)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getMvpDelegate().onAttach(context)
    }

    @CallSuper
    override fun onDetach() {
        getMvpDelegate().onDetach()
        super.onDetach()
    }

    @SuppressLint("NewApi")
    @CallSuper
    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        getMvpDelegate().onAttachFragment(childFragment)
    }

    @CallSuper
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        getMvpDelegate().onConfigurationChanged(newConfig)
    }

    abstract override fun createPresenter(): P

    @Suppress("UNCHECKED_CAST")
    override fun getMvpView(): V {
        try {
            return this as V
        } catch (e: ClassCastException) {
            val msg =
                "Couldn't cast the View to the corresponding View interface. Most likely you forgot to add \"Activity implements YourMvpViewInterface\".\""
            Timber.e(this.toString(), msg)
            throw RuntimeException(msg, e)
        }

    }

    @Synchronized
    private fun getMvpDelegate(): FragmentMviDelegate<V, P> {
        if (mvpDelegateField == null) {
            mvpDelegateField =
                FragmentMviDelegateImpl(
                    this,
                    this
                )
        }
        return mvpDelegateField!!
    }

    override fun setRestoringViewState(restoringViewState: Boolean) {
        this.isRestoringViewState = restoringViewState
    }

    protected fun isRestoringViewState(): Boolean {
        return isRestoringViewState
    }

    fun restart(bundle: Bundle? = null) {
        bundle?.let { arguments = it }
        activity?.supportFragmentManager?.let {
            if (!it.isStateSaved) {
                getMvpDelegate().recreatePresenter()
                it.beginTransaction().detach(this).attach(this).commitAllowingStateLoss()
            }
        }
    }
}
