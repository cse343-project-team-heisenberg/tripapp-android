package com.example.runningapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.runningapplication.databinding.ActivityMainBinding
import com.example.runningapplication.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonGiris.setOnClickListener {
            val mail = binding.editTextMail.text.toString().trim()
            val name = binding.editName.text.toString().trim()
            val surname = binding.editSurname.text.toString().trim()
            val password = binding.editTextSifre.text.toString().trim()
            val againpassword = binding.editagainTextSifre.text.toString().trim()
            if(mail.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty() && password==againpassword ){
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
                        }else{
                            Toast.makeText(this@Register,"Mail gönderilirken sorun oluştu "+p0.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        }
    }
}