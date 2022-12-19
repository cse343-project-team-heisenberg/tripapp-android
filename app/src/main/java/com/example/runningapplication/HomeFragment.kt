package com.example.runningapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapplication.Adapter.MainAdapter
import com.example.runningapplication.Adapter.ProfileAdapter
import com.example.runningapplication.Model.MainPostDataClass
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.databinding.FragmentHomeBinding
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
        val userList = ArrayList<MainPostDataClass>()
        Firebase.firestore.collection("Post").get().addOnCompleteListener {
            if(it.isSuccessful){
                for(datas in it.result){
                    val a = datas.toObject<UserAllData>()
                    for(i in 0  until a.data!!.data!!.size){
                        val userInfo = a.UserInfo
                        val profilePicture = a.profilePicture
                        val data = a.data!!.data!!.get(i)
                        val userClass = MainPostDataClass(userInfo,data,profilePicture)
                        userList.add(userClass)
                            val manager = LinearLayoutManager(requireContext())
                            manager.orientation = LinearLayoutManager.VERTICAL
                            binding.recyclerView.layoutManager = manager
                            val adapter = MainAdapter(userList,requireContext())
                            binding.recyclerView.adapter = adapter
                    }
                    //val mainPost = MainPostDataClass(a.UserInfo,a.profilePicture)
                }
                Log.e("TAG",userList.size.toString())
            }
        }


        return binding.root
    }

}