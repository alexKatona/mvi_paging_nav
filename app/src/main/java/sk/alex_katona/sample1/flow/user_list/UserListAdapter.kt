package sk.alex_katona.sample1.flow.user_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.user_item_row.view.*
import sk.alex_katona.sample1.R

class UserViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.user_item_row, parent, false)
) {
    fun bind(user: User) {
        itemView.tv_user_name.text = "${user.firstName} ${user.lastName}"
        itemView.tv_user_email.text = user.email
        Glide.with(itemView.context)
            .load(user.avatarUrl)
            .thumbnail(
                Glide.with(itemView.context).load(R.drawable.avatar_placeholder)
                    .apply(RequestOptions().circleCrop())
            )
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            )
            .circleCrop()
            .into(itemView.iv_user_avatar)
    }
}

class UserAdapter(val onClickSubject: Subject<User>) : PagingDataAdapter<User, UserViewHolder>(
    UserComparator
) {
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { user ->
            holder.bind(user)
            holder.itemView.setOnClickListener { onClickSubject.onNext(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(parent)
    }
}

object UserComparator : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}