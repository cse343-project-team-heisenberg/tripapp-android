package com.example.runningapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.runningapplication.Model.UserInfo
import com.example.runningapplication.databinding.ActivityEditProfileUserBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditProfileUser : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var flag = false
        val map = HashMap<String,Any>()
        val sharedPreferences = getSharedPreferences("com.example.runningapplication", Context.MODE_PRIVATE)
       val userInfo = UserInfo("","","","","","")
       userInfo.name = sharedPreferences.getString("name"," ")!!
       userInfo.surname = sharedPreferences.getString("surname"," ")!!
       userInfo.userName = sharedPreferences.getString("username"," ")!!
       userInfo.mail = sharedPreferences.getString("mail","")!!
       userInfo.password = sharedPreferences.getString("password","")!!
       userInfo.uuid = Firebase.auth.currentUser!!.uid.toString()

        binding.buttonEdit.setOnClickListener {
            val name = binding.editName.text.toString().trim()
            val surname = binding.editSurname.text.toString().trim()
            val username = binding.editusername.text.toString().trim()
            val mailAddress = binding.editTextMail.text.toString().trim()
            val password = binding.editTextSifre.text.toString().trim()

            if(name.isNullOrEmpty() == false){
                flag = true
                userInfo.name = name
            }
            if(surname.isNullOrEmpty() == false){
                flag = true
                userInfo.surname = surname
            }
            if(username.isNullOrEmpty() == false) {
                flag = true
                userInfo.userName = username
            }
            if(mailAddress.isNullOrEmpty() == false) {
                flag = true
                userInfo.mail = mailAddress
            }
            if(password.isNullOrEmpty()== false){
                flag = true
                userInfo.password = password
            }
            if(flag == true){
                map.put("UserInfo",userInfo)
                Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
                    .update(map).addOnCompleteListener {
                        Toast.makeText(applicationContext,"Güncellendi",Toast.LENGTH_LONG).show()
                    }
            }
            if(password.isNullOrEmpty() == true) {
                Firebase.auth.currentUser!!.updatePassword(password).addOnCompleteListener {
                    Toast.makeText(applicationContext,"Şifreniz Onaylandı",Toast.LENGTH_LONG).show()
                }
            }

        }
    }
}