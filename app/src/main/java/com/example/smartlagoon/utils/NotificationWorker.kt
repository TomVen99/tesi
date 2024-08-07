package com.example.smartlagoon.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.smartlagoon.MainActivity
import com.example.smartlagoon.R
import com.example.smartlagoon.TakePhotoActivity

/*class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        sendNotification()
        Log.d("Worker","worker")
        return Result.success()
    }

    private fun sendNotification() {
        val context = applicationContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "channel_id",
            "Channel Name",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel Description"
        }
        notificationManager.createNotificationChannel(channel)

        // Create an Intent for the activity you want to start.
        val resultIntent = Intent(context, TakePhotoActivity::class.java)
        // Create the TaskStackBuilder.
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack.
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack.
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        /*val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }*/
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context, "channel_id").apply {
            setSmallIcon(R.drawable.ic_badge)
            setContentTitle("Berillo")
            setContentText("È il momento della sfida giornaliera!")
            priority = NotificationCompat.PRIORITY_DEFAULT
            setContentIntent(resultPendingIntent)
            setAutoCancel(true)
        }
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(1, builder.build())
        }

        //notificationManager.notify(1, builder.build())
    }
}*/
class NotificationWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // Recupera l'ID della foto dai parametri di input
        /*val photoId = inputData.getInt("photoId", -1)

        if (photoId != -1) {
            // Recupera la foto dal database utilizzando il repository
            val photo = repository.getPhotoById(photoId)
            photo?.let {*/
                sendNotification(applicationContext)//, photo)
            /*}
        }*/

        return Result.success()
    }

    private fun sendNotification(context: Context){//, photo: Photo) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "photo_channel"
        val channelName = "Photo Notifications"
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        // Create an Intent for the activity you want to start.
        val resultIntent = Intent(context, TakePhotoActivity::class.java)
        // Create the TaskStackBuilder.
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack.
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack.
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        /*val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }*/
        //val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo_notifica)
            .setContentTitle("Berillo")
            .setContentText("È il momento della sfida giornaliera!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
