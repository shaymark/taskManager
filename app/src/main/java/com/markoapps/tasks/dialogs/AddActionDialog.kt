package com.markoapps.tasks.dialogs

import android.os.Bundle
import android.telecom.Call
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
                ActionType.OPEN_APP -> ActionModel.OpenAppActionModel(newValues[0].value!!, newValues[1].value!!)
            }
        }
    }

    fun getActionType(): ActionType {
        val actionModel: ActionModel? = requireArguments()[ARG_ACTION_KEY] as? ActionModel
        val actionType: ActionType = actionModel?.toActionType() ?:  requireArguments()[ARG_ACTION_TYPE_KEY] as ActionType
        return actionType
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
                    ActionType.OPEN_APP -> ActionModel.OpenAppActionModel("default.package", "default.name")
                }
            )

            title.text = argsData.title
            actionContainer.setArgs(argsData.list, this@AddActivityDialog::onClickKey)
        }
    }

    fun onClickKey(position: Int) {
        when(getActionType()) {
            ActionType.CALL -> {
                ChoosePhoneDialog(requireContext()){
                    itemTaskDetailsEditBinding.actionContainer.setItemValue(it.mobileNumber, position)
                }.show()
            }
            ActionType.OPEN_APP -> {
                ChooseApplicationDialog(requireContext()){
                    itemTaskDetailsEditBinding.actionContainer.setItemValue(it.packageName, 0)
                    itemTaskDetailsEditBinding.actionContainer.setItemValue(it.appName, 1)
                }.show()
            }
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
    CALL, STOP, DELAY, OPEN_APP
}



fun ActionModel.toActionType() = when(this) {
    is ActionModel.CallNumberActionModel -> ActionType.CALL
    is ActionModel.CallStopActionModel -> ActionType.STOP
    is ActionModel.GeneralDelayActionModel -> ActionType.DELAY
    is ActionModel.OpenAppActionModel -> ActionType.OPEN_APP
    else -> ActionType.CALL
}