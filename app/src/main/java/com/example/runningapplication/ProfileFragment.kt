package com.example.runningapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.runningapplication.Adapter.ProfileAdapter
import com.example.runningapplication.Model.Post
import com.example.runningapplication.Model.PostDetail
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.Model.UserInfo
import com.example.runningapplication.View.ProfileActivity
import com.example.runningapplication.databinding.FragmentProfileBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.util.Objects

class ProfileFragment : Fragment() {

    private lateinit var _binding:FragmentProfileBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        /*
        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                val image = it.data?.getValue("profilePicture")
                Picasso.get().load(image.toString()).resize(285,215).into(binding.imageView2)
                val data : HashMap<String, UserInfo> = it.data?.getValue("UserInfo") as HashMap<String, UserInfo>
                val userInfo = ConvertUserInfo(data)
                binding.textNameSurname.text = userInfo.name + " "+userInfo.surname
                val manager = LinearLayoutManager(requireContext())
                manager.orientation = LinearLayoutManager.HORIZONTAL
               // val adapter = ProfileAdapter()
            }
        */

        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
            .get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot>{
                override fun onComplete(p0: Task<DocumentSnapshot>) {
                    if(p0.isSuccessful) {
                        val tag = p0!!.result.toObject<UserAllData>()
                        Log.e("TAG",tag.toString())
                        binding.textNameSurname.text = tag!!.UserInfo!!.name.toString()+ " "+ tag!!.UserInfo!!.surname.toString()
                        val manager = LinearLayoutManager(requireContext())
                        manager.orientation = LinearLayoutManager.VERTICAL
                        binding.recyclerView.layoutManager = manager
                        val adapter = ProfileAdapter(tag)
                        binding.recyclerView.adapter = adapter
                        Picasso.get().load(tag!!.profilePicture.toString()).into(binding.imageView2)
                    }
                }
            })
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        return binding.root
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