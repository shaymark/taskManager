package com.markoapps.tasks.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.markoapps.taskmanager.models.ActionModel
import com.markoapps.tasks.databinding.ItemTaskDetailsEditBinding
import com.markoapps.tasks.uimodels.TaskDetailUi
import com.markoapps.tasks.uimodels.actionToTaskDetailUi

class AddActivityDialog: DialogFragment() {

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

        val actionModel: ActionModel? = requireArguments()[ARG_ACTION_KEY] as? ActionModel
        val actionType: ActionType = actionModel?.toActionType() ?:  requireArguments()[ARG_ACTION_TYPE_KEY] as ActionType

        setAction(actionType, actionModel)

        itemTaskDetailsEditBinding.save.setOnClickListener {
            setFragmentResult(ACTION_RESULT_KEY, bundleOf(ACTION_BUNDLE_KEY to uiToActionModel(actionType)))
        }

    }

    fun uiToActionModel(actionType: ActionType): ActionModel{
        itemTaskDetailsEditBinding.apply {

            val newValues = actionContainer.getArgs()!!

            return when(actionType) {
                ActionType.CALL -> ActionModel.CallNumberActionModel(newValues[0].value!!)
                ActionType.STOP -> ActionModel.CallStopActionModel()
                ActionType.DELAY -> ActionModel.GeneralDelayActionModel(newValues[0].value!!.toLong() * 1000)
                else -> ActionModel.GeneralDelayActionModel(0)
            }
        }
    }

    fun setAction(actionType: ActionType, actionModel: ActionModel? = null) {
        itemTaskDetailsEditBinding.apply {
            val argsData: TaskDetailUi.Args = actionToTaskDetailUi(
                actionModel ?:
                // for new action
                when (actionType) {
                    ActionType.CALL -> ActionModel.CallNumberActionModel("")
                    ActionType.STOP -> ActionModel.CallStopActionModel()
                    ActionType.DELAY -> ActionModel.GeneralDelayActionModel(0)
                }
            )

            title.text = argsData.title
            actionContainer.setArgs(argsData.list)
        }
    }

    companion object {
        fun getInstace(actionModel: ActionModel?, actionType: ActionType?) : AddActivityDialog =
            AddActivityDialog().apply {
                arguments = bundleOf(
                    ARG_ACTION_KEY to actionModel,
                    ARG_ACTION_TYPE_KEY to actionType
                )
            }

        val ARG_ACTION_KEY = "action model"
        val ARG_ACTION_TYPE_KEY = "action model type"

        val ACTION_RESULT_KEY = "ActionDialogResult"

        val ACTION_BUNDLE_KEY = "action"
    }
}

enum class ActionType {
    CALL, STOP, DELAY
}



fun ActionModel.toActionType() = when(this) {
    is ActionModel.CallNumberActionModel -> ActionType.CALL
    is ActionModel.CallStopActionModel -> ActionType.STOP
    is ActionModel.GeneralDelayActionModel -> ActionType.DELAY
    else -> ActionType.CALL
}