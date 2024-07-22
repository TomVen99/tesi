package com.example.outdoorromagna.data.database

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LatLngListConverter {

    @TypeConverter
    fun fromLatLngList(latLngList: List<LatLng>?): String? {
        if (latLngList == null) return null
        val gson = Gson()
        val type = object : TypeToken<List<LatLng>>() {}.type
        return gson.toJson(latLngList, type)
    }

    @TypeConverter
    fun toLatLngList(latLngListString: String?): List<LatLng>? {
        if (latLngListString == null) return null
        val gson = Gson()
        val type = object : TypeToken<List<LatLng>>() {}.type
        return gson.fromJson(latLngListString, type)
    }
}