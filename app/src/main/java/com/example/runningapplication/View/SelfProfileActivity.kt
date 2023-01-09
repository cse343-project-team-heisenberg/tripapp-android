package com.example.runningapplication.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapplication.Adapter.ProfileAdapter
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.Model.UserInfo
import com.example.runningapplication.R
import com.example.runningapplication.databinding.ActivityProfileBinding
import com.example.runningapplication.databinding.ActivitySelfProfilePictureBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class SelfProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySelfProfilePictureBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelfProfilePictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.constFollowers.setOnClickListener {
            val intent = Intent(applicationContext,FollowersActivity::class.java)
            intent.putExtra("flag",0)
            startActivity(intent)
        }

        binding.constFollows.setOnClickListener {
            val intent = Intent(applicationContext,FollowingActivity::class.java)
            intent.putExtra("flag",0)
            startActivity(intent)

        }

        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
            .get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot>{
                override fun onComplete(p0: Task<DocumentSnapshot>) {
                    if(p0.isSuccessful) {
                        val tag = p0!!.result.toObject<UserAllData>()
                        Log.e("TAG",tag.toString())

                        if(tag!=null){
                            binding.textNameSurname.text = tag!!.UserInfo!!.name.toString()+ " "+ tag!!.UserInfo!!.surname.toString()
                            binding.about.text = tag!!.UserInfo!!.userName.toString()
                            val manager = LinearLayoutManager(applicationContext)
                            manager.orientation = LinearLayoutManager.VERTICAL
                            binding.recyclerView.layoutManager = manager
                            val adapter = ProfileAdapter(tag,applicationContext)
                            binding.recyclerView.adapter = adapter
                            if(tag!!.follows!!.follows == null){
                                binding.textFollowsNumber.text = "0"
                            }else{
                                binding.textFollowsNumber.text = tag!!.follows!!.follows!!.size.toString()
                            }
                            if( tag.following == null||tag.following!!.following==null ){
                                binding.textFollowersNumber.text = "0"
                            }else{
                                binding.textFollowersNumber.text = tag.following!!.following!!.size.toString()
                            }

                            Picasso.get().load(tag!!.profilePicture.toString()).transform(
                                CropCircleTransformation()
                            ).into(binding.imageView2)
                            var follows = 0
                            if(tag!!.follows != null){
                                follows = tag!!.follows!!.follows!!.size
                            }

                        }



                    }
                }
            })
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this@SelfProfileActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    fun ConvertUserInfo(data: java.util.HashMap<String, UserInfo>): UserInfo {
        val name = data.get("name").toString()
        val surname = data.get("surname").toString()
        val username = data.get("username").toString()
        val mail = data.get("mail").toString()
        val password = data.get("password").toString()
        val uuid = data.get("uuid").toString()
        val userInfo = UserInfo(name,surname,username,mail,password,uuid)
        return userInfo

    }
    }
