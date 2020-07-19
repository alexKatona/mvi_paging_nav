package sk.alex_katona.sample1.flow.user_list

import androidx.paging.rxjava2.RxPagingSource
import io.reactivex.Single
import sk.alex_katona.sample1.api.convert
import javax.inject.Inject

class UserListPagingSourceFactory @Inject constructor(
    private val userListInteractor: UserListInteractor
) {

    fun createUserListPagingSourceInstance(): RxPagingSource<Int, User> {
        return UserListPagingSource()
    }

    private inner class UserListPagingSource : RxPagingSource<Int, User>() {
        override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, User>> {
            val nextPage = params.key ?: 1
            val nexNextPage = nextPage + 1
            return userListInteractor.getUserList(nextPage)
                .map {
                    LoadResult.Page(
                        it.convert(),
                        null,
                        if (nexNextPage > it.totalPages) null else nexNextPage,
                        LoadResult.Page.COUNT_UNDEFINED,
                        LoadResult.Page.COUNT_UNDEFINED
                    ) as LoadResult<Int, User>
                }.onErrorReturn { error -> LoadResult.Error(error) }
        }
    }
}

