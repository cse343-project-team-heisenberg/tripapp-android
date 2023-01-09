package com.example.runningapplication.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.runningapplication.Model.FollowFollowers
import com.example.runningapplication.databinding.FollowFollowersRowBinding
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class FollowFollowersAdapter(val data:ArrayList<FollowFollowers>, val context:Context):RecyclerView.Adapter<FollowFollowersAdapter.PersonVH>() {
    class PersonVH(val binding:FollowFollowersRowBinding):ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonVH {
       val binding = FollowFollowersRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PersonVH(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PersonVH, position: Int) {

        Picasso.get().load(data.getOrNull(position)!!.picture).transform(
            CropCircleTransformation()
        ).into(holder.binding.imageViewPerson)
        holder.binding.textNameSurname.text = data.getOrNull(position)!!.name + " "+ data.getOrNull(position)!!.surname
        holder.binding.textUserName.text = data.getOrNull(position)!!.userName
        holder.binding.cardViewAdapter.setOnClickListener {


        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}