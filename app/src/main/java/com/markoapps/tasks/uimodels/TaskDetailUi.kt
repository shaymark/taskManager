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
            val type: TaskDetailUiArgsType,
            val actionPosition: Int = 0
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

enum class TaskDetailUiArgsType {
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
            ),
            type = TaskDetailUiArgsType.trigger
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
            ),
            type = TaskDetailUiArgsType.action
        )
    }
    is ActionModel.CallStopActionModel -> {
        TaskDetailUi.Args(
            id = UUID.randomUUID().toString(),
            title = "hang up call number",
            list = listOf(),
            type = TaskDetailUiArgsType.action
        )
    }
    is ActionModel.GeneralDelayActionModel -> {
        TaskDetailUi.Args(
            id = UUID.randomUUID().toString(),
            title = "delay",
            list = listOf(
                KeyValuePair("delay[seconds]", (actionModel.delay / 1000).toString(), null, true),
            ),
            type = TaskDetailUiArgsType.action
        )
    }
}