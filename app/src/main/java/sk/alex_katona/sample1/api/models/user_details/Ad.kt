package sk.alex_katona.sample1.api.models.user_details


import com.google.gson.annotations.SerializedName

data class Ad(
    @SerializedName("company")
    val company: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("url")
    val url: String
)