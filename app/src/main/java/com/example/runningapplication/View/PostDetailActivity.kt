package com.example.runningapplication.View

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapplication.Adapter.MainAdapter
import com.example.runningapplication.Model.*
import com.example.runningapplication.R
import com.example.runningapplication.Util.ConstantValues
import com.example.runningapplication.databinding.ActivityPostDetailBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.util.Objects

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Picasso.get().load(MainAdapter.passClass!!.postDetail!!.pictureUrl.toString()).into(binding.imageViewPost)

        binding.textViewPostDetail.text = MainAdapter.passClass!!.postDetail!!.description.toString()


        binding.textLikeCount.text = MainAdapter.passClass!!.postDetail!!.like!!.size.toString()



        Picasso.get().load(MainAdapter.passClass!!.profilePicture).into(binding.imageViewProfilePicture)


        binding.textUserId.text = MainAdapter.passClass!!.userInfo!!.userName


        binding.imageViewLocation.setOnClickListener {
            try {
                val gmmIntentUri = Uri.parse("google.streetview:cbll=${MainAdapter.passClass!!.postDetail!!.ltlng!!.latitude},${MainAdapter.passClass!!.postDetail!!.ltlng!!.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }catch (e:Exception) {

            }


        }
        var flag1 = false
        binding.imageViewSave.setOnClickListener {
            if(flag1 == false) {
                            Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid.toString()).get().addOnCompleteListener {
                                if(it.isSuccessful){
                                    try {
                                        val my = it.result.toObject<UserAllData>()
                                        val data = Saved()

                                        var tempFlag = false
                                        Firebase.firestore.collection("Post").document(MainAdapter.passClass!!.userInfo!!.uuid.toString()).get().addOnCompleteListener {
                                            val a = it.result.toObject<UserAllData>()
                                            for(i in a!!.data!!.data!!){
                                                if(i.pictureUrl!!.equals(MainAdapter.passClass!!.postDetail!!.pictureUrl)){
                                                    if(i.save != null) {
                                                        for(j in i.save!!) {
                                                            if(j.equals(Firebase.auth.currentUser!!.uid.toString())){
                                                                tempFlag = true

                                                                break
                                                            }

                                                        }
                                                        if(tempFlag == false){

                                                            i.save!!.add(Firebase.auth.currentUser!!.uid.toString())

                                                            val post = Post(a!!.data!!.data,MainAdapter.passClass!!.userInfo!!.uuid.toString())
                                                            val map = kotlin.collections.HashMap<String, Any>()
                                                            map.put("data", post)
                                                            Firebase.firestore.collection("Post")
                                                                .document(MainAdapter.passClass!!.userInfo!!.uuid.toString())
                                                                .update(map)
                                                                .addOnCompleteListener {
                                                                    Toast.makeText(applicationContext, "Kaydedildi", Toast.LENGTH_LONG)
                                                                        .show()
                                                                }

                                                            if(a!!.notification==null){
                                                                a!!.notification = Notification()
                                                                a!!.notification?.notification = ArrayList()
                                                            }

                                                                data.saved = ArrayList<MainPostDataClass>()
                                                                if(my!!.save!=null){
                                                                    data.saved!!.addAll(my!!.save!!.saved!!)
                                                                }
                                                                data.saved!!.add(MainAdapter.passClass!!)
                                                                val map1 = HashMap<String,Saved>()
                                                                map1.put("save",data)
                                                                Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid.toString())
                                                                    .update(map1 as Map<String, Any>)

                                                                val notification = Notification()
                                                                val postDetail = NotificationDetail("Biri Gönderinizi Kaydetti",MainAdapter!!.passClass!!.postDetail?.pictureUrl.toString())
                                                                a!!.notification!!.notification!!.add(postDetail)
                                                                a!!.notification?.notification?.let { it1 ->
                                                                    notification.notification?.addAll(it1)
                                                                }
                                                                val map2 = HashMap<String,Notification>()
                                                                map2.put("notification",a!!.notification!!)
                                                                Firebase.firestore.collection("Post").document(MainAdapter.passClass!!.userInfo!!.uuid.toString())
                                                                    .update(map2 as Map<String, Any>)



                                                        }

                                                    }
                                                    else {

                                                        if(tempFlag == false){
                                                            i.save = ArrayList()
                                                            i.save!!.add(Firebase.auth.currentUser!!.uid.toString())

                                                            val post = Post(a!!.data!!.data,MainAdapter.passClass!!.userInfo!!.uuid.toString())
                                                            val map = kotlin.collections.HashMap<String, Any>()
                                                            map.put("data", post)
                                                            Firebase.firestore.collection("Post")
                                                                .document(MainAdapter.passClass!!.userInfo!!.uuid.toString())
                                                                .update(map)
                                                                .addOnCompleteListener {
                                                                    Toast.makeText(applicationContext, "Kaydedildi", Toast.LENGTH_LONG)
                                                                        .show()
                                                                }

                                                            if(a!!.notification==null){
                                                                a!!.notification = Notification()
                                                                a!!.notification?.notification = ArrayList()
                                                            }

                                                            data.saved = ArrayList<MainPostDataClass>()
                                                            if(my!!.save!=null){
                                                                data.saved!!.addAll(my!!.save!!.saved!!)
                                                            }
                                                            data.saved!!.add(MainAdapter.passClass!!)
                                                            val map1 = HashMap<String,Saved>()
                                                            map1.put("save",data)
                                                            Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid.toString())
                                                                .update(map1 as Map<String, Any>)

                                                            val notification = Notification()
                                                            val postDetail = NotificationDetail("Biri Gönderinizi Kaydetti",MainAdapter!!.passClass!!.postDetail?.pictureUrl.toString())
                                                            a!!.notification!!.notification!!.add(postDetail)
                                                            a!!.notification?.notification?.let { it1 ->
                                                                notification.notification?.addAll(it1)
                                                            }
                                                            val map2 = HashMap<String,Notification>()
                                                            map2.put("notification",a!!.notification!!)
                                                            Firebase.firestore.collection("Post").document(MainAdapter.passClass!!.userInfo!!.uuid.toString())
                                                                .update(map2 as Map<String, Any>)



                                                        }


                                                    }
                                                    break
                                                }

                                            }




                                        }
                                        flag1 = true
                                    }catch (e:Exception){

                                    }

                                }
                            }

            }else{
                flag1 = false

            }

        }
        var flag = false
        val likeList = ArrayList<String>()
       binding.imageViewLike.setOnClickListener {
           if(MainAdapter.passClass!!.postDetail!!.like != null) {
               likeList.addAll(MainAdapter.passClass!!.postDetail!!.like!!)
               for(i in MainAdapter.passClass!!.postDetail!!.like!!){
                   if(i.equals(Firebase.auth.currentUser!!.uid)) {
                       flag = true
                       likeList.remove(i)
                       break
                   }
               }
           }
            if(flag == false) {
                binding.imageViewLike.setImageResource(R.drawable.ic_baseline_favorite_24)
                val like = HashMap<String, Any>()

              //  val likeList1 = ArrayList<String>()
               // likeList1.addAll(likeList)
                //likeList1.add(Firebase.auth.currentUser!!.uid.toString())
                //like.put("like", likeList1)
                //UpdateMap(like)
                Firebase.firestore.collection("Post").document(MainAdapter.passClass!!.userInfo!!.uuid.toString()).get().addOnCompleteListener {
                    val a = it.result.toObject<UserAllData>()

                    val notification = Notification()
                    val postDetail = NotificationDetail("Biri Gönderinizi Beğendi",MainAdapter!!.passClass!!.postDetail?.pictureUrl.toString())
                    a!!.notification!!.notification!!.add(postDetail)
                    a!!.notification?.notification?.let { it1 ->
                        notification.notification?.addAll(it1)
                    }
                    val map = HashMap<String,Notification>()
                    map.put("notification",a!!.notification!!)
                    Firebase.firestore.collection("Post").document(MainAdapter.passClass!!.userInfo!!.uuid.toString())
                        .update(map as Map<String, Any>)


                        for(i in a!!.data!!.data!!){
                            if(i!!.pictureUrl.equals(MainAdapter.passClass!!.postDetail!!.pictureUrl)){
                                i.like?.add(Firebase.auth.currentUser!!.uid.toString())
                                val post = Post(a!!.data!!.data,MainAdapter.passClass!!.userInfo!!.uuid.toString())
                                val map = kotlin.collections.HashMap<String, Any>()
                                map.put("data", post)
                                Firebase.firestore.collection("Post")
                                    .document(MainAdapter.passClass!!.userInfo!!.uuid.toString())
                                    .update(map)
                                    .addOnCompleteListener {
                                        Toast.makeText(applicationContext, "Kaydedildi", Toast.LENGTH_LONG)
                                            .show()
                                    }
                            }

                        }






                }


                flag = true

            }else {

                binding.imageViewLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                flag = false
            }

       }

    }
    fun UpdateMap(data: HashMap<String,Any>){
        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid.toString())
            .update(data).addOnCompleteListener {

            }

    }



}