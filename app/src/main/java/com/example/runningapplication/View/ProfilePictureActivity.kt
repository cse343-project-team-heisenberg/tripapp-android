package com.example.runningapplication.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.example.runningapplication.ShareActivity
import com.example.runningapplication.databinding.ActivityProfilePictureBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.UUID

class ProfilePictureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePictureBinding
    var secilenBitmap: Bitmap? = null
    var secilenGorsel: Uri? = null
    private var shared : SharedPreferences ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shared = getSharedPreferences("com.example.runningapplication", Context.MODE_PRIVATE)
        binding = ActivityProfilePictureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setTitle("Choose a profile picture")
        val pictureBoolean = shared!!.getBoolean("profilePicture",false)
        binding.imagaViewProfile.setOnClickListener {
            val intentPhoto = Intent(Intent.ACTION_PICK)
            intentPhoto.setType("image/*")
            startActivityForResult(intentPhoto,1)
        }
        binding.button.setOnClickListener {
            shared!!.edit().putBoolean("profilePicture",true).apply()
            val intent = Intent(applicationContext, ShareActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when(requestCode) {
                1 -> {
                secilenGorsel = data?.data
                    if(secilenGorsel != null) {
                        if(Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)

                            binding.imagaViewProfile.setImageBitmap(secilenBitmap)
                        }else{
                            secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                            binding.imagaViewProfile.setImageBitmap(secilenBitmap)
                        }
                        val data: List<Bitmap> = ArrayList<Bitmap>()
                        val lt: ArrayList<String> = arrayListOf()

                        val list = bitmapToString(secilenBitmap!!)
                            lt.add(list.toString())
                        val uuid = UUID.randomUUID().toString()
                        val map = HashMap<String,String>()
                        map.put("profile_picture",uuid)
                        Firebase.storage.reference.child(Firebase.auth.currentUser!!.uid.toString())
                            .child("profilePicture")
                            .child(uuid)
                            .putFile(secilenGorsel!!).addOnSuccessListener { t ->
                                t.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task->
                                    val uri = task.result!!
                                    val map = HashMap<String,Any>()
                                    map.put("profilePicture",uri)

                                    Firebase.firestore.collection("Post")
                                        .document(Firebase.auth.currentUser!!.uid)
                                        .update(map)
                                        .addOnCompleteListener {
                                            Toast.makeText(applicationContext,"Kaydedildi",Toast.LENGTH_LONG).show()

                                        }
                                }
                            }
                    }
                }
            }
        }
    }
    fun bitmapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        val temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}