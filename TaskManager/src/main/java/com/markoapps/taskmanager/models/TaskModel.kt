package com.markoapps.taskmanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.markoapps.taskmanager.managers.GeofanceEntry
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


sealed class ActionModel(@SerializedName(Converters.TYPE) val type: String) : java.io.Serializable{
    data class CallNumberActionModel(
        @SerializedName("phoneNumber") val phoneNumber: String)
        : ActionModel(Converters.CallNumberActionModelType)
    class CallStopActionModel()
        : ActionModel(Converters.CallStopActionModelType)
    data class GeneralDelayActionModel(
        @SerializedName("delay") val delay: Long)
        : ActionModel(Converters.GeneralDelayActionModelType)
    data class OpenAppActionModel(
        @SerializedName("packageName") val packageName: String,
        @SerializedName("appName") val appName: String,
        @SerializedName("isNotification") val isNotification: Boolean,
    ): ActionModel(Converters.OpenAppActionModelType)
    data class ToastActionModel(
        @SerializedName("message") val message: String
    ): ActionModel(Converters.ToastActionModelType)
}


sealed class TriggerModel  (
    @SerializedName(Converters.TYPE) val type: String)  : java.io.Serializable{

    data class SMSTriggerType(
        @SerializedName("smsFilter")val smsFilter: SmsFilter) :
        TriggerModel(Converters.SMSTriggerType)

    data class GEOTriggerType(
        @SerializedName("geofanceEntry")val geofanceEntry: GeofanceEntry) :
        TriggerModel(Converters.GEOTriggerType)
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

