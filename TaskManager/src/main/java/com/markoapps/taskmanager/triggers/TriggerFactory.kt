package com.markoapps.taskmanager.triggers


import com.markoapps.taskmanager.managers.GeoFenceManager
import com.markoapps.taskmanager.managers.GeofanceEntry
import com.markoapps.taskmanager.managers.SmsManager
import com.markoapps.taskmanager.tasks.Task

class TriggerFactory(val smsManager: SmsManager, val geoFenceManager: GeoFenceManager) {
    fun createSmsTrigger(task: Task, filter: SmsFilter?) : SmsTrigger{
        return SmsTrigger(smsManager = smsManager, task = task, filter = filter)
    }

    fun createGeoTrigger(task: Task, geoFenceEntry: GeofanceEntry): GeoTrigger {
        return GeoTrigger(geoFenceManager = geoFenceManager, task = task, geofenceEntry = geoFenceEntry)
    }
}