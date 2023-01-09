package com.example.runningapplication.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapplication.Model.UserInfoSearch
import com.example.runningapplication.View.ProfileDetailActivity
import com.example.runningapplication.View.SelfProfileActivity
import com.example.runningapplication.databinding.UserAdapterLayoutBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class UserAdapter (val userList: ArrayList<UserInfoSearch>,val context: Context):
    RecyclerView.Adapter<UserAdapter.UserInfoVH>() {
    class UserInfoVH(val binding:UserAdapterLayoutBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoVH {
        val binding = UserAdapterLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserInfoVH(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserInfoVH, position: Int) {
        Picasso.get().load(userList.getOrNull(position)!!.profilePicture).transform(
            CropCircleTransformation()).into(holder.binding.imageViewPersonPicture)
        holder.binding.textViewUserName.text = userList.getOrNull(position)!!.userInfo.userName
        holder.binding.textViewFullName.text = userList.getOrNull(position)!!.userInfo.name +" "+ userList.getOrNull(position)?.userInfo!!.surname
        holder.itemView.setOnClickListener {
            if(userList.get(position).userInfo.uuid!!.equals(Firebase.auth!!.currentUser!!.uid)){
                val intent = Intent(context,SelfProfileActivity::class.java)
                intent.putExtra("uuid",userList.get(position).userInfo.uuid)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }else {
                val intent = Intent(context,ProfileDetailActivity::class.java)
                intent.putExtra("uuid",userList.get(position).userInfo.uuid)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
        }

    override fun getItemCount(): Int {
        return userList.size
    }
}