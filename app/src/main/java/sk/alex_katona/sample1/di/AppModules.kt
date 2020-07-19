package sk.alex_katona.sample1.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import sk.alex_katona.sample1.api.UsersApi
import sk.alex_katona.sample1.common.AppActivityManager
import sk.alex_katona.sample1.common.AppActivityManagerImpl
import sk.alex_katona.sample1.common.AppNavigator
import sk.alex_katona.sample1.common.AppNavigatorImpl
import sk.alex_katona.sample1.flow.user_detail.UserDetailInteractor
import sk.alex_katona.sample1.flow.user_detail.UserDetailInteractorImpl
import sk.alex_katona.sample1.flow.user_list.UserListInteractor
import sk.alex_katona.sample1.flow.user_list.UserListInteractorImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModules {

    @Singleton
    @Binds
    abstract fun bindAppActivityManager(appActivityManagerImpl: AppActivityManagerImpl): AppActivityManager

    @Singleton
    @Binds
    abstract fun bindAppNavigator(appNavigatorImpl: AppNavigatorImpl): AppNavigator

    @Binds
    abstract fun bindUserListInteractor(userListInteractorImpl: UserListInteractorImpl): UserListInteractor

    @Binds
    abstract fun bindUserDetailsInteractor(userDetailInteractorImpl: UserDetailInteractorImpl): UserDetailInteractor
}

@Module
@InstallIn(ApplicationComponent::class)
class ApiModule{

    @Provides
    fun bindUserListApi(): UsersApi = UsersApi.newInstance()

}