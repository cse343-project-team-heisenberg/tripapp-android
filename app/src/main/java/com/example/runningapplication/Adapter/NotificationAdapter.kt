package com.example.runningapplication.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.runningapplication.Model.NotificationDetail
import com.example.runningapplication.databinding.NotificationRowBinding
import com.example.runningapplication.databinding.UserAdapterLayoutBinding
import com.squareup.picasso.Picasso


class NotificationAdapter(val notificationList: ArrayList<NotificationDetail>, val context: Context):
    RecyclerView.Adapter<NotificationAdapter.NotificationVH>() {
    class NotificationVH(val binding:NotificationRowBinding):ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH {
        val binding = NotificationRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationVH(binding)
    }

    override fun onBindViewHolder(holder: NotificationVH, position: Int) {
       holder.binding.textViewNotification.text = notificationList.get(position).description
        Picasso.get().load(notificationList.get(position).imageUrl).into(holder.binding.imageViewNotification)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

}