package com.example.smartlagoon.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDAO {
    /*@Query("SELECT * FROM user ORDER BY username ASC")
    fun getAllUser(): Flow<List<User_old>>

    @Query("SELECT * FROM user WHERE username = :user")
    fun getUser(user: String): Flow<User_old?>

    @Query("SELECT points FROM user WHERE username = :username")
    fun getUserPoints(username: String): Int

    @Query("SELECT * FROM user ORDER BY points DESC")
    fun getAllUserRanking() : Flow<List<User_old>>

    @Query("UPDATE user SET points = points + :points WHERE username = :username")
    suspend fun addPoints(username: String, points: Int)

    @Query("UPDATE user SET urlProfilePicture = :profileImg WHERE username = :username")
    suspend fun updateProfileImg(username: String, profileImg: String)

    @Upsert
    suspend fun upsertUser(userOld: User_old)

    @Delete
    suspend fun deleteUser(item: User_old)*/

}

@Dao
interface ChallengesDAO {
    /*@Query(
        """
        SELECT * FROM Challenge_old 
        WHERE id NOT IN (
            SELECT challengeId FROM UserChallenge WHERE username = :username
        )
    """
    )
    suspend fun getUncompletedChallengesForUser(username: String): List<Challenge_old>

    @Query("SELECT * FROM Challenge_old")
    fun getAllChallenges(): Flow<List<Challenge_old>>

    @Insert
    suspend fun insertChallenge(challenge: Challenge_old)*/
}

@Dao
interface UserChallengeDAO{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChallengeDone(userChallenge: UserChallenge)
}

@Dao
interface PhotoDAO {
    @Upsert
    suspend fun upsertPhoto(photoOld: Photo_old)

    @Query("SELECT * FROM Photo_old order by timestamp desc")
    fun getAllPhotos(): Flow<List<Photo_old>>

    @Query("SELECT * FROM Photo_old WHERE username = :user ")
    suspend fun getUserPhotos(user: String): List<Photo_old>

    @Query("DELETE FROM Photo_old WHERE timestamp < :cutoff")
    fun deleteOldPhotos(cutoff: Long)

    @Query("SELECT count(*) AS numeroFoto FROM Photo_old p WHERE p.username =:username ")
    suspend fun getUserPhotoNumber(username: String): Int
}

