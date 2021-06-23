package com.markoapps.taskmanager.triggers

interface ITrigger {
    fun setTrigger(payload: Map<String, String>)
    fun removeTrigger()
}