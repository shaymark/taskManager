package com.markoapps.tasks

import android.app.Application
import com.markoapps.taskmanager.ui.TaskManagerApi

class TasksApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TaskManagerApi.initApi(this)
    }

}