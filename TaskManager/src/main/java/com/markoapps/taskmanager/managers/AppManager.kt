package com.markoapps.taskmanager.managers;

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager


class AppManager(val context: Context) : Manager() {

    fun openAppWithNotification(packageName: String, notificationTitle: String, notificationmessage: String) {
        val pm: PackageManager = context.packageManager
        val launchIntent: Intent? = pm.getLaunchIntentForPackage(packageName)

        if(launchIntent != null) {
            val pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0)

            NotificationManager(context).sendNotification(notificationTitle, notificationmessage, pendingIntent, notificationId = (Math.random() * 1000).toInt())
        }

    }

    fun openAppPendingIntent(packageName: String): PendingIntent? {
        val pm: PackageManager = context.packageManager
        val launchIntent: Intent? = pm.getLaunchIntentForPackage(packageName)

        if(launchIntent != null) {
            return PendingIntent.getActivity(context, 0, launchIntent, 0)
        }
        return null
    }

    fun openApp(packageName: String) {
        val pm: PackageManager = context.packageManager
        val launchIntent: Intent? = pm.getLaunchIntentForPackage(packageName)

        if(launchIntent != null) {
            context.startActivity(launchIntent)
        }
    }

}
