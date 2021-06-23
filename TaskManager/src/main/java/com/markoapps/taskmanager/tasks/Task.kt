package com.markoapps.taskmanager.tasks

import com.markoapps.taskmanager.actions.Action
import com.markoapps.taskmanager.di.Provider
import com.markoapps.taskmanager.triggers.Trigger
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class Task(val actionList: List<Action?>, var trigger: Trigger?, id: String = UUID.randomUUID().toString())
    : ITask {

    var taskSchandler: TaskSchandler? = null
    var taskId: String = UUID.randomUUID().toString()
    var isDisable: Boolean = false

    val executorService: ExecutorService = Executors.newFixedThreadPool(1)

    fun clearTask() {
        trigger?.removeTrigger()
    }

    fun activateTask() {
        trigger?.setTrigger(mapOf())
    }

    fun deactivateTask() {
        trigger?.removeTrigger()
    }


    override fun onTriggerFire(payload: Any?) {
        actionList?.forEach {
            executorService.submit {
                it?.startAction()
            }
        }

    }

}