package com.markoapps.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.markoapps.taskmanager.models.TaskModel
import com.markoapps.taskmanager.ui.TaskManagerApi
import com.markoapps.tasks.adapters.TasksAdapter
import com.markoapps.tasks.databinding.FragmentTasksListBinding
import com.markoapps.tasks.viewmodels.TasksViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TasksListFragment : Fragment() {

    lateinit var viewBinding: FragmentTasksListBinding
    lateinit var taskAdapter: TasksAdapter

    val viewModel: TasksViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentTasksListBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        taskAdapter = TasksAdapter(listener = object : TasksAdapter.ClickListener{
            override fun onClickCell(task: TaskModel) {
                val action = TasksListFragmentDirections.actionTasksListFragmentToTaskDetailsFragment(task.id)
                findNavController().navigate(action)
            }

            override fun onClickDelete(task: TaskModel) {
                MaterialDialog(requireContext()).show {
                    title(text = "are you sure you want to delete?")
                    positiveButton {
                        viewModel.deleteTask(task)
                    }
                }
            }


        })

        viewBinding.apply {
            taskList.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        viewModel.taskLiveData.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }


    }
}