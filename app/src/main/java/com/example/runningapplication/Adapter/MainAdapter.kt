package com.example.runningapplication.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapplication.HomeFragmentDirections
import com.example.runningapplication.Model.MainPostDataClass
import com.example.runningapplication.ProfilePictureActivity
import com.example.runningapplication.databinding.MainLayoutBinding
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
                val intent = Intent(context,ProfilePictureActivity::class.java)
               context.startActivity(intent)
           }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}