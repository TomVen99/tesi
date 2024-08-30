package com.example.smartlagoon.data.repositories

import com.example.smartlagoon.data.database.User_old
import com.example.smartlagoon.data.database.UsersDAO
import kotlinx.coroutines.flow.Flow

class UsersRepository(private val usersDAO: UsersDAO) {
    /*val users: Flow<List<User_old>> = usersDAO.getAllUser()
    val usersRanking: Flow<List<User_old>> = usersDAO.getAllUserRanking()

    suspend fun upsert(userOld: User_old) = usersDAO.upsertUser(userOld)

    suspend fun delete(userOld: User_old) = usersDAO.deleteUser(userOld)

    fun getUser(user: String) = usersDAO.getUser(user)

    fun getUserPoints(username: String) = usersDAO.getUserPoints(username)

    suspend fun addPoints(username: String, points: Int) = usersDAO.addPoints(username, points)

    suspend fun updateProfileImg(username: String, profileImg: String) {
        usersDAO.updateProfileImg(username, profileImg)
    }*/
}


