package com.markoapps.tasks

import android.app.Application
import com.markoapps.taskmanager.actions.Action
import com.markoapps.taskmanager.actions.ActionFactory
import com.markoapps.taskmanager.tasks.Task
import com.markoapps.taskmanager.ui.TaskManagerApi

class TasksApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TaskManagerApi.initApi(this)

        TaskManagerApi.addDefaultCallAfterSmsTask("+972546187418", "+972546187418", "call home gate", 20000)
    }
}