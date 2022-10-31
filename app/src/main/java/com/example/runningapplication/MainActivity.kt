package com.example.runningapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.runningapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

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