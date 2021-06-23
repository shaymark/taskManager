package com.markoapps.taskmanager.managers;

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telecom.TelecomManager
import androidx.annotation.RequiresApi


class GeneralManager(val context: Context) : Manager() {

   fun delay(delay: Long) {
       Thread.sleep(delay)
   }
}
