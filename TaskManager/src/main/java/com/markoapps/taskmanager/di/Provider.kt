package com.markoapps.taskmanager.di

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.markoapps.taskmanager.actions.ActionFactory
import com.markoapps.taskmanager.managers.AppManager
import com.markoapps.taskmanager.managers.CallManager
import com.markoapps.taskmanager.managers.GeneralManager
import com.markoapps.taskmanager.managers.SmsManager
import com.markoapps.taskmanager.models.TasksDatabase
import com.markoapps.taskmanager.tasks.TaskSchandler
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

@SuppressLint("StaticFieldLeak")
object Provider {

    lateinit var context: Context

    val smsManager = SmsManager()

    val actionFactory by lazy {
        ActionFactory(callManager, generalManager)
    }

    val callManager by lazy {
        CallManager(context.applicationContext)
    }

    val generalManager by lazy {
        GeneralManager(context.applicationContext)
    }

    val appManager by lazy {
        AppManager(context.applicationContext)
    }

    val TaskSchandler by lazy {
        TaskSchandler()
    }

    val gson by lazy {
        Gson()
    }

    val executors = Executors.newSingleThreadExecutor()

    val tasksDatabase: TasksDatabase by lazy {
        TasksDatabase.getInstance(context)
    }

}