package com.markoapps.taskmanager.actions

import com.markoapps.taskmanager.managers.AppManager
import com.markoapps.taskmanager.managers.CallManager
import com.markoapps.taskmanager.tasks.Task

class OpenAppAction(val packageName: String, val appManager: AppManager): Action(appManager) {

    override fun startAction() {
        appManager.openApp(packageName)
    }

}