package sk.alex_katona.sample1.flow.user_detail

import io.reactivex.Observable
import sk.alex_katona.sample1.common.architecture.MviReducePresenter
import sk.alex_katona.sample1.common.architecture.MviView
import javax.inject.Inject

interface UserDetailView : MviView<UserDetailViewState> {
    fun initIntent(): Observable<Int>
}

data class UserDetailViewState(
    val userData: UserDetails? = null,
    val isError: Boolean = false,
    val isLoading: Boolean = false
)

sealed class UserDetailPartialState {
    object Error : UserDetailPartialState()
    object Loading : UserDetailPartialState()
    data class User(val userDetails: UserDetails) : UserDetailPartialState()
}

class UserDetailPresenter @Inject constructor(
    private val userDetailInteractor: UserDetailInteractor
) : MviReducePresenter<UserDetailView, UserDetailViewState, UserDetailPartialState>() {
    override fun intents(): List<Observable<out UserDetailPartialState>> {

        val initIntent = intent { it.initIntent() }
            .flatMap { userId ->
                userDetailInteractor.getUserDetail(userId)
                    .toObservable()
                    .map { UserDetailPartialState.User(it) as UserDetailPartialState }
                    .onErrorReturn { UserDetailPartialState.Error }
                    .startWith(UserDetailPartialState.Loading)
            }

        return listOf(
            initIntent
        )
    }

    override fun initState(): UserDetailViewState = UserDetailViewState()

    override fun reduce(
        previousState: UserDetailViewState,
        partialState: UserDetailPartialState
    ): UserDetailViewState {
        return when (partialState) {
            UserDetailPartialState.Error -> previousState.copy(isError = true, isLoading = false)
            is UserDetailPartialState.User -> previousState.copy(
                isError = false,
                userData = partialState.userDetails,
                isLoading = false
            )
            UserDetailPartialState.Loading -> previousState.copy(isLoading = true)
        }
    }

}