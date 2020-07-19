package sk.alex_katona.sample1.flow.user_list

import io.reactivex.Single
import sk.alex_katona.sample1.api.UsersApi
import sk.alex_katona.sample1.api.models.user_list.UserList
import javax.inject.Inject

interface UserListInteractor {
    fun getUserList(page: Int): Single<UserList>
}

class UserListInteractorImpl @Inject constructor(
    private val usersApi: UsersApi
) : UserListInteractor {

    companion object {
        private const val PAGE_SIZE = 5
    }

    override fun getUserList(page: Int): Single<UserList> {
        return usersApi.getUserList(page, PAGE_SIZE)
    }

}

data class User(
    val firstName: String,
    val lastName: String,
    val avatarUrl: String,
    val email: String,
    val id: Int
)


