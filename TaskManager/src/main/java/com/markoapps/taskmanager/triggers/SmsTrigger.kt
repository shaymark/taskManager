package com.markoapps.taskmanager.triggers

import com.markoapps.taskmanager.framework.IObserver
import com.markoapps.taskmanager.managers.SmsData
import com.markoapps.taskmanager.managers.SmsManager
import com.markoapps.taskmanager.tasks.Task

data class SmsFilter (
    val sender: String? = null, val content: String? = null
        )

class SmsTrigger(val smsManager: SmsManager, task: Task, val filter: SmsFilter?) : Trigger(smsManager, task),
    IObserver<SmsData> {

    override fun setTrigger(payload: Map<String, String>) {
        smsManager.smsListener.add(this)
    }

    override fun removeTrigger() {
        smsManager.smsListener.remove(this)
    }

    override fun update(value: SmsData) {
       filter?.sender?.let {
           if(!value.sender.contains(it)) {
               return
           }
       }

        filter?.content?.let {
            if(!value.content.contains(it)) {
                return
            }
        }

        task.onTriggerFire(value)

    }
}