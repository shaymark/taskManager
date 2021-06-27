package com.markoapps.tasks

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.markoapps.tasks.databinding.FragmentTaskDetailsBinding

import com.markoapps.tasks.viewmodels.TasksDetailsViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TaskDetailsFragment : Fragment() {

    lateinit var viewBindings : FragmentTaskDetailsBinding
    val viewModel: TasksDetailsViewModel by viewModels()
    val args: TaskDetailsFragmentArgs by navArgs()

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



        viewModel.taskLiveData.observe(viewLifecycleOwner) {
            viewBindings.apply {
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

        viewModel.taskId = args.taskId

    }
}