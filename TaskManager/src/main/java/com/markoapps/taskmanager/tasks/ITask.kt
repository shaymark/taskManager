package com.markoapps.taskmanager.tasks

interface ITask {
    fun onTriggerFire(payload: Any? = null)
}