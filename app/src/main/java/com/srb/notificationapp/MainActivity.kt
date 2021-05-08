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
    private val NOTIFICATION_ID: Int = 0
    private val ACTION_SNOOZE = "com.srb.notificationapp.ACTION_SNOOZE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        createNotificationChannel()

//===================used if there's no button==================================//
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


//===================used to show buttons in notification==================================//
        val snoozeIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(CHANNEL_ID, 0)
        }
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Example")
            //if style is there this won't be shown , text will be displayed only of one line
            .setContentText("sample text")
            .setSmallIcon(R.drawable.ic_baseline_anchor_24)
            //text can be displayed of multiple lines
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("this is big text following the second line as well let's see what happens")
            )
            //sets the notification to top if two notifications comes at the same time and the other one has low priority
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            //removes the notification  when the user taps it
            .setAutoCancel(true)
            .addAction(R.drawable.ic_baseline_access_alarm_24,"Snooze",snoozePendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        button.setOnClickListener {
            notificationManager.notify(NOTIFICATION_ID, notification)
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