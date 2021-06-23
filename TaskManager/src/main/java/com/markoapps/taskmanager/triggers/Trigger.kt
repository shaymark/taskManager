package com.markoapps.taskmanager.triggers;

import com.markoapps.taskmanager.managers.Manager;
import com.markoapps.taskmanager.tasks.Task



abstract public class Trigger (
        val manager:Manager, val task: Task
) : ITrigger {

        fun onTriggerFired() {
                task.onTriggerFire()
        }
}
