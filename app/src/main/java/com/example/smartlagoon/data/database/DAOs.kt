package com.example.outdoorromagna.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDAO {
    @Query("SELECT * FROM user ORDER BY username ASC")
    fun getAllUser(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE username = :user")
    fun getUser(user: String): Flow<User?>

    /*@Query("SELECT * FROM Track WHERE start = :latitude AND longitude = :longitude")
    fun getTrackByLatLng(latitude: Float, longitude: Float): Flow<Track?>*/

    /*@Query("SELECT * FROM favorite WHERE userId = :userID AND markerId = :markerId")
    fun getFavorite(userID : Int, markerId : Int) : Flow<Favorite?>*/

    @Query("UPDATE user SET urlProfilePicture = :profileImg WHERE username = :username")
    suspend fun updateProfileImg(username: String, profileImg: String)

    /*@Query("SELECT * FROM place ORDER BY name ASC")
    fun getAllPlaces(): Flow<List<Place>>*/
    @Upsert
    suspend fun upsertUser(user: User)

    @Delete
    suspend fun deleteUser(item: User)

}

@Dao
interface FavouritesDAO {
    @Upsert
    suspend fun upsertFavourite(favourite: Favourite)

    @Delete
    suspend fun deteleFavourite(favourite: Favourite)

    @Query("SELECT trackId FROM FAVOURITE WHERE userId = :userId")
    suspend fun getFavouritesByUser(userId: Int): List<Int>
}


@Dao
interface TracksDAO {
    @Upsert
    suspend fun upsertTrack(track: Track)

    @Query("SELECT * FROM Track ")
    fun getAllTracks(): Flow<List<Track>>

    @Query("SELECT * FROM Track WHERE userId = :id ")
    suspend fun getUserTracks(id: Int): List<Track>

    @Query("SELECT * FROM Track WHERE id in (SELECT trackId FROM favourite WHERE userId = :userId)")
    suspend fun getFavoriteTracks(userId: Int): List<Track>

    @Query("""
    SELECT 
        MIN(startLat) + (MAX(startLat) - MIN(startLat)) / 2 AS groupedLat, 
        MIN(startLng) + (MAX(startLng) - MIN(startLng)) / 2 AS groupedLng
    FROM 
        track
    GROUP BY 
        CAST(startLat * 100 AS INTEGER), CAST(startLng * 100 AS INTEGER)
    """)
    fun getGroupedTracks(): Flow<List<GroupedTrack>>

    @Query("""
    SELECT 
        *
    FROM 
        track
    WHERE 
        CAST(startLat * 100 AS INTEGER) = CAST(:startLat * 100 AS INTEGER)
        AND CAST(startLng * 100 AS INTEGER) = CAST(:startLng * 100 AS INTEGER)
    """)
    suspend fun getTracksInRange(startLat: Double, startLng: Double): List<Track>

    @Delete
    suspend fun deleteTrack(track: Track)

    @Query("SELECT count(*) AS numeroPercorsi FROM Track t WHERE t.userId =:userId ")
    suspend fun getUserTracksNumber(userId: Int): Int
}

@Dao
/*interface ActivityDAO {
    @Query("SELECT * FROM activity")
    fun getActivities(): Flow<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(activity: Activity)

    @Update
    fun updateActivity(activity: Activity)

    @Delete
    suspend fun delete(activity: Activity)

    @Query("DELETE FROM activity")
    suspend fun deleteAll()

    @Query("UPDATE activity SET name = :name WHERE activityId = :activityId")
    suspend fun updateActivityName(activityId: String, name: String)

    @Query("UPDATE activity SET favourite = :favourite WHERE activityId = :activityId")
    suspend fun updateActivityFavourite(activityId: String, favourite: Boolean)

    @Transaction
    @Query("SELECT * FROM activity WHERE userCreatorUsername = :usernameSelected")
    fun getActivitiesFromUser(usernameSelected: String): Flow<List<Activity>>

    @Transaction
    @Query("SELECT * FROM activity WHERE userCreatorUsername = :usernameSelected and favourite = true")
    fun getFavouriteActivitiesFromUser(usernameSelected: String): Flow<List<Activity>>

}*/

data class GroupedTrack(
    val groupedLat: Double,
    val groupedLng: Double
)
