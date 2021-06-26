package com.markoapps.taskmanager.models

import android.content.ContentValues
import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.markoapps.taskmanager.di.Provider
import com.markoapps.taskmanager.triggers.SmsFilter
import models.*
import java.util.*
import java.util.concurrent.Executors


@Database(
    entities = [TaskModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TasksDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "tasks.db"

        private var instance: TasksDatabase? = null

        private fun create(context: Context) : TasksDatabase =
            Room.databaseBuilder(context, TasksDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(rdc)
                .build()

        fun getInstance(context: Context): TasksDatabase =
            (instance ?: create(context).also { instance = it })

        var rdc: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // do something after database has been created

                val task = TaskModel(
                    id = UUID.randomUUID().toString(),
                    trigger = TriggerModel.SMSTriggerType(
                            smsFilter = SmsFilter(
                                sender = "+972545352473",
                                content = "call home gate"
                            )
                        )
                    ,
                    condition = Condition(),
                    actionList = listOf(
                        ActionModel.CallNumberActionModel(
                            phoneNumber = "+972542544581"
                        ),
                        ActionModel.GeneralDelayActionModel(
                            delay = 20000
                        )
                        ,
                        ActionModel.CallStopActionModel()
                    ),
                    isActive = true
                )

                Provider.executors.execute {
                    instance?.tasksDao()?.apply {
                        addTask(task)
                    }
                }

            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                // do something every time database is open
            }
        }

    }

    abstract fun tasksDao(): TasksDao

}

