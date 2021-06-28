package com.markoapps.tasks.dialogs

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.markoapps.taskmanager.models.ActionModel
import com.markoapps.tasks.databinding.ItemTaskDetailsEditBinding
import com.markoapps.tasks.uimodels.TaskDetailUi
import com.markoapps.tasks.uimodels.actionToTaskDetailUi

class AddActivityDialog: DialogFragment() {

    lateinit var itemTaskDetailsEditBinding : ItemTaskDetailsEditBinding

    var actionType: ActionType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionType = savedInstanceState?.getSerializable("action_type") as? ActionType
    }

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

        itemTaskDetailsEditBinding.save.setOnClickListener {
            setFragmentResult(ACTION_RESULT_KEY, bundleOf(ACTION_BUNDLE_KEY to uiToActionType()))
        }

        val actionModel: ActionModel? = requireArguments()[ARG_ACTION_KEY] as? ActionModel
        if(actionModel != null) {
            setAction(actionModel.toActionType(), actionModel)
        } else {
            Handler(Looper.getMainLooper()).post {
                openChooseActionTypeDialog()
            }
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("action_type", actionType)
    }

    fun openChooseActionTypeDialog() {
        val actions = listOf("Call", "Stop", "Delay")
        MaterialDialog(requireContext()).show {
            listItems(items = actions) { dialog, index, text ->
                val action = when(index) {
                    0 -> ActionType.CALL
                    1 -> ActionType.STOP
                    2 -> ActionType.DELAY
                    else -> throw (ClassCastException("unssported ActionType"))
                }
                setAction(action)
                dialog.dismiss()
            }
            setOnCancelListener{
                dismiss()
            }
        }
    }

    fun uiToActionType(): ActionModel{
        itemTaskDetailsEditBinding.apply {
            return when(actionType) {
                ActionType.CALL -> ActionModel.CallNumberActionModel(keyValue0.value.text.toString())
                ActionType.STOP -> ActionModel.CallStopActionModel()
                ActionType.DELAY -> ActionModel.GeneralDelayActionModel(keyValue0.value.text.toString().toLong() * 1000)
                else -> ActionModel.GeneralDelayActionModel(0)
            }
        }
    }

    fun setAction(actionType: ActionType, actionModel: ActionModel? = null) {
        this.actionType = actionType
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
            val keyValueList = listOf(
                keyValue0,
                keyValue1,
                keyValue2,
                keyValue3,
                keyValue4,
            )

            keyValueList.forEachIndexed { index, item ->
                if(index < argsData.list.size ) {
                    item.root.visibility = View.VISIBLE
                    val arg = argsData.list[index]
                    item.key.text = arg.key
                    item.value.setText(arg.value)
                    item.isEnabled.isChecked = arg.isEnabled
                } else {
                    item.root.visibility = View.GONE
                }
            }

        }
    }

    companion object {
        fun getInstace(actionModel: ActionModel?) : AddActivityDialog =
            AddActivityDialog().apply {
                arguments = bundleOf(
                    ARG_ACTION_KEY to actionModel
                )
            }

        val ARG_ACTION_KEY = "action model"

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