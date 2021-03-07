package com.lolign.githubuser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolign.githubuser.databinding.FragmentFollowingBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_USERNAME = "username"
        @JvmStatic
        fun newInstance(index: Int,username:String?) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_USERNAME,username)
                }
            }
    }



    private val list = ArrayList<User>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME)
        getListFollowingFollowers(index,username)

    }

    private fun getListFollowingFollowers(index: Int?,username: String?){
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 5a57c5f1403b2d255d46af7a99f38981aa1e07c0")
        client.addHeader("User-Agent", "request")

        val url = if(index==1){
            "https://api.github.com/users/${username}/followers"
        }else{
            "https://api.github.com/users/${username}/following"
        }
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int,headers: Array<out Header>?,responseBody: ByteArray) {
                val result = String(responseBody)

                try {
                    val responseArray = JSONArray(result)
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

                }catch (e: Exception){}

            }

            override fun onFailure(statusCode: Int,headers: Array<out Header>?,responseBody: ByteArray?,error: Throwable?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showRecyclerList() {
        binding.rvUsers.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListUserAdapter(list)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }
    private fun showSelectedUser(user: User) {
        val detailIntent = Intent(activity,DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(detailIntent)
    }

}