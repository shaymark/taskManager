package com.markoapps.taskmanager.actions

import android.os.Build
import androidx.annotation.RequiresApi
import com.markoapps.taskmanager.managers.CallManager
import com.markoapps.taskmanager.managers.GeneralManager

class GeneralDelayAction(val generalManager: GeneralManager, val delay: Long): Action(generalManager) {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun startAction() {
        Thread.sleep(delay)
    }

}