package com.markoapps.tasks.dialogs

import android.os.Bundle
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

class AddActivityDialog(): DialogFragment() {

    interface OnAddActivity {
        fun onSaveActivitySubmit(actionModel: ActionModel, dialog: AddActivityDialog)
    }

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

        itemTaskDetailsEditBinding.title.setOnClickListener {
            val actions = listOf("Call", "Stop", "Delay")

            MaterialDialog(requireContext()).show {
                listItems(items = actions) { dialog, index, text ->
                    val action = when(index) {
                        0 -> ActionType.CALL
                        1 -> ActionType.STOP
                        2 -> ActionType.DELAY
                        else -> throw (ClassCastException("unssporterActionType"))
                    }
                    setAction(action)
                    dialog.dismiss()
                }


            }
        }

        itemTaskDetailsEditBinding.save.setOnClickListener {
            setFragmentResult("ActionDialog", bundleOf("action" to uiToActionType()))
        }
    }

    fun uiToActionType(): ActionModel{
        return ActionModel.CallNumberActionModel("434534543543")
    }

    fun setAction(actionType: ActionType) {
        itemTaskDetailsEditBinding.apply {
            val argsData: TaskDetailUi.Args = actionToTaskDetailUi(
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
}

enum class ActionType {
    CALL, STOP, DELAY
}