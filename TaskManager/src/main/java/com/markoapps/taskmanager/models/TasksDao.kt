package com.markoapps.taskmanager.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.markoapps.taskmanager.tasks.Task
import models.TaskModel

@Dao
interface TasksDao {

    @Insert(onConflict = REPLACE)
    fun addTask(task: TaskModel)

    @Query(value = "SELECT * FROM tasks")
    fun getAllTask(): List<TaskModel>

    @Query(value = "SELECT * FROM tasks WHERE isActive == 1")
    fun getAllActiveTasks(): List<TaskModel>

    @Delete
    fun deleteTask(task: TaskModel)

}