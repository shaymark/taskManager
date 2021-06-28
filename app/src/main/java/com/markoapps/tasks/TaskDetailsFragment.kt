package com.markoapps.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.markoapps.taskmanager.models.ActionModel
import com.markoapps.tasks.adapters.TasksDetailsAdapter
import com.markoapps.tasks.databinding.FragmentTaskDetailsBinding
import com.markoapps.tasks.dialogs.AddActivityDialog

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
            override fun onAddActionClick() = addAction()
            override fun onEditActionClick(actionPosition: Int) = editAction(actionPosition)
            override fun onEditTriggerClick() = TODO("Not yet implemented")
            override fun onDeleteActionClick(actionPosition: Int) = deleteAction(actionPosition)
        })

        viewBindings.apply {
            list.apply {
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
        openActionDialog(null)
    }

    fun editAction(actionPosition: Int) {
        viewModel.editedDialogPosition = actionPosition ///??????????? need to find difrent solution
        val actionModel = viewModel.taskLiveData.value!!.actionList.get(actionPosition)
        openActionDialog(actionModel)
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

    fun openActionDialog(actionModel: ActionModel?){
        val dialog = AddActivityDialog.getInstace(actionModel = actionModel)
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

    companion object {
        const val DIALOG_FRAGMENT_TAG =  "addActivity"
    }

}



/*

                        id.text = it.id
                if(!it.name.equals(name.text.toString())) {
                    name.setText(it.name)
                }
                isActive.apply {

                    val arrayAdapter = ArrayAdapter(
                        context, R.layout.simple_list_item_1, listOf(
                            "Active", "NotActive"
                        )
                    )
                    adapter = arrayAdapter

                    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            viewModel.setActive(when (position) {
                                0 -> true
                                1 -> false
                                else -> false
                            })
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }

                    val selection = when(it.isActive) {
                        true -> 0
                        false -> 1
                    }
                    if(selection != this.selectedItemPosition) {
                        this.setSelection(selection)
                    }


                }

                name.doAfterTextChanged {
                    viewModel.setTaskName(it.toString())
                }

                save.setOnClickListener {
                    viewModel.saveTask()
                }
            }
        }

        */