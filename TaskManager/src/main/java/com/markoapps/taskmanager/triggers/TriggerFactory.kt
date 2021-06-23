package com.markoapps.taskmanager.triggers


import com.markoapps.taskmanager.managers.SmsManager
import com.markoapps.taskmanager.tasks.Task

class TriggerFactory(val smsManager: SmsManager) {
    fun createSmsTrigger(task: Task, filter: SmsFilter?) : SmsTrigger{
        return SmsTrigger(smsManager = smsManager, task = task, filter = filter)
    }
}