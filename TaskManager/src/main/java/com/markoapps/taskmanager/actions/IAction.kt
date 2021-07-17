package com.markoapps.taskmanager.actions

import android.app.PendingIntent

interface IAction {
    fun startAction()

}

interface INotification {
    fun getPendingIntent(): PendingIntent? = null
    fun getTitle(): String
    fun getContent(): String
}