package com.markoapps.taskmanager.managers;

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.telecom.TelecomManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import java.lang.Exception
import java.lang.RuntimeException
import java.util.jar.Manifest


class CallManager(val context: Context) : Manager() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    var tm = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    fun startCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + phoneNumber)
        callIntent.setFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        )
        context.startActivity(callIntent)
    }

    fun getCurrentCallDetails() {

    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.P)
    fun stopCall() {
        try {
            val success = tm.endCall()
            Log.d("CallManager", "stopCall: " + success)
        } catch (e: Exception) {
            Log.d("CallManager", "faild: " + e)
        } catch (e: RuntimeException) {
            Log.d("CallManager", "faild: " + e)
        }

    }

}
