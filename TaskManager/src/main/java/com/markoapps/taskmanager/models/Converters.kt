package com.markoapps.taskmanager.models

import androidx.room.TypeConverter
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.markoapps.taskmanager.di.Provider
import java.lang.Exception


class Converters {

    companion object {
        const val TYPE = "type"
        const val SMSTriggerType = "SMSTriggerType"
        const val GEOTriggerType = "GEOTriggerType"
        const val CallNumberActionModelType = "CallNumberActionModel"
        const val CallStopActionModelType = "CallStopActionModel"
        const val GeneralDelayActionModelType = "GeneralDelayActionModel"
        const val OpenAppActionModelType = "OpenAppActionModel"
        const val ToastActionModelType = "ToastActionModel"
    }

    @TypeConverter
    fun StringToTriggerModel(string: String?) : TriggerModel?{

            val map = Provider.gson.fromJson(string, Map::class.java)

            return when (map[TYPE]) {
                SMSTriggerType ->  Provider.gson.fromJson(string, TriggerModel.SMSTriggerType::class.java)
                GEOTriggerType ->  Provider.gson.fromJson(string, TriggerModel.GEOTriggerType::class.java)
                else -> throw(Exception("invalid trigger type !!!"))
            }

    }

    @TypeConverter
    fun TriggerModelToString(triggerModel: TriggerModel): String {
        return Provider.gson.toJson(triggerModel)
    }

    @TypeConverter
    fun StringToTCondition(string: String?) : Condition?{
        return Provider.gson.fromJson(string, Condition::class.java)
    }

    @TypeConverter
    fun ConditionToString(condition: Condition): String {
        return Provider.gson.toJson(condition)
    }

    @TypeConverter
    fun StringToTActionModelList(string: String?) : List<ActionModel?>{
          val myType = object : TypeToken<List<JsonObject>>() {}.type
          val list =  Provider.gson.fromJson<List<JsonObject>>(string, myType)
          return list.mapNotNull {
            when (it.get(TYPE).asString) {
                CallNumberActionModelType ->  Provider.gson.fromJson(it, ActionModel.CallNumberActionModel::class.java)
                CallStopActionModelType ->  Provider.gson.fromJson(it, ActionModel.CallStopActionModel::class.java)
                GeneralDelayActionModelType ->  Provider.gson.fromJson(it, ActionModel.GeneralDelayActionModel::class.java)
                OpenAppActionModelType -> Provider.gson.fromJson(it, ActionModel.OpenAppActionModel::class.java)
                ToastActionModelType -> Provider.gson.fromJson(it, ActionModel.ToastActionModel::class.java)
                else -> throw(Exception("invalid action type !!!"))
            }
          }
    }

    @TypeConverter
    fun ActionModelToString(actionModelList: List<ActionModel>): String {
        return Provider.gson.toJson(actionModelList)
    }

}