package com.lolign.githubuser


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolign.githubuser.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUsers.setHasFixedSize(true)

        binding.progressBar.visibility = View.INVISIBLE
        binding.tvNotFound.visibility = View.INVISIBLE

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
                getListUsers(query)
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
        val detailIntent = Intent(this,DetailActivity::class.java)
            detailIntent.putExtra(DetailActivity.EXTRA_USER, user)
            startActivity(detailIntent)
    }

    private fun getListUsers(query:String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvNotFound.visibility = View.INVISIBLE
        binding.tvStart.visibility = View.INVISIBLE

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 43a5b98e1ff1e93a4db19a4e16714b37f667f9c0")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=${query}"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                binding.progressBar.visibility = View.INVISIBLE



                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val responseObject = JSONObject(result)
                    if(responseObject.getInt("total_count") == 0){
                        binding.rvUsers.visibility = View.INVISIBLE
                        binding.tvNotFound.visibility = View.VISIBLE
                    }else{

                        val responseArray = responseObject.optJSONArray("items")
                        val listUser = ArrayList<User>()

                        for (i in 0 until responseArray.length()) {
                            val jsonObject = responseArray.getJSONObject(i)
                            val dataUsername = jsonObject.getString("login")
                            val dataId = jsonObject.getInt("id")
                            val dataPhoto = jsonObject.getString("avatar_url")
                            listUser.add(User(
                                    dataUsername,
                                    dataId,
                                    dataPhoto
                            ))
                        }

                        list.clear()
                        list.addAll(listUser)
                        showRecyclerList()
                        binding.rvUsers.visibility = View.VISIBLE

                    }

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray, error: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                val result = String(responseBody)
                Log.d(TAG, result)
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })


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