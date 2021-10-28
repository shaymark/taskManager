package com.markoapps.taskmanager.actions

import com.markoapps.taskmanager.managers.CallManager
import com.markoapps.taskmanager.managers.GeneralManager

class ActionFactory(
    val callManager: CallManager,
    val generalManager: GeneralManager
) {

    fun createCallNumberAction(phoneNumber: String): CallNumberAction {
        return CallNumberAction(phoneNumber, callManager)
    }

    fun createCallStopAction() : CallStopAction {
        return CallStopAction(callManager)
    }

    fun createGeneralDelayAction(delay: Long) : GeneralDelayAction {
        return GeneralDelayAction(delay, generalManager)
    }

    fun createToastAction(message: String) : GeneralToastAction {
        return GeneralToastAction(message, generalManager)
    }

}