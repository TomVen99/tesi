package com.example.smartlagoon.data.repositories

import com.example.smartlagoon.data.database.User
import com.example.smartlagoon.data.database.UsersDAO
import kotlinx.coroutines.flow.Flow

class UsersRepository(private val usersDAO: UsersDAO) {
    val users: Flow<List<User>> = usersDAO.getAllUser()
    val usersRanking: Flow<List<User>> = usersDAO.getAllUserRanking()

    suspend fun upsert(user: User) = usersDAO.upsertUser(user)

    suspend fun delete(user: User) = usersDAO.deleteUser(user)

    fun getUser(user: String) = usersDAO.getUser(user)

    fun getUserPoints(username: String) = usersDAO.getUserPoints(username)

    suspend fun addPoints(username: String, points: Int) = usersDAO.addPoints(username, points)

    suspend fun updateProfileImg(username: String, profileImg: String) {
        usersDAO.updateProfileImg(username, profileImg)
    }
}


