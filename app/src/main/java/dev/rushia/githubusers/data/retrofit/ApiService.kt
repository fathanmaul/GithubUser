package dev.rushia.githubusers.data.retrofit

import dev.rushia.githubusers.BuildConfig
import dev.rushia.githubusers.data.response.UserDetailResult
import dev.rushia.githubusers.data.response.UserResultItem
import dev.rushia.githubusers.data.response.UserSearchResultItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    @GET("users")
    fun getUsers(): Call<ArrayList<UserResultItem>>

    @GET("search/users")
    fun getSearchUsers(
        @Query("q") q: String
    ): Call<UserSearchResultItem>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetailResult>

    @GET("users/{username}/followers")
    fun getFollowersUser(
        @Path("username") username: String
    ): Call<ArrayList<UserResultItem>>

    @GET("users/{username}/following")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<ArrayList<UserResultItem>>
}