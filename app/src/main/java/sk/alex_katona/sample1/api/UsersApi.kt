package sk.alex_katona.sample1.api

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sk.alex_katona.sample1.api.models.user_details.UserDetail
import sk.alex_katona.sample1.api.models.user_list.UserList
import java.util.concurrent.TimeUnit

interface UsersApi {

    @GET("/api/users")
    fun getUserList(@Query("page") page: Int, @Query("per_page") perPage: Int): Single<UserList>

    @GET("/api/users/{userId}")
    fun getUserDetails(@Path("userId") userId: Int): Single<UserDetail>

    companion object {
        fun newInstance(): UsersApi {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
            return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl("https://reqres.in")
                .build()
                .create(UsersApi::class.java)
        }
    }

}