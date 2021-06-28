package com.markoapps.tasks.viewmodels

import androidx.lifecycle.*
import com.markoapps.taskmanager.models.ActionModel
import com.markoapps.taskmanager.ui.TaskManagerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.markoapps.taskmanager.models.TaskModel

class TasksDetailsViewModel: ViewModel() {

    var taskId: String = ""
    set(value) {
        if(value.isNotEmpty()) {
            viewModelScope.launch (Dispatchers.Default) {
                try {
                    val task = taskManagerApi.getTaskDetails(taskId = value)
                    taskLiveData.postValue(task)
                } catch (e: NoSuchElementException) {
                }
            }
        }
    }

    val taskLiveData: MutableLiveData<TaskModel> = MutableLiveData()

    val taskManagerApi = TaskManagerApi

    fun saveTask() {
        taskLiveData?.value?.let {
            taskManagerApi.updateOrAddTask(it)
        }
    }

    fun setTaskName(name: String) {
        taskLiveData.value?.let {
           taskLiveData.value = it.copy(name = name)
        }
    }

    fun setActive(isActive: Boolean) {
        taskLiveData.value?.let {
            taskLiveData.value = it.copy(isActive = isActive)
        }
    }

    fun addAction(actionModel: ActionModel) {

    }
}