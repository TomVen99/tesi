package com.example.smartlagoon.data.repositories

import com.example.smartlagoon.data.database.ChallengesDAO

class ChallengeRepository(
    private val challengesDAO: ChallengesDAO,
) {/*
    var challenges: Flow<List<Challenge>> = challengesDAO.getAllChallenges()

    suspend fun getUncompletedChallengesForUser(username: String) = challengesDAO.getUncompletedChallengesForUser(username)

    fun getAllChallenges() = challengesDAO.getAllChallenges()

    suspend fun insertChallenge(challenge: Challenge) = challengesDAO.insertChallenge(challenge)

    /*suspend fun deleteOldPhoto(cutoff: Long) = photoDAO.deleteOldPhotos(cutoff)

    fun getAllPhotos() = photoDAO.getAllPhotos()

    suspend fun getUserPhotos(user: String) = photoDAO.getUserPhotos(user)

    suspend fun getUserPhotoNumber(username: String) = photoDAO.getUserPhotoNumber(username)*/

    suspend fun generateChallengeTest() {

        insertChallenge(Challenge(
            title = "Sfida 1",
            description = "Descrizione sfida 1",
            points = 10
        ))
        insertChallenge(Challenge(
            title = "Sfida 2",
            description = "Descrizione sfida 2",
            points = 20
        ))
        insertChallenge(Challenge(
            title = "Sfida 3",
            description = "Descrizione sfida 3",
            points = 30
        ))
        insertChallenge(Challenge(
            title = "Sfida 4",
            description = "Descrizione sfida 4",
            points = 40
        ))
    }*/

}
