package com.example.runningapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapplication.Adapter.MainAdapter
import com.example.runningapplication.Adapter.ProfileAdapter
import com.example.runningapplication.Adapter.UserAdapter
import com.example.runningapplication.Model.MainPostDataClass
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.Model.UserInfoSearch
import com.example.runningapplication.Util.ConstantValues
import com.example.runningapplication.databinding.FragmentDiscoverBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class DiscoverFragment : Fragment() {
    private lateinit var _binding: FragmentDiscoverBinding
    private val binding get() = _binding!!
    val userList = ArrayList<UserInfoSearch>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater,container,false)
        getDiscoverList()
        Firebase.firestore.collection("Post").get().addOnCompleteListener {
            if(it.isSuccessful){
                for(datas in it.result){
                    val a = datas.toObject<UserAllData>()
                    val userInfo = a.UserInfo
                    val info = userInfo?.let { it1 -> a.profilePicture?.let { it2 ->
                        UserInfoSearch(it1,
                            it2
                        )
                    } }
                    Log.e("TAG",a.UserInfo?.userName.toString())
                    info?.let { it1 -> userList.add(it1) }

                }

            }
        }
        val tempList = ArrayList<UserInfoSearch>()
        binding.searchView.setOnSearchClickListener {
            binding.recyclerView.removeAllViews()
            tempList.clear()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchText=query!!.toLowerCase(Locale.getDefault())
                if(searchText!!.isNotEmpty()){

                    for(i in 0 until userList.size){
                        if(searchText.equals(userList.get(i).userInfo.userName)){
                            tempList.add(userList.get(i))
                            Log.e("TAG1",tempList.size.toString())

                        }
                        val manager = LinearLayoutManager(requireContext())
                        manager.orientation = LinearLayoutManager.VERTICAL
                        binding.recyclerView.layoutManager = manager
                        val adapter = UserAdapter(tempList,requireContext())
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.adapter!!.notifyDataSetChanged()

                    }

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
        binding.searchView.setOnCloseListener(object : SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                tempList.clear()
                binding.recyclerView.removeAllViews()
                getDiscoverList()
                return false
            }


        })


        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getDiscoverList(){

        val userList = ArrayList<MainPostDataClass>()
        val manager = LinearLayoutManager(requireContext())
        val adapter = MainAdapter(userList,requireContext())
        Firebase.firestore.collection("Post").get().addOnCompleteListener {
            if(it.isSuccessful){
                try {
                    for(datas in it.result){
                        val a = datas.toObject<UserAllData>()
                        if(a.data != null && !a.UserInfo!!.uuid.equals(Firebase.auth.currentUser!!.uid.toString())) {

                                val userInfo = a.UserInfo
                                val profilePicture = a.profilePicture
                                var flag = false

                            if(a.following == null){
                                val userInfo = a.UserInfo
                                val profilePicture = a.profilePicture
                                if(a.data!=null){
                                    for(i in a.data!!.data!!){
                                        val data = i
                                        val userClass = MainPostDataClass(userInfo,data,profilePicture)
                                        userList.add(userClass)
                                    }
                                    manager.orientation = LinearLayoutManager.VERTICAL
                                    binding.recyclerView.layoutManager = manager

                                    binding.recyclerView.adapter = adapter
                                    binding.recyclerView.adapter!!.notifyDataSetChanged()
                                }

                            }

                                for(i in 0 until a.follows!!.follows!!.size){
                                    for(j in 0 until ConstantValues.follows!!.follows!!.size){
                                        Log.e("TEMP",ConstantValues.follows!!.follows!!.get(j).equals(a.follows.follows!!.get(i)).toString())
                                        if(ConstantValues.follows!!.follows!!.get(j).equals(a.follows.follows!!.get(i))){
                                            flag = true
                                        }

                                    }
                                    Log.e("FLAG",flag.toString())
                                    if(flag != false) {
                                        val userInfo = a.UserInfo
                                        val profilePicture = a.profilePicture
                                        val data = a.data!!.data!!.get(i)
                                        val userClass = MainPostDataClass(userInfo,data,profilePicture)
                                        userList.add(userClass)
                                        flag = false
                                    }
                                }
                                Log.e("Follow",ConstantValues.follows.toString())
                                Log.e("SIZE",userList.size.toString())
                                manager.orientation = LinearLayoutManager.VERTICAL
                                binding.recyclerView.layoutManager = manager

                                binding.recyclerView.adapter = adapter
                                binding.recyclerView.adapter!!.notifyDataSetChanged()

                        }
                    }
                }catch (e:Exception){

                }

            }
        }

    }


}