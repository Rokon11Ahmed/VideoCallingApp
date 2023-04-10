package com.example.videocallingapp.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videocallingapp.R
import com.example.videocallingapp.data.model.User
import com.example.videocallingapp.databinding.ItemUserSingleLayoutBinding
import com.example.videocallingapp.extention.loadImage
import com.example.videocallingapp.extention.milliSecondToTime

class UserListAdapter(val onItemClicked: ((Int, User) -> Unit)? = null,)
    : RecyclerView.Adapter<UserListAdapter.ViewHolder>(){

    private var list: MutableList<User> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = ItemUserSingleLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item,position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<User>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ItemUserSingleLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
        fun bind(item: User, position: Int) {
            binding.apply {
                nameTextView.text = item.username
                userImageView.loadImage(item.profilePictureUrl)
                recentLoginTextView.text = "${recentLoginTextView.context.getText(R.string.IDS_LAST_LOGIN)}: ${milliSecondToTime(item.lastLoginTime)}"

                binding.liveButton.setOnClickListener {
                    onItemClicked?.invoke(position, item)
                }
            }

            if (item.onLive){
                binding.liveButton.background = binding.liveButton.context.getDrawable(R.drawable.ic_live_button_bg_active)
                binding.liveButton.isEnabled = true
                binding.liveButton.setTextColor(binding.liveButton.context.getColor(R.color.white))
            }else {
                binding.liveButton.background = binding.liveButton.context.getDrawable(R.drawable.ic_live_button_bg_inactive)
                binding.liveButton.isEnabled = false
                binding.liveButton.setTextColor(binding.liveButton.context.getColor(R.color.black))
            }
        }
    }
}