package com.example.runningapplication.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.databinding.ProfileAdapterBinding
import com.squareup.picasso.Picasso

class ProfileAdapter(val data: UserAllData): RecyclerView.Adapter<ProfileAdapter.AdapterVH>() {
    class AdapterVH(val binding:ProfileAdapterBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterVH {
       val binding = ProfileAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AdapterVH(binding)
    }

    override fun onBindViewHolder(holder: AdapterVH, position: Int) {
       holder.binding.textViewUserName.text = data.UserInfo.userName
       Picasso.get().load(data.profilePicture).into(holder.binding.imageViewProfilePicture)
       holder.binding.textViewDescription.text = data.data.data.getOrNull(position)!!.description
       Picasso.get().load(data.data.data.getOrNull(position)!!.pictureUrl).into(holder.binding.imageViewProfilePicture)
    }

    override fun getItemCount(): Int {
        return data.data.data.size
    }

}