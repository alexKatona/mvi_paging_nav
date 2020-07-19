package sk.alex_katona.sample1.helpers

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import sk.alex_katona.sample1.R

fun View.show(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun loadAvatar(context: Context, url: String, view: ImageView) {
    Glide.with(context)
        .load(url)
        .thumbnail(
            Glide.with(context).load(R.drawable.avatar_placeholder)
                .apply(RequestOptions().circleCrop())
        )
        .transition(DrawableTransitionOptions.withCrossFade(200))
        .apply(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        )
        .circleCrop()
        .into(view)
}