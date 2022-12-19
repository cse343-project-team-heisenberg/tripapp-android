package com.example.runningapplication.Adapter

import android.util.Log
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
       holder.binding.textViewUserName.text = data.UserInfo!!.userName
       Picasso.get().load(data.profilePicture).into(holder.binding.imageViewProfilePicture)
        Log.e("DENEME",data.UserInfo!!.userName.toString())
       holder.binding.textViewDescription.text = data.data!!.data!!.get(position)!!.description
       Picasso.get().load(data.data!!.data!!.get(position)!!.pictureUrl).into(holder.binding.imageTemp)
    }

    override fun getItemCount(): Int {
        Log.e("TAG",data.data!!.data!!.size.toString())
        return data.data!!.data!!.size
    }

}