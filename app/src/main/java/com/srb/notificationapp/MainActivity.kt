package com.srb.notificationapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "channelId"
    private val CHANNEL_Name = "channelName"
    private val ACTION_SNOOZE = "com.srb.notificationapp.ACTION_SNOOZE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

//===================used if there's no button============================================//

                                /*Explicit Intent*/
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


                                /*Implicit Intent*/
        val iIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data  = Uri.parse("https://www.google.com")

        }
        val pI = PendingIntent.getActivity(this,1,iIntent,0)


//===================used to show buttons in notification==================================//
        val snoozeIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(CHANNEL_ID, 0)
        }
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)

//====================================================================================================//

        createNotificationChannel()
 
 
        val notificationManager = NotificationManagerCompat.from(this)

        button.setOnClickListener {
            notificationManager.notify(1, notification(pI))
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            notificationManager.notify(2,notificationWithButton(pI))
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            notificationManager.notify(3,headsUpNotification())
        }

    }




    //to register the notification
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



    private fun notification(pI : PendingIntent) : Notification{
       return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Example")
            //text will be displayed only of one line ,if setStyle is there this won't be shown
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
            .setContentIntent(pI)
            //removes the notification  when the user taps it
            .setAutoCancel(true)
            .build()
    }


    private fun notificationWithButton(pI : PendingIntent) : Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText("sample text")
            .setContentTitle("notificationWithButton")
            .setSmallIcon(R.drawable.ic_baseline_anchor_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_baseline_access_alarm_24, "Snooze", pI)
            .build()

    }

    private fun headsUpNotification(): Notification {
        return Notification.Builder(this, CHANNEL_ID)
            .setContentText("sample text")
            .setContentTitle("headsUpNotification")
            .setSmallIcon(R.drawable.ic_baseline_anchor_24)
            .setPriority(Notification.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
            .setAutoCancel(true)
            .build()
    }

}
