package com.example.runningapplication.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapplication.Model.MainPostDataClass
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.View.PostDetailActivity
import com.example.runningapplication.View.ProfileDetailActivity
import com.example.runningapplication.databinding.ProfileAdapterBinding
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class ProfileAdapter(val data: UserAllData,val context: Context): RecyclerView.Adapter<ProfileAdapter.AdapterVH>() {
    class AdapterVH(val binding:ProfileAdapterBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterVH {
       val binding = ProfileAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AdapterVH(binding)
    }

    override fun onBindViewHolder(holder: AdapterVH, position: Int) {
       holder.binding.textViewUserName.text = data.UserInfo!!.userName
        Picasso.get().load(data.profilePicture).transform(
            CropCircleTransformation()
        ).into(holder.binding.imageViewProfilePicture)

       holder.binding.textViewDescription.text = data.data!!.data!!.get(position)!!.description
       Picasso.get().load(data.data!!.data!!.get(position)!!.pictureUrl).into(holder.binding.imageTemp)
        holder.binding.constraintId.setOnClickListener {
            MainAdapter.passClass = MainPostDataClass()
            MainAdapter.passClass?.profilePicture = data!!.profilePicture
            MainAdapter.passClass?.userInfo = data!!.UserInfo
            MainAdapter.passClass?.postDetail = data!!.data!!.data!!.get(position)
           val intent = Intent(context,PostDetailActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        var size = 0
        if (data.data?.data == null) {

        }else {
            size = data.data?.data!!.size
        }
        return size
    }

}