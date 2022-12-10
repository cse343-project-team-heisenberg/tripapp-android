package com.example.runningapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.runningapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGiris.setOnClickListener {
            val mail = "m.yilmaz2019@gtu.edu.tr"
            val password = "123456"
            if (mail.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(mail,password).addOnSuccessListener {
                    if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                        Toast.makeText(this,"Başarılı", Toast.LENGTH_LONG).show()
                        //Intent içindeki activity classı değiştirilmesi gerekiyor.
                        val boolean = getSharedPreferences("com.example.runningapplication",
                            Context.MODE_PRIVATE)

                        if(boolean.getBoolean("isItSend",false) == false) {
                            val name = boolean.getString("name","")
                            val surname = boolean.getString("surname","")
                            val password = boolean.getString("password","")
                            val username = boolean.getString("username","")
                            val mail = boolean.getString("mail","")
                            val map = HashMap<String,Any>()
                            val userInfo = UserInfo(
                                name.toString(), surname.toString(), username.toString(),
                                mail.toString(),
                                password.toString(),Firebase.auth.currentUser!!.uid.toString())
                           // map.put("name",name!!)
                           // map.put("surname",surname!!)
                           // map.put("password",password!!)
                           // map.put("username",username!!)
                           // map.put("mail",mail!!)
                            map.put("UserInfo",userInfo)
                            boolean.edit().putBoolean("isItSend",true).apply()
                            Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
                                .set(map)
                                .addOnCompleteListener {
                                    Toast.makeText(applicationContext,"Burada",Toast.LENGTH_LONG).show()
                                }
                        }
                        val data = boolean.getBoolean("profilePicture",false)
                        if (boolean!=null){
                            if (data) {
                                val intent = Intent(applicationContext,ShareActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else {
                                val intent = Intent(applicationContext,ProfilePictureActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.buttonKayitOl.setOnClickListener {
            Toast.makeText(this, "Kayıt ekranına yönlendiriliyor", Toast.LENGTH_LONG).show()
            val intent = Intent(applicationContext, Register::class.java)
            startActivity(intent)
        }
    }
}