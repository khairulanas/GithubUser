package com.lolign.githubuser


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolign.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUsers.setHasFixedSize(true)

        list.addAll(getListUsers())
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /*
            Gunakan method ini ketika search selesai atau OK
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            /*
            Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
             */
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun showSelectedUser(user: User) {
        Toast.makeText(this, "${user.name}", Toast.LENGTH_SHORT).show()
        val detailIntent = Intent(this,DetailActivity::class.java)
            detailIntent.putExtra(DetailActivity.EXTRA_USER, user)
            startActivity(detailIntent)
    }

    private fun getListUsers(): ArrayList<User> {
        val dataUsername = resources.getStringArray(R.array.username)
        val dataName = resources.getStringArray(R.array.name)
        val dataPhoto = resources.getStringArray(R.array.avatar)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataLocation= resources.getStringArray(R.array.location)
        val dataRepository= resources.getStringArray(R.array.repository)
        val dataFollowers= resources.getStringArray(R.array.followers)
        val dataFollowing= resources.getStringArray(R.array.following)

        val listUser = ArrayList<User>()
        for (position in dataUsername.indices) {
            val user = User(
                dataUsername[position],
                dataName[position],
                dataPhoto[position],
                dataCompany[position],
                dataLocation[position],
                dataRepository[position],
                dataFollowers[position],
                dataFollowing[position],
            )
            listUser.add(user)
        }
        return listUser
    }

    private fun showRecyclerList() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }
}