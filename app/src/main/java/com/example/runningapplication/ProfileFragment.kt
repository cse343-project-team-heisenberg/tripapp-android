package com.example.runningapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.runningapplication.Adapter.ProfileAdapter
import com.example.runningapplication.Model.Post
import com.example.runningapplication.Model.PostDetail
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.Model.UserInfo
import com.example.runningapplication.View.FollowersActivity
import com.example.runningapplication.View.FollowingActivity
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
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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


        binding.constFollowers.setOnClickListener {
            val intent = Intent(requireContext(),FollowersActivity::class.java)
            intent.putExtra("flag",0)
            startActivity(intent)
        }

        binding.constFollows.setOnClickListener {
            val intent = Intent(requireContext(),FollowingActivity::class.java)
            intent.putExtra("flag",0)
            startActivity(intent)

        }
        binding.textViewSave.setOnClickListener {
            val Intent = Intent(requireContext(),SaveActivity::class.java)
            startActivity(Intent)

        }
        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
            .get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot>{
                override fun onComplete(p0: Task<DocumentSnapshot>) {
                    if(p0.isSuccessful) {
                        try {
                            val tag = p0!!.result.toObject<UserAllData>()
                            Log.e("TAG",tag.toString())

                            if(tag!=null){
                                Picasso.get().load(tag!!.profilePicture.toString()).transform(
                                    CropCircleTransformation()
                                ).into(binding.imageView2)

                                binding.textNameSurname.text = tag!!.UserInfo!!.name.toString()+ " "+ tag!!.UserInfo!!.surname.toString()

                                binding.about.text = tag!!.UserInfo!!.userName.toString()
                                val manager = LinearLayoutManager(requireContext())
                                manager.orientation = LinearLayoutManager.VERTICAL
                                binding.recyclerView.layoutManager = manager
                                val adapter = ProfileAdapter(tag,requireContext())
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


                                var follows = 0
                                if(tag!!.follows != null){
                                    follows = tag!!.follows!!.follows!!.size
                                }

                            }
                        }catch (e:Exception ){


                        }

                    }
                }
            })
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    fun BindingUserInfo(data:  UserInfo) {
        binding.textNameSurname.text = data.name  + " " + data.surname


    }


}