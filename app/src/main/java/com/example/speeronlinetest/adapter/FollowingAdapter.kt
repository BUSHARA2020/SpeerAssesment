package com.example.speeronlinetest.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.speeronlinetest.data.Followers
import com.example.speeronlinetest.databinding.ItemFollowingBinding


class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {
    private var followingList: List<Followers> = emptyList()

    private var onItemClick: ((Followers) -> Unit)? = null

    fun setOnItemClickListener(listener: (Followers) -> Unit) {
        onItemClick = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val binding = ItemFollowingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FollowingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(followingList[position])
    }

    override fun getItemCount(): Int {
        return followingList.size
    }

    fun updateFollowingList(followingList: List<Followers>) {
        this.followingList = followingList
        notifyDataSetChanged()
    }

    inner class FollowingViewHolder(private val binding: ItemFollowingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(followers: Followers) {
            binding.followers = followers
            binding.root.setOnClickListener {
                onItemClick?.invoke(followers)
            }
            binding.executePendingBindings()
        }
    }
}



