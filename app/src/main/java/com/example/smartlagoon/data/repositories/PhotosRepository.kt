package com.example.smartlagoon.data.repositories

import android.content.ContentResolver
import android.net.Uri
import com.example.camera.utils.saveImageToStorage
import com.example.smartlagoon.data.database.Photo_old
import com.example.smartlagoon.data.database.PhotoDAO
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotosRepository(
    private val photoDAO: PhotoDAO,
    private val contentResolver: ContentResolver
) {
    var photos: Flow<List<Photo_old>> = photoDAO.getAllPhotos()

    suspend fun upsertPhoto(photoOld: Photo_old) {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Smartlagoon_Photo_$timeStamp"
        if (photoOld.imageUri?.isNotEmpty() == true) {
            val imageUri = saveImageToStorage(
                Uri.parse(photoOld.imageUri),
                contentResolver,
                fileName
            )
            photoDAO.upsertPhoto(photoOld.copy(imageUri = imageUri.toString()))
        } else {
            photoDAO.upsertPhoto(photoOld)
        }
    }

    //suspend fun delete(track: Track) = tracksDAO.deleteTrack(track)
    suspend fun deleteOldPhoto(cutoff: Long) = photoDAO.deleteOldPhotos(cutoff)

    fun getAllPhotos() = photoDAO.getAllPhotos()

    suspend fun getUserPhotos(user: String) = photoDAO.getUserPhotos(user)

    suspend fun getUserPhotoNumber(username: String) = photoDAO.getUserPhotoNumber(username)

}

