package sk.alex_katona.sample1.api.models.user_list
import com.google.gson.annotations.SerializedName

data class UserList(
    @SerializedName("ad")
    val ad: Ad,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)