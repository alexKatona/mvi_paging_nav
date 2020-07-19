package sk.alex_katona.sample1.helpers

import android.view.View

fun View.show(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}