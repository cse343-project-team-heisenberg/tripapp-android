package com.example.runningapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapplication.Adapter.MainAdapter
import com.example.runningapplication.Model.MainPostDataClass
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.databinding.ActivitySaveBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class SaveActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySaveBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userList = ArrayList<MainPostDataClass>()
        val manager = LinearLayoutManager(applicationContext)
        val adapter = MainAdapter(userList,this)
        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid.toString()).get().addOnCompleteListener {
            if(it.isSuccessful){
                try {
                    val a = it.result.toObject<UserAllData>()
                    if(a!!.save != null) {
                            val userInfo = a!!.save!!.saved!!.get(0).userInfo
                            val profilePicture = a!!.save!!.saved!!.get(0).profilePicture
                        try {
                            for (i in a!!.save!!.saved!!) {
                                val userClass = MainPostDataClass(userInfo,i.postDetail,profilePicture)
                                userList.add(userClass)
                                manager.orientation = LinearLayoutManager.VERTICAL
                                binding.rvSaved.layoutManager = manager
                                binding.rvSaved.adapter = adapter
                                binding.rvSaved.adapter!!.notifyDataSetChanged()

                            }

                        }catch (e:Exception){

                        }
                    }

                }catch (e:Exception){
                }

            }
        }

    }
}