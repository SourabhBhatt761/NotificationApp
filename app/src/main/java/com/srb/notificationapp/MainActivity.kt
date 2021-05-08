package com.srb.notificationapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "channelId"
    private val CHANNEL_Name = "channelName"
    private val NOTIFICATION_ID : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        createNotificationChannel()

        val intent = Intent(this,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Example")
            .setContentText("sample text")
            .setSmallIcon(R.drawable.ic_baseline_anchor_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)              //sets the notification to top if two notifications comes at the same time and the other one has low priority
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)            //removes the notification  when the user taps it
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        button.setOnClickListener {
            notificationManager.notify(NOTIFICATION_ID,notification)
        }

    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_Name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "hello there people"
                enableVibration(true)
                lightColor = Color.GREEN
                enableLights(true)
            }

            // Register the channel with the system
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)


        }
    }
}