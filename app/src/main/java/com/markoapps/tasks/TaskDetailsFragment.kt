package com.markoapps.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.markoapps.taskmanager.models.ActionModel
import com.markoapps.taskmanager.models.TriggerModel
import com.markoapps.tasks.adapters.TasksDetailsAdapter
import com.markoapps.tasks.databinding.FragmentTaskDetailsBinding
import com.markoapps.tasks.dialogs.*

import com.markoapps.tasks.viewmodels.TasksDetailsViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TaskDetailsFragment : Fragment() {

    lateinit var viewBindings : FragmentTaskDetailsBinding
    val viewModel: TasksDetailsViewModel by viewModels()
    val args: TaskDetailsFragmentArgs by navArgs()

    lateinit var taskDetailAdapter: TasksDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBindings = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return viewBindings.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskDetailAdapter = TasksDetailsAdapter(object: TasksDetailsAdapter.TasksDetailsAdapterListener {
            override fun onEditGeneralClick() = editTaskName()
            override fun onAddActionClick() = addAction()
            override fun onAddTriggerClick() = addTrigger()
            override fun onEditActionClick(actionPosition: Int) = editAction(actionPosition)
            override fun onEditTriggerClick() = editTrigger()
            override fun onDeleteActionClick(actionPosition: Int) = deleteAction(actionPosition)
        })

        viewBindings.apply {
            list.apply {
                list.itemAnimator = null
                list.adapter = taskDetailAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        viewModel.taskLiveData.observe(viewLifecycleOwner) {
            taskDetailAdapter.updateTaskModel(it)
        }

        viewModel.taskId = args.taskId
    }

    fun addAction(){
        openChooseActionTypeDialog {
            openActionDialog(null, actionType = it)
        }
    }

    fun editAction(actionPosition: Int) {
        viewModel.editedDialogPosition = actionPosition ///??????????? need to find difrent solution
        val actionModel = viewModel.taskLiveData.value!!.actionList.get(actionPosition)
        openActionDialog(actionModel, actionModel.toActionType())
    }

    fun deleteAction(actionPosition: Int) {
        MaterialDialog(requireContext()).show {
            title(text = "are you sure you want to delete?")
            positiveButton {
                viewModel.deleteAction(actionPosition)
                viewModel.saveTask()
            }
        }
    }

    fun openActionDialog(actionModel: ActionModel?, actionType: ActionType?){
        val dialog = AddActivityDialog.getInstace(actionModel = actionModel, actionType)
        dialog.show(parentFragmentManager, DIALOG_FRAGMENT_TAG)
        setFragmentResultListener(AddActivityDialog.ACTION_RESULT_KEY) { requestKey, bundle ->
            clearFragmentResultListener(AddActivityDialog.ACTION_RESULT_KEY)
            val dialog = parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) as? AddActivityDialog
            dialog?.dismiss()
            val result = bundle.getSerializable(AddActivityDialog.ACTION_BUNDLE_KEY) as? ActionModel
            if(result != null) {
                viewModel.addOrUpdateAction(result)
                viewModel.saveTask()
            }
        }
    }

    fun editTaskName(){
        var name = viewModel.taskLiveData.value!!.name
        var isActive = viewModel.taskLiveData.value!!.isActive

        MaterialDialog(requireContext()).show {

            input(hint = "Task Name", waitForPositiveButton = false, prefill = name) { dialog, text ->
                name = text.toString()
            }


            checkBoxPrompt(text = "Is Active", isCheckedDefault = isActive) {
                isActive = it
            }

            positiveButton {
                viewModel.setTaskName(name)
                viewModel.setActive(isActive)
                viewModel.saveTask()
            }

            negativeButton {  }
        }
    }

    fun editTrigger() {
        val triggerModel = viewModel.taskLiveData.value!!.trigger
        openTriggerDialog(triggerModel, triggerModel.toTriggerType())
    }

    fun openTriggerDialog(triggerModel: TriggerModel?, triggerType: TriggerType?){
        val dialog = AddTriggerDialog.getInstace(triggerModel = triggerModel, triggerType)
        dialog.show(parentFragmentManager, DIALOG_FRAGMENT_TAG)
        setFragmentResultListener(AddActivityDialog.ACTION_RESULT_KEY) { requestKey, bundle ->
            clearFragmentResultListener(AddActivityDialog.ACTION_RESULT_KEY)
            val dialog = parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) as? AddTriggerDialog
            dialog?.dismiss()
            val result = bundle.getSerializable(AddActivityDialog.ACTION_BUNDLE_KEY) as? TriggerModel
            if(result != null) {
                viewModel.updateTrigger(result)
                viewModel.saveTask()
            }
        }
    }

    fun openChooseTriggerTypeDialog(triggerType: ((triggerType: TriggerType) -> Unit)) {
        openChooseDialog(listOf(
                "SMS" to TriggerType.SMS
        ), triggerType)
    }

    fun openChooseActionTypeDialog(actionType: ((actionType: ActionType) -> Unit)) {
        openChooseDialog(listOf(
                "Call" to ActionType.CALL,
                "Stop" to ActionType.STOP,
                "Delay" to ActionType.DELAY
        ), actionType)
    }

    fun addTrigger() {
        openChooseTriggerTypeDialog {
            openTriggerDialog(triggerModel = null, triggerType = it)
        }
    }

    fun <T>openChooseDialog(list: List<Pair<String, T>>, choose: ((choose: T) -> Unit)) {
        MaterialDialog(requireContext()).show {
            listItems(items = list.map { it.first }) { dialog, index, text ->
                choose(list[index].second)
                dialog.dismiss()
            }
            setOnCancelListener{
                dismiss()
            }
        }
    }

    companion object {
        const val DIALOG_FRAGMENT_TAG =  "addActivity"
    }

}