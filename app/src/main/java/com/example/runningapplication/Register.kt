package com.example.runningapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.runningapplication.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

import com.google.firebase.auth.FirebaseAuth
import java.io.IOException

class Register : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private var bitmap:Bitmap ? = null
    private var shared:SharedPreferences ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shared = getSharedPreferences("com.example.runningapplication",Context.MODE_PRIVATE)


            binding.buttonGiris.setOnClickListener {
            val mail = binding.editTextMail.text.toString().trim()
                /*val mail = "m.cagri0205@gmail.com"
                val name ="das"
                val surname = "binding.editSurname.text.toString().trim()"
                val password = "123456"
                val againpassword = "123456"
                */
                val name = binding.editName.text.toString().trim()
                val surname = binding.editSurname.text.toString().trim()
                val username = binding.editusername.text.toString().trim()
                val password = binding.editTextSifre.text.toString().trim()
                val againpassword = binding.editagainTextSifre.text.toString().trim()
            if(username.isNotEmpty() && mail.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty() && password==againpassword ){
                shared?.let {
                    it.edit().putString("name",name).apply()
                    it.edit().putString("surname",surname).apply()
                    it.edit().putString("password",password).apply()
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,password).addOnSuccessListener {
                        Toast.makeText(this,"Onay Kodu Gönderildi", Toast.LENGTH_LONG).show()
                        mailGonder()


                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(this@Register, "Hatalı giriş denemesi", Toast.LENGTH_SHORT).show()
            }

        }
    }



    private fun mailGonder(){
        var kullanici=FirebaseAuth.getInstance().currentUser
        if (kullanici != null){
            kullanici.sendEmailVerification()
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        if(p0.isSuccessful){
                            Toast.makeText(this@Register,"Mailinizi kontrol edin, mailinizi onaylayın", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@Register,MainActivity::class.java)
                           bitmap?.let {

                               intent.putExtra("flag",true)
                           }
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@Register,"Mail gönderilirken sorun oluştu "+p0.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        }
    }
}