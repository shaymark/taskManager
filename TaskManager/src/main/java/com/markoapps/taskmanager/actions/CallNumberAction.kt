package com.markoapps.taskmanager.actions

import android.app.PendingIntent
import com.markoapps.taskmanager.managers.CallManager
import com.markoapps.taskmanager.tasks.Task

class CallNumberAction(val phoneNumber: String, val callManager: CallManager): Action(callManager) {

    override fun startAction() {
        callManager.startCall(phoneNumber)
    }


}