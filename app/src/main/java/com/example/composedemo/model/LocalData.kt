package com.example.composedemo.model


import android.net.Uri
import androidx.room.Entity
import com.example.composedemo.utils.JsonNavType
import com.google.gson.Gson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "localRepo", primaryKeys = ["id"])
data class LocalData(
    @SerialName("image")
    val image: String="",
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String="",
    @SerialName("projectLink")
    val projectLink: String ="",
    @SerialName("description")
    val description: String="",
    @SerialName("contributors")
    val contributors: String="",
    )
{
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}


class ItemArgsType : JsonNavType<LocalData>() {
    override fun fromJsonParse(value: String): LocalData = Gson().fromJson(value, LocalData::class.java)
    override fun LocalData.getJsonParse(): String = Gson().toJson(this)
}