package com.example.runningapplication.View

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.runningapplication.Model.Post
import com.example.runningapplication.Model.PostDetail
import com.example.runningapplication.databinding.ActivityPostBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.type.LatLng
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    var secilenBitmap: Bitmap? = null
    var secilenGorsel: Uri? = null
    var temp = ArrayList<PostDetail>()
    var ltLng:com.google.android.gms.maps.model.LatLng ? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPostBinding.inflate(layoutInflater)



        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            // Permission has already been granted

            getLastLocation()
        }

        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                try {
                    val data: HashMap<String, Post>? = it.data?.getValue("data") as HashMap<String, Post>
                    if(data!=null){
                        val x = data.get("data") as ArrayList<PostDetail>
                        temp.addAll(x)
                    }
                }catch (e:java.lang.Exception) {

                }

            }

        binding.buttonSave.setOnClickListener {
            val uuid = UUID.randomUUID().toString()
            val map = HashMap<String, String>()
            map.put("textDescription", binding.editTextDescription.text.toString())
            map.put("post_picture", uuid)



            Firebase.storage.reference.child(Firebase.auth.currentUser!!.uid).child("postPicture")
                .child(uuid)
                .putFile(secilenGorsel!!).addOnSuccessListener { t ->
                    t.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                        val uri = task.result!!
                        Log.e("TAG", uri.toString())
                        val map = kotlin.collections.HashMap<String, Any>()
                        val like = ArrayList<String>()
                        val save = ArrayList<String>()
                        val comment = ArrayList<String>()
                        val postDetail = PostDetail(uri.toString(),binding.editTextDescription.text.toString(),like,save,comment,ltLng)
                        temp.add(postDetail)
                        val post = Post(temp, Firebase.auth.currentUser!!.uid)
                        map.put("data", post)
                        Firebase.firestore.collection("Post")
                            .document(Firebase.auth.currentUser!!.uid)
                            .update(map)
                            .addOnCompleteListener {
                                Toast.makeText(applicationContext, "Kaydedildi", Toast.LENGTH_LONG)
                                    .show()
                            }
                    }
                }
            /*
            Firebase.firestore.collection("app")
                .document(Firebase.auth.currentUser!!.uid)
                .collection("postPicture")
                .add(map).addOnSuccessListener {
                    Toast.makeText(applicationContext,"Kaydetti", Toast.LENGTH_LONG).show()
                }
            */
        }
        binding.imageView.setOnClickListener {
            val intentPhoto = Intent(Intent.ACTION_PICK)
            intentPhoto.setType("image/*")
            startActivityForResult(intentPhoto, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    secilenGorsel = data?.data
                    if (secilenGorsel != null) {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source =
                                ImageDecoder.createSource(this.contentResolver, secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)

                            binding.imageView.setImageBitmap(secilenBitmap)
                        } else {
                            secilenBitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                secilenGorsel
                            )
                            binding.imageView.setImageBitmap(secilenBitmap)
                        }
                    }
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted
                    getLastLocation()
                } else {
                    // permission denied
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->

                if (location != null) {
                    ltLng = com.google.android.gms.maps.model.LatLng(location.latitude,location.longitude)
                    Log.e("TAG",location.latitude.toString())

                }
            }
    }
}