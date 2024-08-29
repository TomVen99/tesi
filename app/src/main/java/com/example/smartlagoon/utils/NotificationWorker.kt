package com.example.smartlagoon.utils


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smartlagoon.MainActivity
import com.example.smartlagoon.R
import com.example.smartlagoon.ui.SmartlagoonRoute

class NotificationWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        sendNotification(applicationContext)

        return Result.success()
    }

    private fun sendNotification(context: Context){//, photo: Photo) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "photo_channel"
        val channelName = "Photo Notifications"
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        // Create an Intent for the activity you want to start.
        //val resultIntent = Intent(context, TakePhotoActivity::class.java)
        val resultIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("route", SmartlagoonRoute.Challenge.route)
        }
        // Create the TaskStackBuilder.
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack.
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack.
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo_notifica)
            .setContentTitle("BeLagoon")
            .setContentText("È il momento della sfida giornaliera!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
