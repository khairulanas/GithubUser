package com.lolign.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imgPhoto:ImageView = findViewById(R.id.img_photo)
        val txtUsername:TextView = findViewById(R.id.txt_username)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        imgPhoto.setImageResource(user.avatar!!)
        txtUsername.text = user.username
    }
}