/*

/**FUN PER INSERIRE TRACK DI TEST*/
fun generateTestTracks(): List<Track> {
    return listOf(
        Track(
            name = "Trekking Rimini 1",
            description = "Percorso trekking tra le colline di Rimini.",
            city = "Rimini",
            startLat = 44.0600,
            startLng = 12.5653,
            trackPositions = listOf(
                LatLng(44.0600, 12.5653),
                LatLng(44.0610, 12.5660),
                LatLng(44.0620, 12.5670),
                LatLng(44.0630, 12.5680),
                LatLng(44.0640, 12.5690),
                LatLng(44.0650, 12.5700),
                LatLng(44.0660, 12.5710),
                LatLng(44.0670, 12.5720),
                LatLng(44.0680, 12.5730),
                LatLng(44.0690, 12.5740)
            ),
            imageUri = null,
            duration = 1,
            userId = 1,
        ),
        Track(
            name = "Trekking Cesena 1",
            description = "Percorso trekking nei dintorni di Cesena.",
            city = "Cesena",
            startLat = 44.1373,
            startLng = 12.2421,
            trackPositions = listOf(
                LatLng(44.1373, 12.2421),
                LatLng(44.1380, 12.2430),
                LatLng(44.1390, 12.2440),
                LatLng(44.1400, 12.2450),
                LatLng(44.1410, 12.2460),
                LatLng(44.1420, 12.2470),
                LatLng(44.1430, 12.2480),
                LatLng(44.1440, 12.2490),
                LatLng(44.1450, 12.2500),
                LatLng(44.1460, 12.2510)
            ),
            imageUri = null,
            duration = 1,
            userId = 1,
        ),
        Track(
            name = "Trekking Ravenna 1",
            description = "Percorso trekking nelle campagne di Ravenna.",
            city = "Ravenna",
            startLat = 44.4153,
            startLng = 12.1960,
            trackPositions = listOf(
                LatLng(44.4153, 12.1960),
                LatLng(44.4160, 12.1970),
                LatLng(44.4170, 12.1980),
                LatLng(44.4180, 12.1990),
                LatLng(44.4190, 12.2000),
                LatLng(44.4200, 12.2010),
                LatLng(44.4210, 12.2020),
                LatLng(44.4220, 12.2030),
                LatLng(44.4230, 12.2040),
                LatLng(44.4240, 12.2050)
            ),
            imageUri = null,
            duration = 1,
            userId = 1,
        ),
        Track(
            name = "Trekking Rimini 2",
            description = "Un altro percorso trekking tra le colline di Rimini.",
            city = "Rimini",
            startLat = 44.0400,
            startLng = 12.5453,
            trackPositions = listOf(
                LatLng(44.0400, 12.5453),
                LatLng(44.0410, 12.5460),
                LatLng(44.0420, 12.5470),
                LatLng(44.0430, 12.5480),
                LatLng(44.0440, 12.5490),
                LatLng(44.0450, 12.5500),
                LatLng(44.0460, 12.5510),
                LatLng(44.0470, 12.5520),
                LatLng(44.0480, 12.5530),
                LatLng(44.0490, 12.5540)
            ),
            imageUri = null,
            duration = 1,
            userId = 1,
        ),
        Track(
            name = "Trekking Cesena 2",
            description = "Un altro percorso trekking nei dintorni di Cesena.",
            city = "Cesena",
            startLat = 44.1273,
            startLng = 12.2321,
            trackPositions = listOf(
                LatLng(44.1273, 12.2321),
                LatLng(44.1280, 12.2330),
                LatLng(44.1290, 12.2340),
                LatLng(44.1300, 12.2350),
                LatLng(44.1310, 12.2360),
                LatLng(44.1320, 12.2370),
                LatLng(44.1330, 12.2380),
                LatLng(44.1340, 12.2390),
                LatLng(44.1350, 12.2400),
                LatLng(44.1360, 12.2410)
            ),
            imageUri = null,
            duration = 1,
            userId = 1,
        ),
        Track(
            name = "Trekking Ravenna 2",
            description = "Un altro percorso trekking nelle campagne di Ravenna.",
            city = "Ravenna",
            startLat = 44.4053,
            startLng = 12.1860,
            trackPositions = listOf(
                LatLng(44.4053, 12.1860),
                LatLng(44.4060, 12.1870),
                LatLng(44.4070, 12.1880),
                LatLng(44.4080, 12.1890),
                LatLng(44.4090, 12.1900),
                LatLng(44.4100, 12.1910),
                LatLng(44.4110, 12.1920),
                LatLng(44.4120, 12.1930),
                LatLng(44.4130, 12.1940),
                LatLng(44.4140, 12.1950)
            ),
            imageUri = null,
            duration = 1,
            userId = 1,
        ),
        Track(
            name = "Trekking Rimini 3",
            description = "Percorso trekking tra i monti di Rimini.",
            city = "Rimini",
            startLat = 44.0800,
            startLng = 12.5953,
            trackPositions = listOf(
                LatLng(44.0800, 12.5953),
                LatLng(44.0810, 12.5960),
                LatLng(44.0820, 12.5970),
                LatLng(44.0830, 12.5980),
                LatLng(44.0840, 12.5990),
                LatLng(44.0850, 12.6000),
                LatLng(44.0860, 12.6010),
                LatLng(44.0870, 12.6020),
                LatLng(44.0880, 12.6030),
                LatLng(44.0890, 12.6040)
            ),
            imageUri = null,
            duration = 1,
            userId = 4,

        ),
        Track(
            name = "Trekking Cesena 3",
            description = "Percorso trekking in una zona tranquilla di Cesena.",
            city = "Cesena",
            startLat = 44.1473,
            startLng = 12.2521,
            trackPositions = listOf(
                LatLng(44.1473, 12.2521),
                LatLng(44.1480, 12.2530),
                LatLng(44.1490, 12.2540),
                LatLng(44.1500, 12.2550),
                LatLng(44.1510, 12.2560),
                LatLng(44.1520, 12.2570),
                LatLng(44.1530, 12.2580),
                LatLng(44.1540, 12.2590),
                LatLng(44.1550, 12.2600),
                LatLng(44.1560, 12.2610)
            ),
            imageUri = null,
            duration = 1,
            userId = 5,
        ),
        Track(
            name = "Trekking Ravenna 3",
            description = "Percorso trekking tra i boschi di Ravenna.",
            city = "Ravenna",
            startLat = 44.4253,
            startLng = 12.2060,
            trackPositions = listOf(
                LatLng(44.4253, 12.2060),
                LatLng(44.4260, 12.2070),
                LatLng(44.4270, 12.2080),
                LatLng(44.4280, 12.2090),
                LatLng(44.4290, 12.2100),
                LatLng(44.4300, 12.2110),
                LatLng(44.4310, 12.2120),
                LatLng(44.4320, 12.2130),
                LatLng(44.4330, 12.2140),
                LatLng(44.4340, 12.2150)
            ),
            imageUri = null,
            duration = 1,
            userId = 2,
        ),
        Track(
            name = "Trekking Rimini 3",
            description = "Percorso trekking tra i monti di Rimini 2.",
            city = "Rimini",
            startLat = 44.0808,
            startLng = 12.5953,
            trackPositions = listOf(
                LatLng(44.0808, 12.5953),
                LatLng(44.0810, 12.5960),
                LatLng(44.0820, 12.5970),
                LatLng(44.0830, 12.5980),
                LatLng(44.0840, 12.5990),
                LatLng(44.0850, 12.6000),
                LatLng(44.0860, 12.6010),
                LatLng(44.0870, 12.6020),
                LatLng(44.0880, 12.6030),
                LatLng(44.0890, 12.6040)
            ),
            imageUri = null,
            duration = 1,
            userId = 2,
        )
    )
}*/
