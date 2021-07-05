package com.markoapps.tasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.markoapps.tasks.databinding.ItemTaskBinding
import com.markoapps.taskmanager.models.TaskModel

class TasksAdapter(val listener: ClickListener) : androidx.recyclerview.widget.ListAdapter<TaskModel, TasksAdapter.ViewHolder>(DiffTaskUtil()) {

    interface ClickListener {
        fun onClickCell(task: TaskModel)
        fun onClickDelete(task: TaskModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemTaskBinding.inflate(inflater, parent, false), listener)
    }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(val itemTaskBinding: ItemTaskBinding, val listener: ClickListener): RecyclerView.ViewHolder(itemTaskBinding.root) {

        fun bind(taskModel: TaskModel) {
            itemTaskBinding.apply {
                taskId.text = taskModel.id
                taskName.text = taskModel.name
                root.setOnClickListener {
                    listener.onClickCell(taskModel)
                }
                delete.setOnClickListener{
                    listener.onClickDelete(taskModel)
                }
            }
        }

    }

    class DiffTaskUtil: DiffUtil.ItemCallback<TaskModel>() {
        override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
            return oldItem == newItem
        }

    }

}