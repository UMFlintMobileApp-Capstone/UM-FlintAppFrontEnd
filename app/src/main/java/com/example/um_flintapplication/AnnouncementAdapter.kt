package com.example.um_flintapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.um_flintapplication.databinding.AnnouncementItemBinding
import com.example.um_flintapplication.Announcement

class AnnouncementAdapter :
    ListAdapter<Announcement, AnnouncementAdapter.AnnouncementViewHolder>(AnnouncementDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val binding = AnnouncementItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val announcement = getItem(position)
        holder.bind(announcement)
    }

    class AnnouncementViewHolder(private val binding: AnnouncementItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(announcement: Announcement) {
            binding.announcementTitle.text = announcement.title
            binding.announcementDescription.text = announcement.description
            binding.announcementTimestamp.text = announcement.timestamp
        }
    }

    class AnnouncementDiffCallback : DiffUtil.ItemCallback<Announcement>() {
        override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem.title == newItem.title && oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem == newItem
        }
    }
}