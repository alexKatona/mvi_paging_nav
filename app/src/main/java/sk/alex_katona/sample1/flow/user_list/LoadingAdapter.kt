package sk.alex_katona.sample1.flow.user_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_list_loading_item.view.*
import sk.alex_katona.sample1.R

class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.user_list_loading_item, parent, false)
) {
    fun bind(loadState: LoadState) {
        itemView.fl_loading_container.visibility =   if(loadState is LoadState.Loading) View.VISIBLE else View.GONE
    }
}

class LoadingAdapter : LoadStateAdapter<LoadingViewHolder>() {
    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder {
        return LoadingViewHolder(parent)
    }
}