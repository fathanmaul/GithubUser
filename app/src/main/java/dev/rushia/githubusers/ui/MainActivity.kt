package dev.rushia.githubusers.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.rushia.githubusers.data.response.UserResultItem
import dev.rushia.githubusers.data.response.UserSearchResultItem
import dev.rushia.githubusers.data.retrofit.ApiConfig
import dev.rushia.githubusers.databinding.ActivityMainBinding
import dev.rushia.githubusers.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchBar.setText(searchView.text)
                searchView.hide()
                getSearchUsers(searchBar.text.toString())
                false
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        viewModel.listUser.observe(this) {
            setUsersData(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }


    private fun getSearchUsers(query: String?) {
        if (query.isNullOrEmpty()) {
            viewModel.listUser.observe(this) {
                setUsersData(it)
            }
        } else {
            showLoading(true)
            binding.rvUser.visibility = View.INVISIBLE
            val client = ApiConfig.getApiService().getSearchUsers(query)
            client.enqueue(object : Callback<UserSearchResultItem> {
                override fun onResponse(
                    call: Call<UserSearchResultItem>, response: Response<UserSearchResultItem>
                ) {
                    showLoading(false)
                    binding.rvUser.visibility = View.VISIBLE
                    if (response.isSuccessful) {
                        val users = response.body()?.items
                        if (users != null) {
                            setUsersData(users)
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to get data", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<UserSearchResultItem>, t: Throwable) {
                    Log.e("Failure", t.message.toString())
                }

            })
        }
    }


    private fun setUsersData(users: ArrayList<UserResultItem>) {
        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvUser.adapter = adapter
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}