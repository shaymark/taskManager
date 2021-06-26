package com.markoapps.taskmanager.actions

import android.os.Build
import androidx.annotation.RequiresApi
import com.markoapps.taskmanager.managers.CallManager
import com.markoapps.taskmanager.managers.GeneralManager

class GeneralDelayAction(val delay: Long, val generalManager: GeneralManager): Action(generalManager) {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun startAction() {
        Thread.sleep(delay)
    }

}