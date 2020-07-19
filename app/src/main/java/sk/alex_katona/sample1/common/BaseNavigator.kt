package sk.alex_katona.sample1.common

interface BaseNavigator<T> {
    fun navigateFrom(screen: T)
}