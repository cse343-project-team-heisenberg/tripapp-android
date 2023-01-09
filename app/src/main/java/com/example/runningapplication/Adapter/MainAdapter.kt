package com.example.runningapplication.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapplication.Model.MainPostDataClass
import com.example.runningapplication.View.PostDetailActivity
import com.example.runningapplication.View.ProfileDetailActivity
import com.example.runningapplication.View.SelfProfileActivity
import com.example.runningapplication.databinding.MainLayoutBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class MainAdapter(val data: ArrayList<MainPostDataClass>,val context: Context): RecyclerView.Adapter<MainAdapter.AdapterMainVH>() {
    class AdapterMainVH(val binding: MainLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterMainVH {
        val binding = MainLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AdapterMainVH(binding)
    }

    override fun onBindViewHolder(holder: AdapterMainVH, position: Int) {
            holder.binding.textViewUserName.text = data.getOrNull(position)!!.userInfo!!.userName
            Picasso.get().load(data.getOrNull(position)!!.profilePicture.toString()).transform(CropCircleTransformation()).into(holder.binding.imageViewProfilePicture)
            holder.binding.textViewDescription.text = data.getOrNull(position)!!.postDetail!!.description
            Picasso.get().load(data.getOrNull(position)!!.postDetail!!.pictureUrl.toString()).into(holder.binding.imageViewDescription)

           holder.binding.constraintProfile.setOnClickListener {
               val uuid = data.get(position).userInfo!!.uuid.toString()
               //Navigation.findNavController(it).navigate(HomeFragmentDirections.actionHomeFragmentToPostDetailFragment()
               if(uuid!!.equals(Firebase.auth!!.currentUser!!.uid)){
                   val intent = Intent(context, SelfProfileActivity::class.java)
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                   intent.putExtra("uuid",uuid)
                   context.startActivity(intent)
               }else {
                   val intent = Intent(context, ProfileDetailActivity::class.java)
                   intent.putExtra("uuid",uuid)
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                   context.startActivity(intent)
               }

           }
            holder.binding.constraintPostDetail.setOnClickListener {
                val uuid = data.get(position).userInfo!!.uuid.toString()
                val intent = Intent(context, PostDetailActivity::class.java)
                intent.putExtra("uuid",uuid)

                passClass = data.get(position)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
    }

    override fun getItemCount(): Int {
        return data.size
    }
    companion object {
        var passClass: MainPostDataClass?=null


    }

}