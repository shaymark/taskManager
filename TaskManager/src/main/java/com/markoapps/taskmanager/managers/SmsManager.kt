package com.markoapps.taskmanager.managers;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.markoapps.taskmanager.di.Provider
import com.markoapps.taskmanager.framework.IObservable
import java.util.*


data class SmsData(
    val date: Date, val sender: String, val content: String
)

public class SmsManager: Manager() {

    val smsListener : IObservable<SmsData> = IObservable()

    fun sendSms() {

    }

    fun getLastSms(): List<SmsData> {
        return listOf()
    }


}

class SMSBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val bundle = intent.extras
        var msgs: Array<SmsMessage?>? = null
        var str = "no message received"
        if (bundle != null) {
            val pdus = bundle["pdus"] as Array<Any>?
            msgs = arrayOfNulls<SmsMessage>(pdus!!.size)
            for (i in msgs.indices) {
                msgs!![i] = SmsMessage.createFromPdu(pdus!![i] as ByteArray)
                str += "SMS from Phone No: " + msgs[i]?.getOriginatingAddress()
                str += """
                    
                    Message is: 
                    """.trimIndent()
                str += msgs[i]?.getMessageBody().toString()
                str += "\n"
                val phoneNumber = msgs[i]?.getOriginatingAddress() ?: ""
                val content = msgs[i]?.getMessageBody().toString()
                Provider.smsManager.smsListener.setValue(SmsData(Date(), phoneNumber, content))
            }
            Log.v("Debug", str)
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
    }
}
