package sk.alex_katona.sample1.common.architecture

import com.hannesdorfmann.mosby3.mvp.MvpView

interface MviView<VS> : MvpView {
    fun render(viewState: VS)
}
