package dev.rushia.githubusers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.rushia.githubusers.data.response.UserResultItem
import dev.rushia.githubusers.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<ArrayList<UserResultItem>>()
    val listUser: LiveData<ArrayList<UserResultItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun getUsers() {
        _isLoading.value = true
        _listUser.value = arrayListOf()
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<ArrayList<UserResultItem>> {
            override fun onResponse(
                call: Call<ArrayList<UserResultItem>>, response: Response<ArrayList<UserResultItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()
                } else {
                    Log.e("ERROR", response.message().toString())
                }

            }

            override fun onFailure(call: Call<ArrayList<UserResultItem>>, t: Throwable) {
                Log.e("ERROR onFailure()", t.message.toString())
            }

        })
    }

    init {
        getUsers()
    }
}