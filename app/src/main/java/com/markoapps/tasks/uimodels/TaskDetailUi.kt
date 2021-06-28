package com.markoapps.tasks.uimodels

import android.graphics.drawable.Icon

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
        val icon: Icon? = null
        )

enum class TitleType {
    trigger,
    action,
    condition
}