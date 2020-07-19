package sk.alex_katona.sample1.flow.user_detail

import io.reactivex.Single
import sk.alex_katona.sample1.api.UsersApi
import sk.alex_katona.sample1.api.convert
import javax.inject.Inject

interface UserDetailInteractor {
    fun getUserDetail(id: Int): Single<UserDetails>
}

class UserDetailInteractorImpl @Inject constructor(
    private val usersApi: UsersApi
) : UserDetailInteractor {
    override fun getUserDetail(id: Int): Single<UserDetails> {
        return usersApi.getUserDetails(id)
            .map { it.convert() }
    }
}

data class UserDetails(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String
)