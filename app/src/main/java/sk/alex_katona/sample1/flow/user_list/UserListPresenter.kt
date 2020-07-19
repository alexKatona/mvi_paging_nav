package sk.alex_katona.sample1.flow.user_list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.observable
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import sk.alex_katona.sample1.common.architecture.MviReducePresenter
import sk.alex_katona.sample1.common.architecture.MviView
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

interface UserListView :
    MviView<UserListViewState> {
    fun initIntent(): Observable<Unit>
    fun userClickIntent(): Observable<User>
}

data class UserListViewState(
    val data: PagingData<User> = PagingData.empty(),
    val isError: Boolean = false,
    val moveNextWithUserId: Int? = null
)

sealed class UserListPartialState {
    data class UserList(val data: PagingData<User>) : UserListPartialState()
    object Error : UserListPartialState()
    data class MoveNext(val moveNextWithUserId: Int?) : UserListPartialState()
}

class UserListPresenter @Inject constructor(
    private val userListPagingSourceFactory: UserListPagingSourceFactory
) : MviReducePresenter<UserListView, UserListViewState, UserListPartialState>() {
    override fun intents(): List<Observable<out UserListPartialState>> {

        val pager = Pager(
            PagingConfig(5, prefetchDistance = 1),
            null,
            null,
            { userListPagingSourceFactory.createUserListPagingSourceInstance() }
        )

        val initIntent = intent { it.initIntent() }
            .flatMap {
                pager.observable
                    .cachedIn(CoroutineScope(EmptyCoroutineContext)) // this is currently the limitation of the paging library
                    .map {
                        UserListPartialState.UserList(
                            it
                        ) as UserListPartialState
                    }.onErrorReturn { UserListPartialState.Error }
            }.onErrorReturn { UserListPartialState.Error }

        val userClickIntent = intent { it.userClickIntent() }
            .flatMap {
                Observable.just(
                    UserListPartialState.MoveNext(it.id),
                    UserListPartialState.MoveNext(null)
                )
            }

        return listOf(
            initIntent,
            userClickIntent
        )
    }

    override fun initState(): UserListViewState =
        UserListViewState()

    override fun reduce(
        previousState: UserListViewState,
        partialState: UserListPartialState
    ): UserListViewState {
        return when (partialState) {
            is UserListPartialState.UserList -> previousState.copy(
                data = partialState.data,
                isError = false
            )
            UserListPartialState.Error -> previousState.copy(isError = true)
            is UserListPartialState.MoveNext -> previousState.copy(moveNextWithUserId = partialState.moveNextWithUserId)
        }
    }

}