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

    var editedDialogPosition: Int? = null

    val taskLiveData: MutableLiveData<TaskModel> = MutableLiveData()

    val taskManagerApi = TaskManagerApi

    fun saveTask() {
        taskLiveData?.value?.let {
            taskManagerApi.updateOrAddTask(it)
        }
    }

    fun deleteAction(actionPostion: Int) {
        val actionList = taskLiveData.value!!.actionList.toMutableList().apply {
            removeAt(actionPostion)
        }
        taskLiveData?.value?.let {
            taskLiveData.value = it.copy(actionList = actionList)
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

    fun addOrUpdateAction(actionModel: ActionModel) {
        if(editedDialogPosition == null) {
            taskLiveData.value?.let {
                taskLiveData.value = it.copy(actionList =
                taskLiveData.value!!.actionList + actionModel
                )
            }
        } else {
            taskLiveData.value?.let {
                val postion = editedDialogPosition!!
                val actionList = taskLiveData.value!!.actionList.toMutableList().apply {
                    set(postion, actionModel)
                }
                taskLiveData.value?.let {
                    taskLiveData.value = it.copy(actionList = actionList)
                }
            }
            editedDialogPosition = null
        }

    }

}