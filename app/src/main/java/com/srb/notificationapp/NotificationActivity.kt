package com.srb.notificationapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.srb.notificationapp.Constants.ACTION_SNOOZE
import com.srb.notificationapp.Constants.CHANNEL_ID
import com.srb.notificationapp.Constants.CHANNEL_Name
import com.srb.notificationapp.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //creating notification channel
        createNotificationChannel()
        val notificationManager = NotificationManagerCompat.from(this)


        //on clicks display the notifications
        binding.button.setOnClickListener {
            notificationManager.notify(1, showSimpleNotification())
        }


        /*Implicit Intent*/
        val iIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse("https://www.google.com")
        }
        val pI = PendingIntent.getActivity(this, 111, iIntent, PendingIntent.FLAG_IMMUTABLE)

        binding.button2.setOnClickListener {
            notificationManager.notify(2, showNotificationWithButton(pI))
        }


        /*Explicit Intent*/
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val p2 = PendingIntent.getActivity(this, 112, intent, PendingIntent.FLAG_IMMUTABLE)

        binding.button3.setOnClickListener {
            notificationManager.notify(3, showNotificationWhichHasPendingIntent(p2))
        }


        /*Broadcasting intent*/
        val intent3 = Intent(this,MyBroadcastReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(CHANNEL_ID, CHANNEL_Name)
        }

        val p3  = PendingIntent.getBroadcast(this,113,intent3,PendingIntent.FLAG_IMMUTABLE)
        binding.button4.setOnClickListener {
            notificationManager.notify(4,showNotificationWhichHasPendingIntent(p3))
        }

    }

    //to register the notification
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_Name,
            NotificationManager.IMPORTANCE_DEFAULT
//            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "my notification channel description"
            enableVibration(true)
            lightColor = Color.GREEN
            enableLights(true)
        }

        // Register the channel with the system
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }


    private fun showNotificationWhichHasPendingIntent(pI: PendingIntent): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Simple Notification")
            //text will be displayed only of one line ,if setStyle is there this won't be shown
            .setContentText("sample content text")
            //text can be displayed of multiple lines
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("this is a big text following the second line as well let's see what happens")
            )
            .setSmallIcon(R.drawable.ic_baseline_anchor_24)
            //sets the notification to top if two notifications comes at the same time and the other one has low priority
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pI)
            //removes the notification  when the user taps it
            .setAutoCancel(true)
            .build()
    }

    private fun showNotificationWithButton(pI: PendingIntent): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification With Button")
            .setContentText("Notification with button text")
            .setSmallIcon(R.drawable.ic_baseline_anchor_24)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            //used to add button
            .addAction(R.drawable.ic_hut, "Google", pI)
            .build()
    }

    private fun showSimpleNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Simple Notification")
            .setContentText("Simple Notification content text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()
    }


}