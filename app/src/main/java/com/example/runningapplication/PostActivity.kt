package com.example.runningapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.runningapplication.databinding.ActivityPostBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PostActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPostBinding
    var secilenBitmap: Bitmap? = null
    var secilenGorsel: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonSave.setOnClickListener {
            val uuid = UUID.randomUUID().toString()
            val map = HashMap<String,String>()
            map.put("textDescription",binding.editTextDescription.text.toString())
            map.put("post_picture",uuid)
            Firebase.storage.reference.child(Firebase.auth.currentUser!!.uid).child("postPicture")
                .child(uuid)
                .putFile(secilenGorsel!!)
            Firebase.firestore.collection("app")
                .document(Firebase.auth.currentUser!!.uid)
                .collection("postPicture")
                .add(map).addOnSuccessListener {
                    Toast.makeText(applicationContext,"Kaydetti", Toast.LENGTH_LONG).show()
                }


        }

        binding.imageView.setOnClickListener {
            val intentPhoto = Intent(Intent.ACTION_PICK)
            intentPhoto.setType("image/*")
            startActivityForResult(intentPhoto,1)


        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                1 -> {
                    secilenGorsel = data?.data
                    if(secilenGorsel != null) {
                        if(Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)

                            binding.imageView.setImageBitmap(secilenBitmap)
                        }else{
                            secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                            binding.imageView.setImageBitmap(secilenBitmap)
                        }







                        /*
                      Firebase.firestore.collection("media").document(uuid)
                          .collection("profileId").document("datas")
                          .set(map)
                          .addOnSuccessListener {
                              Toast.makeText(applicationContext,"Başarılı",Toast.LENGTH_LONG).show()
                          }.addOnFailureListener {
                              Toast.makeText(applicationContext,it.localizedMessage.toString(),Toast.LENGTH_LONG).show()
                          }
                        */



                    }
                }
            }
        }
    }
}