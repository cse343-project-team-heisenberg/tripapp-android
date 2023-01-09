package com.example.runningapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapplication.Adapter.MainAdapter
import com.example.runningapplication.Adapter.ProfileAdapter
import com.example.runningapplication.Model.MainPostDataClass
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.Util.ConstantValues
import com.example.runningapplication.View.PostActivity
import com.example.runningapplication.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.floatActionButtonPost2.setOnClickListener {
            val intent = Intent(requireContext(), PostActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val userList = ArrayList<MainPostDataClass>()
        val manager = LinearLayoutManager(requireContext())
        val adapter = MainAdapter(userList,requireContext())
        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid.toString()).get().addOnSuccessListener {
            try {
                val person = it.toObject<UserAllData>()
                if (person?.follows?.follows != null) {
                    ConstantValues.follows = person.follows
                    for(i in person.follows.follows!!){
                        Firebase.firestore.collection("Post").document(i.toString()).get().addOnCompleteListener {
                            if(it.isSuccessful){
                                try {
                                    val a = it.result.toObject<UserAllData>()
                                    if(a!!.data != null) {
                                        for(i in 0  until a.data!!.data!!.size){
                                            val userInfo = a.UserInfo
                                            val profilePicture = a.profilePicture
                                            val data = a.data!!.data!!.get(i)
                                            val userClass = MainPostDataClass(userInfo,data,profilePicture)
                                            userList.add(userClass)

                                        }
                                    }

                                    manager.orientation = LinearLayoutManager.VERTICAL
                                    binding.recyclerView.layoutManager = manager
                                    binding.recyclerView.adapter = adapter
                                    binding.recyclerView.adapter!!.notifyDataSetChanged()

                                }catch (e:Exception){
                                }
                                Log.e("TAG",userList.size.toString())
                            }
                        }
                    }
                }
            }catch (e:Exception){

            }



        }



    }
}