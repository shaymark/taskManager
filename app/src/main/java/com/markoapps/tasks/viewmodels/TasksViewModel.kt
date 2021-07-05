package com.markoapps.tasks.viewmodels

import androidx.lifecycle.*
import com.markoapps.taskmanager.ui.TaskManagerApi
import com.markoapps.taskmanager.models.TaskModel

class TasksViewModel: ViewModel() {

    val taskManagerApi = TaskManagerApi

    val taskLiveData: LiveData<List<TaskModel>> = taskManagerApi.getTaskListFlow().asLiveData()

    fun deleteTask(taskModel: TaskModel) {
        taskManagerApi.deleteTask(taskModel)
    }
}