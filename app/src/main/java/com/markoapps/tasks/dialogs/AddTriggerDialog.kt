package com.markoapps.tasks.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.markoapps.taskmanager.managers.GeofanceEntry
import com.markoapps.taskmanager.models.ActionModel
import com.markoapps.taskmanager.models.TriggerModel
import com.markoapps.taskmanager.triggers.SmsFilter
import com.markoapps.tasks.databinding.ItemTaskDetailsEditBinding
import com.markoapps.tasks.uimodels.TaskDetailUi
import com.markoapps.tasks.uimodels.actionToTaskDetailUi
import com.markoapps.tasks.uimodels.triggerToTaskDetailUi

class AddTriggerDialog: DialogFragment() {

    lateinit var itemTaskDetailsEditBinding : ItemTaskDetailsEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemTaskDetailsEditBinding =  ItemTaskDetailsEditBinding.inflate(inflater, container, false)
        return itemTaskDetailsEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val triggerModel: TriggerModel? = requireArguments()[ARG_ACTION_KEY] as? TriggerModel
        val triggerType: TriggerType = triggerModel?.toTriggerType() ?:  requireArguments()[ARG_ACTION_TYPE_KEY] as TriggerType

        setAction(triggerType, triggerModel)

        itemTaskDetailsEditBinding.save.setOnClickListener {
            setFragmentResult(ACTION_RESULT_KEY, bundleOf(ACTION_BUNDLE_KEY to uiToTriggerModel(triggerType)))
        }

    }

    fun uiToTriggerModel(triggerType: TriggerType): TriggerModel{
        itemTaskDetailsEditBinding.apply {

            val newValues = actionContainer.getArgs()!!

            return when(triggerType) {
                TriggerType.SMS -> TriggerModel.SMSTriggerType(SmsFilter(newValues[0].value, newValues[1].value))
                else -> TriggerModel.SMSTriggerType(SmsFilter(newValues[0].value, newValues[1].value))
            }
        }
    }

    fun setAction(triggerType: TriggerType, triggerModel: TriggerModel? = null) {
        itemTaskDetailsEditBinding.apply {
            val argsData: TaskDetailUi.Args = triggerToTaskDetailUi(
            triggerModel ?:
                // for new action
                when (triggerType) {
                    TriggerType.SMS -> TriggerModel.SMSTriggerType(SmsFilter("xxxxxx", "yyyyyyy"))
                    TriggerType.GEO -> TriggerModel.GEOTriggerType(
                            geofanceEntry = GeofanceEntry(
                                key = "herzeliaGeo1",
                                latitude = 32.1624,
                                longitude = 34.8447,
                                transationType = 0
                            )
                    )
                }
            )

            title.text = argsData.title
            actionContainer.setArgs(argsData.list){

            }
            isNotificationCb.visibility = View.GONE


        }
    }

    companion object {
        fun getInstace(triggerModel: TriggerModel?, triggerType: TriggerType?) : AddTriggerDialog =
            AddTriggerDialog().apply {
                arguments = bundleOf(
                    ARG_ACTION_KEY to triggerModel,
                    ARG_ACTION_TYPE_KEY to triggerType
                )
            }

        val ARG_ACTION_KEY = "action model"
        val ARG_ACTION_TYPE_KEY = "action model type"

        val ACTION_RESULT_KEY = "ActionDialogResult"

        val ACTION_BUNDLE_KEY = "action"
    }
}


enum class TriggerType {
    SMS, GEO
}

fun TriggerModel.toTriggerType() = when(this) {
    is TriggerModel.SMSTriggerType -> TriggerType.SMS
    is TriggerModel.GEOTriggerType -> TriggerType.GEO
}