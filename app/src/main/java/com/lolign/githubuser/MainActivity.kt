package com.lolign.githubuser

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var dataUsername:Array<String>
    private lateinit var dataName:Array<String>
    private lateinit var dataPhoto:TypedArray

    private lateinit var dataCompany:Array<String>
    private lateinit var dataLocation:Array<String>
    private lateinit var dataRepository:Array<String>
    private lateinit var dataFollowers:Array<String>
    private lateinit var dataFollowing:Array<String>
    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView:ListView = findViewById(R.id.lv_list)
        adapter = UserAdapter(this)
        listView.adapter = adapter

        prepare()
        addItem()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, users[position].name, Toast.LENGTH_SHORT).show()
            val detailIntent = Intent(this,DetailActivity::class.java)
            detailIntent.putExtra(DetailActivity.EXTRA_USER, users[position])
            startActivity(detailIntent)
        }
    }

    private fun addItem() {
        for (position in dataUsername.indices){
            val user = User(
                dataUsername[position],
                dataName[position],
                dataPhoto.getResourceId(position,-1),
                dataCompany[position],
                dataLocation[position],
                dataRepository[position],
                dataFollowers[position],
                dataFollowing[position],
            )
            users.add(user)
        }
        adapter.users = users
    }

    private fun prepare() {
        dataUsername = resources.getStringArray(R.array.username)
        dataName = resources.getStringArray(R.array.name)
        dataPhoto = resources.obtainTypedArray(R.array.avatar)
        dataCompany = resources.getStringArray(R.array.company)
        dataLocation= resources.getStringArray(R.array.location)
        dataRepository= resources.getStringArray(R.array.repository)
        dataFollowers= resources.getStringArray(R.array.followers)
        dataFollowing= resources.getStringArray(R.array.following)
    }
}