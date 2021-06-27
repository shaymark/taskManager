package com.markoapps.taskmanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.markoapps.taskmanager.triggers.SmsFilter

@Entity(tableName = "tasks")
data class TaskModel (
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "trigger")
    val trigger: TriggerModel,
    @ColumnInfo(name = "condition")
    val condition: Condition,
    @ColumnInfo(name = "actionList")
    val actionList: List<ActionModel>,
    @ColumnInfo(name = "isActive")
    val isActive: Boolean)

sealed class ActionModel(
    @SerializedName("type") val type: String) {
    data class CallNumberActionModel(
        @SerializedName("phoneNumber") val phoneNumber: String)
        : ActionModel("CallNumberActionModel")
    class CallStopActionModel()
        : ActionModel("CallStopActionModel")
    data class GeneralDelayActionModel(
        @SerializedName("delay") val delay: Long)
        : ActionModel("GeneralDelayActionModel")
}


sealed class TriggerModel (
    @SerializedName("type") val type: String) {
    data class SMSTriggerType(
        @SerializedName("smsFilter")val smsFilter: SmsFilter) :
        TriggerModel("SMSTriggerType")
}

enum class NetworkStatus {
    WIFI,
    IDLE
}

enum class BatteryStatus {
    LOW, MEDIUM, HIGH
}


data class Condition (
    val networkStatus: NetworkStatus? = null,
    val batteryStatus: BatteryStatus? = null
        )

