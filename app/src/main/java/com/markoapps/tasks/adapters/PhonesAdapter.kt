package com.markoapps.tasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.markoapps.tasks.databinding.ItemPhoneinfoBinding
import com.markoapps.tasks.dialogs.PhoneInfo
import com.squareup.picasso.Picasso

class PhonesAdapter(val onClick: ((phoneInfo: PhoneInfo) -> Unit)): ListAdapter<PhoneInfo, PhonesAdapter.ViewHolder>(AppInfoDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemPhoneinfoBinding = ItemPhoneinfoBinding.inflate(inflater, parent, false)
        return ViewHolder(itemPhoneinfoBinding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder(val itemPhoneinfoBinding: ItemPhoneinfoBinding, val onClick: ((phoneInfo: PhoneInfo) -> Unit)): RecyclerView.ViewHolder(itemPhoneinfoBinding.root){

        fun bind(item: PhoneInfo) {
            itemPhoneinfoBinding.apply {
                contentName.text = item.name
                phone.text = item.mobileNumber

                Picasso.get().load(item.photoURI).into(icon)

                root.setOnClickListener {
                    onClick(item)
                }
            }
        }

    }

    class AppInfoDiffUtil: DiffUtil.ItemCallback<PhoneInfo>() {
        override fun areItemsTheSame(oldItem: PhoneInfo, newItem: PhoneInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhoneInfo, newItem: PhoneInfo): Boolean {
            return oldItem == newItem
        }
    }

}