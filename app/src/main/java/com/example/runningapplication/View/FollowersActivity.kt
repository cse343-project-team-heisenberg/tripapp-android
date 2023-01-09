package com.example.runningapplication.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapplication.Adapter.ProfileAdapter
import com.example.runningapplication.Adapter.UserAdapter
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.Model.UserInfo
import com.example.runningapplication.Model.UserInfoSearch
import com.example.runningapplication.R
import com.example.runningapplication.databinding.ActivityFollowersBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.launch

class FollowersActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFollowersBinding
    val list = ArrayList<UserInfoSearch>()
    val userUidList = ArrayList<String>()
    var uuid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent().getIntExtra("flag",-1)
        if(intent==0){
            uuid = Firebase.auth.currentUser!!.uid
        }else {
            uuid = getIntent().getStringExtra("uid").toString()
        }
        Firebase.firestore.collection("Post").document(uuid).get().addOnSuccessListener {
            val a = it.toObject<UserAllData>()
            if(a!=null && a.following != null){
                for(i in a.following.following!!){
                    userUidList.add(i)
                }

            }
            var temp = 0
            for(i in userUidList){
                Firebase.firestore.collection("Post").document(i.toString()).get().addOnCompleteListener {
                try {
                    val personDatas = it.result.toObject<UserAllData>()
                    val userSearch = UserInfoSearch(personDatas!!.UserInfo!!,personDatas!!.profilePicture!!)
                    list.add(userSearch)
                    ++temp
                    lifecycleScope.launch {
                        if(temp==userUidList.size){
                            val adapter = UserAdapter(list,applicationContext)
                            val manager = LinearLayoutManager(applicationContext)
                            manager.orientation = LinearLayoutManager.VERTICAL
                            binding.recyclerViewFollowers.layoutManager = manager
                            binding.recyclerViewFollowers.adapter = adapter
                            binding.recyclerViewFollowers.adapter!!.notifyDataSetChanged()
                        }
                    }
                }catch (e:Exception) {

                }

                }
            }
        }

    }
}