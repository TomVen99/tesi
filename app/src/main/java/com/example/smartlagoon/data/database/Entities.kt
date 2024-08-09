package com.example.smartlagoon.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var username: String,

    @ColumnInfo
    var password: String,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var surname: String,

    @ColumnInfo
    var mail: String,

    @ColumnInfo
    var urlProfilePicture : String?,

    @ColumnInfo
    var points : Int = 0,

    @ColumnInfo
    var salt : ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (urlProfilePicture != other.urlProfilePicture) return false
        return salt.contentEquals(other.salt)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + urlProfilePicture.hashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}

@Entity
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val imageUri: String?,

    @ColumnInfo
    val username: String,

    @ColumnInfo
    val timestamp: Long,
)

@Entity(primaryKeys = ["username", "challengeId"])
data class UserChallenge (

    val username: String,

    val challengeId: Int
)

@Entity
data class Challenge (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val title: String,

    @ColumnInfo
    val description: String,
)
