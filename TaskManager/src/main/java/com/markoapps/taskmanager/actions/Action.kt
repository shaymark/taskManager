package com.markoapps.taskmanager.actions

import com.markoapps.taskmanager.managers.Manager
import com.markoapps.taskmanager.tasks.Task

abstract class Action(val manager: Manager, val task: Task? = null, val isNotification: Boolean = false)
    : IAction {

}