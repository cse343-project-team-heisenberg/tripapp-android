package com.example.runningapplication

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
        //val intent = Intent(applicationContext, splash_screen::class.java)
        //startActivity(intent)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonGiris.setOnClickListener {
            val mail = binding.editTextMail.text.toString()
            val password = binding.editTextSifre.text.toString()
            if (mail.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(mail,password).addOnSuccessListener {
                    if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                        Toast.makeText(this,"Başarılı", Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext, ProfileActivity::class.java)
                        startActivity(intent)
                        finish()
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