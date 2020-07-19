package sk.alex_katona.sample1.api

import sk.alex_katona.sample1.api.models.user_details.UserDetail
import sk.alex_katona.sample1.api.models.user_list.UserList
import sk.alex_katona.sample1.flow.user_detail.UserDetails
import sk.alex_katona.sample1.flow.user_list.User

fun UserList.convert(): List<User> {
    return this.data.map {
        User(
            it.firstName,
            it.lastName,
            it.avatar,
            it.email,
            it.id
        )
    }
}

fun UserDetail.convert(): UserDetails{
    return UserDetails(
        data.id,
        data.email,
        data.firstName,
        data.lastName,
        data.avatar
    )
}