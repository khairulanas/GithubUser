package com.lolign.githubuser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private lateinit var txtName:TextView
    private lateinit var txtCompany:TextView
    private lateinit var txtLocation:TextView
    private lateinit var txtRepo:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imgPhoto:CircleImageView = findViewById(R.id.img_photo)
        val txtUsername:TextView = findViewById(R.id.txt_username)
        txtName = findViewById(R.id.txt_name)
        txtCompany = findViewById(R.id.txt_company)
        txtLocation = findViewById(R.id.txt_location)
        txtRepo = findViewById(R.id.txt_repo)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        Glide.with(this).load(user.avatar).into(imgPhoto)
        txtUsername.text = user.username

        getUserDetail(user.username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this,user.username)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun getUserDetail(username:String?){
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "ea31f1842de819a73fdad4a6207fccfa1638ef2a")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${username}"
        client.get(url, object : AsyncHttpResponseHandler(){
            @SuppressLint("SetTextI18n")
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    txtName.text = responseObject.getString("name")
                    txtLocation.text = responseObject.getString("location")
                    txtCompany.text = responseObject.getString("company")
                    txtRepo.text = responseObject.getString("public_repos")+" Repository"

                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int,headers: Array<out Header>?,responseBody: ByteArray,error: Throwable) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }

                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}