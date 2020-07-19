package sk.alex_katona.sample1.api.models.user_details


import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("ad")
    val ad: Ad,
    @SerializedName("data")
    val `data`: Data
)