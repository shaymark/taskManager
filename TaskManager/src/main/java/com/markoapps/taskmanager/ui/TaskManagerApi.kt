package com.markoapps.taskmanager.ui

import android.content.Context
import android.content.IntentFilter
import com.markoapps.taskmanager.actions.Action
import com.markoapps.taskmanager.actions.CallNumberAction
import com.markoapps.taskmanager.actions.CallStopAction
import com.markoapps.taskmanager.actions.GeneralDelayAction
import com.markoapps.taskmanager.di.Provider
import com.markoapps.taskmanager.managers.SMSBroadcastReceiver
import com.markoapps.taskmanager.models.TasksDatabase
import com.markoapps.taskmanager.tasks.Task
import com.markoapps.taskmanager.triggers.SmsFilter
import com.markoapps.taskmanager.triggers.SmsTrigger
import com.markoapps.taskmanager.triggers.Trigger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import com.markoapps.taskmanager.models.*

object TaskManagerApi {

    fun initApi(context: Context) {
        Provider.context = context

        // this only works when the app is not in background so i put one in the manifest
//        val intentFilter = IntentFilter()
//        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
//        context.registerReceiver(SMSBroadcastReceiver(), intentFilter)

        initTaskFromDb(Provider.tasksDatabase)
    }

    fun getTaskList(): List<TaskModel> {
        return Provider.tasksDatabase.tasksDao().getAllTask()
    }

    fun getTaskListFlow(): Flow<List<TaskModel>> {
        return Provider.tasksDatabase.tasksDao().getAllTaskFlow()
    }

    suspend fun getTaskDetails(taskId: String): TaskModel {
        return Provider.tasksDatabase.tasksDao().getTaskById(taskId).first()
    }

    fun getTaskDetailsFlow(taskId: String): Flow<TaskModel> {
        return Provider.tasksDatabase.tasksDao().getTaskByIdFlow(taskId).mapNotNull { it.first() }
    }

    fun updateOrAddTask(task: TaskModel) {
        Provider.executors.submit {
            Provider.tasksDatabase.tasksDao().addTask(task)
            Provider.TaskSchandler.addTask(task)
        }
    }

    fun deleteTask(task: TaskModel) {
        Provider.executors.submit {
            Provider.tasksDatabase.tasksDao().deleteTask(task)
            Provider.TaskSchandler.removeTask(task)
        }
    }

    private fun initTaskFromDb(tasksDatabase: TasksDatabase) {
        Provider.executors.execute {
            val taskModelList = tasksDatabase.tasksDao().getAllActiveTasks()

            taskModelList.forEach {
                Provider.TaskSchandler.addTask(it)
            }
        }
    }

    fun addDefaultCallAfterSmsTask(callNumber: String, smsFrom: String, smsContent: String, delay: Long) {
        val task = Task (
            actionList = listOf(
                Provider.actionFactory.createCallNumberAction(callNumber),
                Provider.actionFactory.createGeneralDelayAction(delay),
                Provider.actionFactory.createCallStopAction()
            ),
            trigger = null)
        task.trigger = SmsTrigger(Provider.smsManager, task, SmsFilter(sender = smsFrom, content = smsContent))

        Provider.TaskSchandler.addTask(task)
    }

}