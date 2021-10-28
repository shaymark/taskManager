package com.markoapps.taskmanager.actions

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.markoapps.taskmanager.managers.CallManager
import com.markoapps.taskmanager.managers.GeneralManager

class GeneralToastAction(val message: String, val generalManager: GeneralManager, isNotification: Boolean = false): Action(generalManager, isNotification = isNotification) {


    override fun startAction() {
        generalManager.showToast(message)
    }

}