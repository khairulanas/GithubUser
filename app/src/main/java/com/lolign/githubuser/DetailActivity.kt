package com.lolign.githubuser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imgPhoto:CircleImageView = findViewById(R.id.img_photo)
        val txtUsername:TextView = findViewById(R.id.txt_username)
        val txtName:TextView = findViewById(R.id.txt_name)
        val txtCompany:TextView = findViewById(R.id.txt_company)
        val txtLocation:TextView = findViewById(R.id.txt_location)
        val txtRepository:TextView = findViewById(R.id.txt_repository)
        val txtFollowers:TextView = findViewById(R.id.txt_followers)
        val txtFollowing:TextView = findViewById(R.id.txt_Following)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        Glide.with(this).load(user.avatar).into(imgPhoto)
        txtUsername.text =": "+ user.username
        txtName.text =": "+ user.id

    }
}