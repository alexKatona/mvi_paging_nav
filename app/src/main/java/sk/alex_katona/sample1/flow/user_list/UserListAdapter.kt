package sk.alex_katona.sample1.flow.user_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.user_item_row.view.*
import sk.alex_katona.sample1.R
import sk.alex_katona.sample1.helpers.loadAvatar

class UserViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.user_item_row, parent, false)
) {
    fun bind(user: User) {
        loadAvatar(itemView.context, user.avatarUrl, itemView.iv_user_avatar)
        itemView.tv_user_name.text = "${user.firstName} ${user.lastName}"
        itemView.tv_user_email.text = user.email
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