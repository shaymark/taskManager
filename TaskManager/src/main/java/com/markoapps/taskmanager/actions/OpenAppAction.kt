package com.markoapps.taskmanager.actions

import android.app.PendingIntent
import com.markoapps.taskmanager.managers.AppManager
import com.markoapps.taskmanager.managers.CallManager
import com.markoapps.taskmanager.tasks.Task

class OpenAppAction(val packageName: String, isNotification: Boolean, val appManager: AppManager): Action(appManager, isNotification = isNotification), INotification {

    override fun startAction() {
        appManager.openApp(packageName)
    }

    override fun getPendingIntent(): PendingIntent? {
        return appManager.openAppPendingIntent(packageName)
    }

    override fun getTitle(): String = "open ${packageName}"


    override fun getContent(): String = "press to open ${packageName}"

}