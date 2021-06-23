package com.markoapps.taskmanager.ui

import android.content.Context
import android.content.IntentFilter
import com.markoapps.taskmanager.di.Provider
import com.markoapps.taskmanager.managers.SMSBroadcastReceiver
import com.markoapps.taskmanager.tasks.Task
import com.markoapps.taskmanager.triggers.SmsFilter
import com.markoapps.taskmanager.triggers.SmsTrigger

object TaskManagerApi {

    fun initApi(context: Context) {
        Provider.context = context

        // this only works when the app is not in background so i put one in the manifest
//        val intentFilter = IntentFilter()
//        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
//        context.registerReceiver(SMSBroadcastReceiver(), intentFilter)
    }

    fun addDefaultCallAfterSmsTask(callNumber: String, smsFrom: String, smsContent: String, delay: Long) {
        val task = Task (
            actionList = listOf(
                Provider.actionFactory.createCallNumberAction(callNumber),
                Provider.actionFactory.createGeneralDelayAction(delay),
                Provider.actionFactory.createCallStopAction()
            ),
            trigger = null)
        task.trigger = SmsTrigger(Provider.smsManager, task, SmsFilter(sender = smsFrom, content = smsContent))

        Provider.TaskSchandler.addTask(task)
    }
}