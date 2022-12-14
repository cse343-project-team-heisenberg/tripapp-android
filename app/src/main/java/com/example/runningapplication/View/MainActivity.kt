package com.example.runningapplication.View

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.runningapplication.Model.UserAllData
import com.example.runningapplication.Model.UserInfo
import com.example.runningapplication.Register
import com.example.runningapplication.ShareActivity
import com.example.runningapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGiris.setOnClickListener {
            val mail = "girayyagmur5858@gmail.com"
            val password = "123456"
            if (mail.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(mail,password).addOnSuccessListener {
                    if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                        Toast.makeText(this,"Başarılı", Toast.LENGTH_LONG).show()

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
                            map.put("UserInfo",userInfo)
                            boolean.edit().putBoolean("isItSend",true).apply()
                            Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
                                .set(map)
                                .addOnCompleteListener {

                                }
                        }
                        Firebase.firestore.collection("Post").document(Firebase.auth.currentUser!!.uid)
                            .get().addOnCompleteListener {
                                val data = it.result.toObject<UserAllData>()
                                if(data?.profilePicture==null){
                                    val intent = Intent(applicationContext, ProfilePictureActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else{
                                    val intent = Intent(applicationContext, ShareActivity::class.java)
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