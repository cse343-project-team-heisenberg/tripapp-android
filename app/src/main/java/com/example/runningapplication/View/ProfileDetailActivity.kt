package com.example.runningapplication.View

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapplication.Adapter.ProfileAdapter
import com.example.runningapplication.Following
import com.example.runningapplication.Model.Follows
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.R
import com.example.runningapplication.Util.ConstantValues
import com.example.runningapplication.databinding.ActivityProfileDetailBinding
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

class ProfileDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileDetailBinding
    private var follows: Follows? = null
    private var following: Following? = null
    var uuidFollowers :String ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent!!.getStringExtra("uuid").toString()
        uuidFollowers = intent
        CheckFollow(intent)
        binding.buttonFollow.setOnClickListener {
            Log.e("TAG1", binding.buttonFollow.text.toString())
            if (binding.buttonFollow.text.toString().equals("Takip Et")) {
                val following = HashMap<String, Any>()

                Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener(object:OnSuccessListener<DocumentSnapshot>{
                        override fun onSuccess(p0: DocumentSnapshot?) {
                            val tag = p0!!.toObject<UserAllData>()
                            try {
                                val new = tag?.following?.following

                                val dizi =  ArrayList<String>()
                                new?.let { it1 -> dizi.addAll(it1) }
                                dizi!!.add(intent.toString())
                                val Following = Following(dizi)
                                following.put("following", Following)

                                FirebaseUpdateData(following,Firebase.auth.currentUser!!.uid.toString())
                                //ConstantValues.follows = FollowsData
                                val following = HashMap<String, Any>()

                                Firebase.firestore.collection("Post").document(intent.toString())
                                    .get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                                        @SuppressLint("SetTextI18n")
                                        override fun onComplete(p0: Task<DocumentSnapshot>) {
                                            if(p0.isSuccessful){
                                                val new = tag?.follows?.follows
                                                if(new==null){
                                                    val list = ArrayList<String>()
                                                    list.add(Firebase.auth.currentUser!!.uid.toString())
                                                    val Follows = Follows(list)

                                                    following.put("follows",Follows)
                                                    FirebaseUpdateData(following,intent.toString())
                                                } else{
                                                    new?.add(Firebase.auth.currentUser!!.uid.toString())
                                                    val Follows = Follows(new)

                                                    following.put("follows",Following)
                                                    FirebaseUpdateData(following,intent.toString())
                                                }

                                            }
                                        }
                                    })


                                binding.buttonFollow.text = "Takip Ediliyor"

                            }catch (e:Exception) {

                            }
                        }

                    })

            }
            else if (binding.buttonFollow.text.toString().equals("Takip Ediliyor")) {
                binding.buttonFollow.text = "Takip Et"
                Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid.toString())
                    .get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                        @SuppressLint("SetTextI18n")
                        override fun onComplete(p0: Task<DocumentSnapshot>) {
                            if(p0.isSuccessful){
                                val tag = p0.result.toObject<UserAllData>()
                                Log.e("TAG",tag!!.follows.toString())
                                if(tag!!.following != null){

                                    for(i in tag!!.following!!.following!!) {

                                        if(i.toString().equals(uuidFollowers)){

                                            val following = HashMap<String, Any>()
                                            tag.following!!.following!!.remove(i)
                                            val Following = Following(tag.following.following)
                                            following.put("following",Following)
                                            FirebaseUpdateData(following, Firebase.auth.currentUser!!.uid.toString())


                                            Firebase.firestore.collection("Post").document(intent.toString()).get()
                                                .addOnSuccessListener(object:OnSuccessListener<DocumentSnapshot>{
                                                    override fun onSuccess(p0: DocumentSnapshot?) {
                                                        val tag = p0!!.toObject<UserAllData>()
                                                        try {
                                                            for(i in tag!!.follows!!.follows!!){
                                                                if(i.equals(Firebase.auth.currentUser!!.uid.toString())){
                                                                    tag!!.follows!!.follows!!.remove(i)
                                                                    val follows = HashMap<String, Any>()

                                                                    val Follows = Follows(tag!!.follows!!.follows)
                                                                    follows.put("follows", Follows)
                                                                    FirebaseUpdateData(follows,intent.toString())


                                                                }

                                                            }

                                                        }catch (e:Exception) {


                                                        }

                                                    }


                                                })


                                        }
                                    }
                                }
                            }
                        }
                    })
            }
        }

        binding.linearLayoutFollows.setOnClickListener {
            val intent = Intent(applicationContext,FollowingActivity::class.java)
            intent.putExtra("flag",1)
            intent.putExtra("uid",uuidFollowers)
            startActivity(intent)

        }
        binding.constraintLayout.setOnClickListener {
            val intent = Intent(applicationContext,FollowersActivity::class.java)
            intent.putExtra("flag",1)
            intent.putExtra("uid",uuidFollowers)
            startActivity(intent)
        }


    }
    fun FirebaseUpdateData(follows: HashMap<String,Any>,uuid:String){
        Firebase.firestore.collection("Post")
            .document(uuid)
            .update(follows)
            .addOnCompleteListener {

            }


    }
    override fun onStart() {
        super.onStart()
        FirebaseReadData(uuidFollowers.toString(), ::CheckFollowersOrNot)
    }
    private fun FirebaseReadData(uuid:String,mainOperation: (p0: Task<DocumentSnapshot>) -> Unit){
        Firebase.firestore.collection("Post").document(uuidFollowers.toString())
            .get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                @SuppressLint("SetTextI18n")
                override fun onComplete(p0: Task<DocumentSnapshot>) {
                  mainOperation(p0)
                }
            })
    }

    @SuppressLint("SetTextI18n")
    fun CheckFollowersOrNot(p0: Task<DocumentSnapshot>){
        if (p0.isSuccessful) {

                val tag = p0.result.toObject<UserAllData>()
                Log.e("TAG", tag.toString())
                follows = tag!!.follows
                if(tag.follows?.follows==null){
                    binding.textFollowsNumber.text = "0"
                }else{
                    binding.textFollowsNumber.text = tag.follows?.follows?.size.toString()
                }
                if(tag.following?.following==null){
                    binding.textFollowersNumber.text = "0"
                }else{
                    binding.textFollowersNumber.text = tag.following?.following?.size.toString()
                }
                binding.textNameSurname.text =
                    tag.UserInfo!!.name + " " + tag!!.UserInfo!!.surname
                binding.textViewUserName.text = tag!!.UserInfo!!.userName.toString()
                val manager = LinearLayoutManager(this@ProfileDetailActivity)
                manager.orientation = LinearLayoutManager.VERTICAL
                binding.recyclerView.layoutManager = manager
                val adapter = ProfileAdapter(tag,applicationContext)
                binding.recyclerView.adapter = adapter
                Picasso.get().load(tag!!.profilePicture.toString()).into(binding.imageView)


        }

    }

    private fun CheckFollow(uuid: String) {

        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid.toString())
            .get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                override fun onComplete(p0: Task<DocumentSnapshot>) {
                    if (p0.isSuccessful) {

                        try {
                            val tag = p0!!.result.toObject<UserAllData>()
                            if (tag!!.following != null) {
                                for (i in tag!!.following!!.following!!) {
                                    Log.e("TAG","${uuid} ${i}")
                                    if (uuid.toString().equals(i.toString())) {
                                        binding.buttonFollow.text = "Takip Ediliyor"
                                    }

                                }
                            }
                        }catch (e:Exception) {

                        }


                    }
                }
            })
    }
}

