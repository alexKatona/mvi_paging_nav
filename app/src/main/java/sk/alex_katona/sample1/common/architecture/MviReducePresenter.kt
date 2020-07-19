package sk.alex_katona.sample1.common.architecture

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class MviReducePresenter<V : MviView<VS>, VS, PS>(private val logging: Boolean = false) : MviBasePresenter<V, VS>() {

    private val consumedSubject = PublishRelay.create<PS>()
    private val tag by lazy { this@MviReducePresenter.javaClass.simpleName }

    abstract fun intents(): List<Observable<out PS>>

    abstract fun initState(): VS

    override fun bindIntents() {
        val mergedObservable = Observable.mergeArray(*intents().toTypedArray(), consumedSubject)
                .observeOn(AndroidSchedulers.mainThread())
        subscribeViewState(mergedObservable.scan(initState(), this::reduceInternal)
                .lift<VS> {
                    object : Observer<VS> {
                        override fun onComplete() = it.onComplete()
                        override fun onSubscribe(d: Disposable) = it.onSubscribe(d)
                        override fun onNext(t: VS) = it.onNext(t)
                        override fun onError(e: Throwable) {
                            //error message from the mosby doesn't contain a stack trace
                            it.onError(IllegalStateException("${tag}: ViewState observable must not reach error state - onError() ", e))
                        }
                    }
                }
                .distinctUntilChanged()) { view, viewState ->
            consumablePartialStates().forEach { consumedSubject.accept(it) }
            renderInternal(view, viewState)
        }
    }

    protected open fun consumablePartialStates(): List<PS> = emptyList()

    override fun <I> intent(binder: ViewIntentBinder<V, I>): Observable<I> {
        return super.intent(binder).doOnNext {
            if (logging) {
                Timber.tag(tag).d("\nINTENT: \n$it")
            }
        }
    }

    private fun renderInternal(view: V, viewState: VS) {
        logState(viewState)
        view.render(viewState)
    }

    open fun logState(viewState: VS) {
        if (logging) {
            Timber.tag(tag).d("\nRENDER_STATE: \n$viewState")
        }
    }

    private fun reduceInternal(previousState: VS, partialState: PS): VS {
        if (logging) {
            Timber.tag(tag).d("\nPREVIOUS_STATE: \n$previousState")
            Timber.tag(tag).d("\nPARTIAL_STATE: \n$partialState")
        }
        return reduce(previousState, partialState)
    }

    abstract fun reduce(previousState: VS, partialState: PS): VS
}

