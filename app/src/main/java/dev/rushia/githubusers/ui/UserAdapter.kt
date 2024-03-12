package dev.rushia.githubusers.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.rushia.githubusers.data.response.UserResultItem
import dev.rushia.githubusers.databinding.ItemUserBinding

class UserAdapter : ListAdapter<UserResultItem, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {
    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserResultItem) {
            binding.tvUser.text = user.login
            Glide.with(itemView.context).load(user.avatarUrl).into(binding.ivAvatar)
            binding.cvUsername.setOnClickListener {
                val intent = Intent(itemView.context, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USER, user.login)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserResultItem>() {
            override fun areItemsTheSame(
                oldItem: UserResultItem, newItem: UserResultItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserResultItem, newItem: UserResultItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        return holder.bind(user)
    }
}