package com.markoapps.taskmanager.tasks

import com.markoapps.taskmanager.actions.Action
import com.markoapps.taskmanager.di.Provider
import com.markoapps.taskmanager.triggers.Trigger
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class Task(var actionList: List<Action?>, var trigger: Trigger? = null, id: String = UUID.randomUUID().toString())
    : ITask {

    var taskSchandler: TaskSchandler? = null
    var taskId: String = UUID.randomUUID().toString()
    var isDisable: Boolean = false

    val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun clearTask() {
        trigger?.removeTrigger()
    }

    fun activateTask() {
        deactivateTask() // we do this to not set the same trigger twice
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