package com.srb.notificationapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Constants.ACTION_SNOOZE) {
            Toast.makeText(context,"Received intent",Toast.LENGTH_LONG).show()
        }
    }

}
