package com.markoapps.taskmanager.tasks

class TaskSchandler {
    val tasks: MutableMap<String, Task> = mutableMapOf()

    fun addTask(task: Task) {
        tasks[task.taskId] = task
        if(!task.isDisable) {
            task.activateTask()
        }
    }

}