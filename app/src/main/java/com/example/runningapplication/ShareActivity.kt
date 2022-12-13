package com.example.runningapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.runningapplication.View.PostActivity
import com.example.runningapplication.databinding.ActivityShareBinding
import nl.joery.animatedbottombar.AnimatedBottomBar

class ShareActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShareBinding
    private lateinit var temp : Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_tutucu,HomeFragment())
            .commit()

        binding.floatActionButtonPost.setOnClickListener {
            val intent = Intent(applicationContext, PostActivity::class.java)
            startActivity(intent)
        }
        binding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {

                if(newIndex != lastIndex){
                    Toast.makeText(applicationContext,"Burada ${lastIndex}",Toast.LENGTH_LONG).show()
                    when (newIndex){
                        0 -> {
                            temp = HomeFragment()

                        }

                        1 -> {
                            temp = DiscoverFragment()

                        }
                        2 -> {
                            temp = MapFragment()

                        }
                        3 -> {
                            temp = NotificationFragment()

                        }
                        4 -> {
                            temp = ProfileFragment()

                        }
                        else -> {

                        }

                    }

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_tutucu,temp)
                        .commit()
                }


            }

            // An optional method that will be fired whenever an already selected tab has been selected again.
            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }
        })
    }
}