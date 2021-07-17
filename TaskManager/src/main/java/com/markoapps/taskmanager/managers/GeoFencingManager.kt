package com.markoapps.taskmanager.managers;

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.location.GeofencingEvent.*
import com.markoapps.taskmanager.di.Provider
import com.markoapps.taskmanager.framework.IObserver

data class GeofanceEntry(
    val key: String,
    val latitude : Double,
    val longitude: Double,
    val transationType: Int
)

class GeoFenceManager(val context: Context) : Manager() {

    val triggerMap : MutableMap<String, IObserver<Geofence>> = mutableMapOf()

    val geofenceHelper = GeoFenceHelper(context)

    fun addGeofence(geofenceEntry: GeofanceEntry, observer: IObserver<Geofence>) {
        triggerMap.put(geofenceEntry.key, observer)
        geofenceHelper.addGeoFence(geofenceEntry)
    }

    fun removeGeofence(geofenceEntry: GeofanceEntry) {
        triggerMap.remove(geofenceEntry.key)
        geofenceHelper.removeGeoFence(geofenceEntry)
    }

    fun onGeoFeanceUpdate(geofenceList:  List<Geofence>) {
        geofenceList.forEach {
            triggerMap[it.requestId]?.update(it)
        }
    }

}

class GeoFenceHelper(val context: Context) {
    val geofencingClient = GeofencingClient(context)

    val geofenceList: MutableList<Geofence> = mutableListOf()

    fun addGeoFence(geofenceEntry: GeofanceEntry){

    }

    fun removeGeoFence(geofenceEntry: GeofanceEntry){

    }

    @SuppressLint("MissingPermission")
    fun initGeofance() {
        setGeofenceList()

        val getGeofencingRequest = getGeofencingRequest()


        geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences added
                // ...
            }
            addOnFailureListener {
                // Failed to add geofences
                // ...
            }
        }

    }

    fun setGeofenceList(){

        val entry = DEFULT_ENTRY

        geofenceList.add(
            Geofence.Builder()

                .setRequestId(entry.key)

                .setCircularRegion(
                    entry.latitude,
                    entry.longitude,
                    GEOFENCE_RADIOS
                )


                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)

                .build())

    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
            addGeofences(geofenceList)
        }.build()
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun removeGeoFance(id: String) {
        geofencingClient?.removeGeofences(listOf(id))?.run {
            addOnSuccessListener {
                // Geofences removed
            }
            addOnFailureListener {
                // Failed to remove geofences
            }
        }
    }

    companion object {
        val DEFULT_ENTRY = GeofanceEntry (
            "123",
            11.33,
            44.55,
            Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)

        val GEOFENCE_RADIOS = 300f
    }
}

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    // ...
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            Provider.geoFenceManager.onGeoFeanceUpdate(triggeringGeofences)

        } else {
            // Log the error.
//            Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
//                geofenceTransition))
        }
    }

    companion object {
        val TAG = GeofenceBroadcastReceiver.javaClass.simpleName
    }
}