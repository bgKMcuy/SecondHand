package id.finalproject.binar.secondhand.model.network.response


import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class GetUserItem(
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("id")
    @PrimaryKey val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)