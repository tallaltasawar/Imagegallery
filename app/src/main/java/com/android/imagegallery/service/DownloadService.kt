package com.android.imagegallery.service

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.android.imagegallery.retrofit.GalleryService
import dagger.android.AndroidInjection
import okhttp3.ResponseBody
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class DownloadService: IntentService("DownloadService") {

    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    @Inject
    lateinit var galleryService: GalleryService

    private lateinit var imageUrl: String

    companion object {
        private val TAG = DownloadService::class.java.simpleName
        private const val NOTIFICATION_ID = 12345678
        private const val NOTIFICATION_CHANNEL_ID = "channel_id"

        const val PARAM_IMAGE_URL = "imageUrl"
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            if (intent.hasExtra(PARAM_IMAGE_URL)) {
                imageUrl = intent.getStringExtra(PARAM_IMAGE_URL)
            }
        }

        notificationBuilder = getNotificationBuilder()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = getNotificationChannel()
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        notificationManager!!.notify(NOTIFICATION_ID, notificationBuilder!!.build())

        downloadImage(galleryService.downloadImage(imageUrl).execute().body())

    }

    /**
     * Notification channel initialization.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationChannel(): NotificationChannel{
        val notificationChannel =
            NotificationChannel(NOTIFICATION_CHANNEL_ID, "image_download", NotificationManager.IMPORTANCE_LOW)

        notificationChannel.description = "no sound"
        notificationChannel.setSound(null, null)
        notificationChannel.enableLights(false)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.enableVibration(false)
        return notificationChannel
    }

    /**
     * Notification initialization.
     */
    private fun getNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Download")
            .setContentText("Downloading Image")
            .setDefaults(0)
            .setAutoCancel(false)
    }

    /**
     * Downloads the image and stores in the [Download] folder on external storage.
     * @param body - Retrofit ResponseBody.
     */
    private fun downloadImage(body: ResponseBody?) {
        try {
            var count: Int
            val data = ByteArray(1024 * 4)
            val fileSize = body?.contentLength()
            val inputStream = BufferedInputStream(body?.byteStream()!!, 1024 * 8)
            val outputFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "downloadedImage.jpg"
            )
            val outputStream = FileOutputStream(outputFile)
            var total: Long = 0

            do {
                count = inputStream.read(data)
                total += count.toLong()
                val progress = ((total * 100).toDouble() / (fileSize?.toDouble() ?: 0.0)).toInt()

                updateNotification(progress)
                if (count == -1) break
                outputStream.write(data, 0, count)
            } while (count != -1)

            onDownloadComplete()
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * Updates notification with latest progress.
     * @param currentProgress - How much file is downloaded.
     */
    private fun updateNotification(currentProgress: Int) {
        notificationBuilder!!.setProgress(100, currentProgress, false)
        notificationBuilder!!.setContentText("Downloaded: $currentProgress%")
        notificationManager!!.notify(NOTIFICATION_ID, notificationBuilder!!.build())
    }

    /**
     * Change status of notification when the download completes.
     */
    private fun onDownloadComplete() {
        notificationManager!!.cancel(NOTIFICATION_ID)
        notificationBuilder!!.setSmallIcon(android.R.drawable.stat_sys_download_done)
        notificationBuilder!!.setProgress(0, 0, false)
        notificationBuilder!!.setContentText("Image Download Completed")
        notificationManager!!.notify(NOTIFICATION_ID, notificationBuilder!!.build())

    }

    override fun onTaskRemoved(rootIntent: Intent) {
        notificationManager!!.cancel(NOTIFICATION_ID)
    }
}
