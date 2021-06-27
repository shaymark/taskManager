package com.markoapps.tasks.viewmodels

import androidx.lifecycle.*
import com.markoapps.taskmanager.ui.TaskManagerApi
import com.markoapps.taskmanager.models.TaskModel

class TasksViewModel: ViewModel() {

    val taskLiveData: LiveData<List<TaskModel>> = TaskManagerApi.getTaskListFlow().asLiveData()

}