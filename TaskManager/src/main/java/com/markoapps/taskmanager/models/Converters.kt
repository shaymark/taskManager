package com.markoapps.taskmanager.models

import androidx.room.TypeConverter
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.markoapps.taskmanager.di.Provider
import com.markoapps.taskmanager.triggers.SmsFilter
import com.markoapps.taskmanager.models.ActionModel
import com.markoapps.taskmanager.models.Condition
import com.markoapps.taskmanager.models.TriggerModel


class Converters {

    @TypeConverter
    fun StringToTriggerModel(string: String?) : TriggerModel?{

            val map = Provider.gson.fromJson(string, Map::class.java)

            return when (map["type"]) {
                "SMSTriggerType" ->  Provider.gson.fromJson(string, TriggerModel.SMSTriggerType::class.java)
                else ->  null
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
//        val myType = object : TypeToken<List<ActionModel>>() {}.type
//        return Provider.gson.fromJson<List<ActionModel>>(string, myType)
          val myType = object : TypeToken<List<JsonObject>>() {}.type
          val list =  Provider.gson.fromJson<List<JsonObject>>(string, myType)
          return list.mapNotNull {
            when (it.get("type").asString) {
                "CallNumberActionModel" ->  Provider.gson.fromJson(it, ActionModel.CallNumberActionModel::class.java)
                "CallStopActionModel" ->  Provider.gson.fromJson(it, ActionModel.CallStopActionModel::class.java)
                "GeneralDelayActionModel" ->  Provider.gson.fromJson(it, ActionModel.GeneralDelayActionModel::class.java)
                else -> null
            }
          }
    }

    @TypeConverter
    fun ActionModelToString(actionModelList: List<ActionModel>): String {
        return Provider.gson.toJson(actionModelList)
    }

}