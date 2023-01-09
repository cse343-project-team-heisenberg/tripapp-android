package com.example.runningapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapplication.Adapter.MainAdapter
import com.example.runningapplication.Adapter.NotificationAdapter
import com.example.runningapplication.Model.NotificationDetail
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.databinding.FragmentNotificationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase


class NotificationFragment : Fragment() {
    private lateinit var _binding: FragmentNotificationBinding
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(layoutInflater,container,false)

        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid.toString()).get().addOnCompleteListener {
           if(it.isSuccessful){
               val a = it.result.toObject<UserAllData>()
               if(a?.notification != null){
                   val data = a.notification
                    if(data!!.notification?.size == 0){
                        binding.textView2.visibility = View.VISIBLE
                    }
                   val list: ArrayList<NotificationDetail> = data!!.notification!!
                   val manager = LinearLayoutManager(requireContext())
                   val adapter = NotificationAdapter(list,requireContext())
                   manager.orientation = LinearLayoutManager.VERTICAL
                   binding.notificationRv.layoutManager = manager

                   binding.notificationRv.adapter = adapter
                   binding.notificationRv.adapter!!.notifyDataSetChanged()
               }

           }



        }

        return binding.root
    }


}