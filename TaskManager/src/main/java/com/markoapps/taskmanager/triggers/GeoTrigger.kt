package com.markoapps.taskmanager.triggers

import com.google.android.gms.location.Geofence
import com.markoapps.taskmanager.framework.IObserver
import com.markoapps.taskmanager.managers.*
import com.markoapps.taskmanager.tasks.Task



class GeoTrigger(val geoFenceManager: GeoFenceManager, task: Task, val geofenceEntry: GeofanceEntry) : Trigger(geoFenceManager, task),
    IObserver<Geofence> {


    override fun setTrigger(payload: Map<String, String>) {
        geoFenceManager.addGeofence(geofenceEntry, this)
    }

    override fun removeTrigger() {
        geoFenceManager.removeGeofence(geofenceEntry)
    }

    override fun update(value: Geofence) {
        if(value.requestId != geofenceEntry.key) {
            return
        }

        task.onTriggerFire(value)
    }
}