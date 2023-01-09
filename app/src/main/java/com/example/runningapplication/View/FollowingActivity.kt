package com.example.runningapplication.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapplication.Adapter.MainAdapter
import com.example.runningapplication.Adapter.UserAdapter
import com.example.runningapplication.Model.MainPostDataClass
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.Model.UserInfoSearch
import com.example.runningapplication.R
import com.example.runningapplication.databinding.ActivityFollowing2Binding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FollowingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFollowing2Binding
    val list = ArrayList<UserInfoSearch>()
    val userUidList = ArrayList<String>()
    var uuid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowing2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val userList = ArrayList<MainPostDataClass>()
        val intent = getIntent().getIntExtra("flag",-1)
        if(intent==0){
            uuid = Firebase.auth.currentUser!!.uid
        }else {
            uuid = getIntent().getStringExtra("uid").toString()
        }
        Firebase.firestore.collection("Post").document(uuid).get().addOnSuccessListener {
                        val a = it.toObject<UserAllData>()
                        if(a!=null && a.follows != null){
                            for(i in a.follows.follows!!){

                                userUidList.add(i)
                            }

                        }
            var temp = 0
            for(i in userUidList){
                Firebase.firestore.collection("Post").document(i.toString()).get().addOnCompleteListener {

                   val personDatas = it.result.toObject<UserAllData>()
                    val userSearch = UserInfoSearch(personDatas!!.UserInfo!!,personDatas!!.profilePicture!!)
                    list.add(userSearch)

                    ++temp
                /*


                 */
                    lifecycleScope.launch {
                        if(temp==userUidList.size){
                            val adapter = UserAdapter(list,applicationContext)
                            val manager = LinearLayoutManager(applicationContext)
                            manager.orientation = LinearLayoutManager.VERTICAL
                            binding.recyclerViewFollowing.layoutManager = manager
                            binding.recyclerViewFollowing.adapter = adapter
                            binding.recyclerViewFollowing.adapter!!.notifyDataSetChanged()
                        }

                    }

                }
            }

        }


    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }
}