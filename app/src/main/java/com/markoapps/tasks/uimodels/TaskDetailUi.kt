package com.markoapps.tasks.uimodels

import android.graphics.drawable.Icon
import com.markoapps.taskmanager.models.ActionModel
import com.markoapps.taskmanager.models.TriggerModel
import java.util.*

sealed class TaskDetailUi {

    abstract val id: String

    data class General(
            override val id: String,
            val name: String,
            val isActive: Boolean
    ) : TaskDetailUi()

    data class Title(
            override val id: String,
            val type: TitleType,
            val title: String
    ) : TaskDetailUi()

    data class Args(
            override val id: String,
            val title: String,
            val list: List<KeyValuePair>,
    ) : TaskDetailUi()
}

data class KeyValuePair (
    val key: String,
    val value: String?,
    val icon: Icon? = null,
    val isEnabled: Boolean
        )

enum class TitleType {
    trigger,
    action,
    condition
}

fun triggerToTaskDetailUi(triggerModel: TriggerModel): TaskDetailUi.Args = when(triggerModel) {
    is TriggerModel.SMSTriggerType -> {
        TaskDetailUi.Args(
            id = UUID.randomUUID().toString(),
            title = "sms trigger",
            list = listOf(
                KeyValuePair("sender", triggerModel.smsFilter.sender, null, true),
                KeyValuePair("contains", triggerModel.smsFilter.content, null, true),
            )
        )
    }
}

fun actionToTaskDetailUi(actionModel: ActionModel) : TaskDetailUi.Args  = when(actionModel) {
    is ActionModel.CallNumberActionModel -> {
        TaskDetailUi.Args(
            id = UUID.randomUUID().toString(),
            title = "call number",
            list = listOf(
                KeyValuePair("call to", actionModel.phoneNumber,null, true),
            )
        )
    }
    is ActionModel.CallStopActionModel -> {
        TaskDetailUi.Args(
            id = UUID.randomUUID().toString(),
            title = "hang up call number",
            list = listOf()
        )
    }
    is ActionModel.GeneralDelayActionModel -> {
        TaskDetailUi.Args(
            id = UUID.randomUUID().toString(),
            title = "delay",
            list = listOf(
                KeyValuePair("delay[seconds]", (actionModel.delay / 1000).toString(), null, true),
            )
        )
    }
}