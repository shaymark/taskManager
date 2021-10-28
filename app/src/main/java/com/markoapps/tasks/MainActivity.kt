package com.markoapps.tasks

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.markoapps.taskmanager.models.DefaultTaskModel
import com.markoapps.taskmanager.models.GeoTaskModel
import com.markoapps.taskmanager.models.TaskModel
import com.markoapps.taskmanager.models.TriggerModel
import com.markoapps.taskmanager.triggers.GeoTrigger
import com.markoapps.taskmanager.triggers.SmsTrigger
import com.markoapps.taskmanager.triggers.Trigger
import com.markoapps.taskmanager.triggers.TriggerFactory
import com.markoapps.taskmanager.ui.TaskManagerApi
import java.util.*


class MainActivity : AppCompatActivity() {

    val taskManagerApi = TaskManagerApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            taskManagerApi.updateOrAddTask(DefaultTaskModel.copy(id = UUID.randomUUID().toString(), isActive = false))
        }

        taskManagerApi.updateOrAddTask(GeoTaskModel.copy(isActive = true))

        requestMultiplePermissionLauncher.launch(
                arrayOf(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ANSWER_PHONE_CALLS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    val requestMultiplePermissionLauncher =
        registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted: Map<String, Boolean> ->
            if (isGranted[Manifest.permission.READ_CONTACTS] == false) {

            }
            if (isGranted[Manifest.permission.ANSWER_PHONE_CALLS] == false) {

            } else {

            }
            if (isGranted[Manifest.permission.CALL_PHONE] == false) {

            } else {

            }
            if (isGranted[Manifest.permission.RECEIVE_SMS] == false) {

            } else {

            }
            if (isGranted[Manifest.permission.READ_SMS] == false) {

            } else {

            }

            if (isGranted[Manifest.permission.ACCESS_FINE_LOCATION] == false) {

            } else {

            }

            if (isGranted[Manifest.permission.SYSTEM_ALERT_WINDOW] == false) {
                if(!isSystemAlertPermissionGranted((this))) {
                    val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                    startActivity(myIntent)
                }

            } else {

            }
        }

        @TargetApi(VERSION_CODES.M)
        fun isSystemAlertPermissionGranted(context: Context?): Boolean {
            return VERSION.SDK_INT < VERSION_CODES.M || Settings.canDrawOverlays(context)
        }
}