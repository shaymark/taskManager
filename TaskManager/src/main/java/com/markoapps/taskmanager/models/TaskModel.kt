package models


data class TaskModel (
    val trigger: TriggerModel,
    val action: ActionModel)


data class ActionModel (
    val type: ActionType,
    val payload: Map<String, String>
        )

enum class ActionType {
    call,
    sendSms,
    general
}

data class TriggerModel (
    val type: TriggerType,
    val payload: Map<String, String>)

enum class TriggerType {
    blueTooth,
    call
}