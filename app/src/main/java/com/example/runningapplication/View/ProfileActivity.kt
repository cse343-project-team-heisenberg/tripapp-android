package com.example.runningapplication.View

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.runningapplication.EditProfileUser
import com.example.runningapplication.Model.UserInfo
import com.example.runningapplication.R
import com.example.runningapplication.databinding.ActivityProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonEdit.setOnClickListener {
            val intent = Intent(applicationContext, EditProfileUser::class.java)
            startActivity(intent)

        }



    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        /*
        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
            .get().addOnCompleteListener {
                val data = it!!.result.data!!.
            /*
                val name = it!!.result.data?.get("name") as String?
                val password = it!!.result.data?.get("password") as String?
                val surname = it!!.result.data?.get("surname") as String?
                val mailAddress = it!!.result.data?.get("mail") as String?
                val userName = it!!.result.data?.get("username") as String?

                binding.editName.text = name
                binding.editSurname.text = surname
                binding.editTextMail.text = mailAddress
                binding.editusername.text = userName
                binding.editTextSifre.text = password

                */

            }
        */
        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                val image = it.data!!.getValue("profilePicture")
                Log.e("TAG",image.toString())
                Picasso.get().load(image.toString()).resize(285,215).into(binding.imageViewProfile)
                val data : HashMap<String, UserInfo> = it.data!!.getValue("UserInfo") as HashMap<String, UserInfo>
                binding.editName.text = applicationContext.getString(R.string.name)+ " : "+data.get("name").toString()
                binding.editSurname.text = applicationContext.getString(R.string.surname)+ " : " +data.get("surname").toString()
                binding.editusername.text = applicationContext.getString(R.string.username)+ " : " +data.get("userName").toString()
                binding.editTextMail.text = applicationContext.getString(R.string.mail)+ " : " +data.get("mail").toString()
                binding.editTextSifre.text = data.get("password").toString()


            }
    }
}