package com.markoapps.taskmanager.actions

import android.os.Build
import androidx.annotation.RequiresApi
import com.markoapps.taskmanager.managers.CallManager

class CallStopAction(val callManager: CallManager): Action(callManager) {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun startAction() {
        callManager.stopCall()
    }

}