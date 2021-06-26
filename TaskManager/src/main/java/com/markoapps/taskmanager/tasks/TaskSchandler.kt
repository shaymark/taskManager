package com.markoapps.taskmanager.tasks

import com.markoapps.taskmanager.actions.Action
import com.markoapps.taskmanager.actions.CallNumberAction
import com.markoapps.taskmanager.actions.CallStopAction
import com.markoapps.taskmanager.actions.GeneralDelayAction
import com.markoapps.taskmanager.di.Provider
import com.markoapps.taskmanager.triggers.SmsTrigger
import com.markoapps.taskmanager.triggers.Trigger
import models.ActionModel
import models.TaskModel
import models.TriggerModel

class TaskSchandler {
    val tasks: MutableMap<String, Task> = mutableMapOf()

    fun addTask(taskModel: TaskModel) {
        addTask(taskFromTaskModel(taskModel))
    }

    fun addTask(task: Task) {
        tasks[task.taskId] = task
        task.taskSchandler = this
        if(!task.isDisable) {
            task.activateTask()
        }
    }

}

private fun taskFromTaskModel(taskModel: TaskModel): Task = Task(
    id = taskModel.id,
    actionList = taskModel.actionList.map {
        actionModelToAction(it)
    },
).apply {
    isDisable = !taskModel.isActive
    trigger = triggerModelToTrigger(taskModel.trigger, this)
}

private fun actionModelToAction(actionModel: ActionModel): Action {
    return when (actionModel) {
        is ActionModel.CallNumberActionModel -> CallNumberAction(
            actionModel.phoneNumber,
            Provider.callManager)
        is ActionModel.CallStopActionModel -> CallStopAction(
            Provider.callManager
        )
        is ActionModel.GeneralDelayActionModel -> GeneralDelayAction(
            actionModel.delay,
            Provider.generalManager
        )
    }
}

private fun triggerModelToTrigger(triggerModel: TriggerModel, task: Task) : Trigger {
    return when(triggerModel) {
        is TriggerModel.SMSTriggerType -> SmsTrigger(
            smsManager = Provider.smsManager,
            task = task,
            filter = triggerModel.smsFilter

        )
    }
}