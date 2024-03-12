package dev.rushia.githubusers.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.rushia.githubusers.data.response.UserDetailResult
import dev.rushia.githubusers.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _dataUser = MutableLiveData<UserDetailResult>()
    val dataUser = _dataUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading


    fun getDetailUser(username: String) {
        _isLoading.value = true
        _dataUser.value = UserDetailResult()
        val client = ApiConfig.getApiService().getDetailUser(username) //Perlu parameter Username
        client.enqueue(object : Callback<UserDetailResult> {
            override fun onResponse(
                call: Call<UserDetailResult>, response: Response<UserDetailResult>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _dataUser.value = response.body()
                } else {
                    Log.e("onResponse Detail", response.message().toString())
                }
            }

            override fun onFailure(call: Call<UserDetailResult>, t: Throwable) {
                Log.e("onFailure Detail", t.message.toString())
            }
        })
    }

}