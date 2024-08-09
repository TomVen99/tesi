package com.example.smartlagoon.data.repositories

import com.example.smartlagoon.data.database.UserChallenge
import com.example.smartlagoon.data.database.UserChallengeDAO

class UserChallengeRepository(
    private val userChallengesDAO: UserChallengeDAO,
) {
    suspend fun insertChallengeDone(userChallenge: UserChallenge) = userChallengesDAO.insertChallengeDone(userChallenge)

    suspend fun insertChallengeTest() {
        insertChallengeDone(UserChallenge("a", 1))
    }
}
