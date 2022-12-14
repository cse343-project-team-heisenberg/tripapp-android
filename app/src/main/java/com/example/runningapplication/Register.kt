package com.example.runningapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.runningapplication.View.MainActivity
import com.example.runningapplication.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private var bitmap:Bitmap ? = null
    private var shared:SharedPreferences ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shared = getSharedPreferences("com.example.runningapplication",Context.MODE_PRIVATE)
        shared!!.edit().remove("name").apply()
        shared!!.edit().remove("surname").apply()
        shared!!.edit().remove("password").apply()
        shared!!.edit().remove("mail").apply()
        shared!!.edit().remove("username").apply()
        shared!!.edit().putBoolean("isItSend",false).apply()
            binding.buttonGiris.setOnClickListener {
                val mail = binding.editTextMail.text.toString().trim()
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
                    it.edit().putString("mail",mail).apply()
                    it.edit().putString("username",username).apply()
                }
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,password).addOnSuccessListener {
                        Toast.makeText(this,"Onay Kodu G??nderildi", Toast.LENGTH_LONG).show()
                        mailGonder()
                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this@Register, "Hatal?? giri?? denemesi", Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(this@Register,"Mailinizi kontrol edin, mailinizi onaylay??n", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@Register, MainActivity::class.java)
                           bitmap?.let {

                               intent.putExtra("flag",true)
                           }
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@Register,"Mail g??nderilirken sorun olu??tu "+p0.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        }
    }
}