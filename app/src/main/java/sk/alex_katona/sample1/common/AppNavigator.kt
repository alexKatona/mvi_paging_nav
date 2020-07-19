package sk.alex_katona.sample1.common

import androidx.navigation.NavController
import androidx.navigation.findNavController
import sk.alex_katona.sample1.R
import sk.alex_katona.sample1.flow.user_list.UserListFragmentDirections

import javax.inject.Inject

sealed class AppScreens {
    data class List(val moveNextWithUserId: Int) : AppScreens()
    object Detail : AppScreens()
}

interface AppNavigator : BaseNavigator<AppScreens>

class AppNavigatorImpl @Inject constructor(
    private val appActivityManager: AppActivityManager
) : AppNavigator {

    override fun navigateFrom(screen: AppScreens) {
        when (screen) {
            is AppScreens.List -> getNavController()?.navigate(UserListFragmentDirections.actionFirstFragmentToSecondFragment(screen.moveNextWithUserId))
            AppScreens.Detail -> getNavController()?.navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun getNavController(): NavController? {
        return appActivityManager.getCurrentActivity()?.findNavController(R.id.nav_host_fragment)
    }
}