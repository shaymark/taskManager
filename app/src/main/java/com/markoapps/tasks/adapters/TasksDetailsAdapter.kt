package com.markoapps.tasks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.markoapps.taskmanager.models.ActionModel
import com.markoapps.taskmanager.models.TaskModel
import com.markoapps.taskmanager.models.TriggerModel
import com.markoapps.tasks.databinding.ItemTaskDetailsArgsBinding
import com.markoapps.tasks.databinding.ItemTaskDetailsGeneralBinding
import com.markoapps.tasks.databinding.ItemTaskDetailsTitleBinding
import com.markoapps.tasks.uimodels.*
import kotlinx.coroutines.*
import java.lang.ClassCastException
import java.util.*

private val ITEM_VIEW_TYPE_GENERAL =    0
private val ITEM_VIEW_TYPE_TITLE =      1
private val ITEM_VIEW_TYPE_ARGS =     2

class TasksDetailsAdapter(val listener: TasksDetailsAdapterListener? = null) : androidx.recyclerview.widget.ListAdapter<TaskDetailUi, RecyclerView.ViewHolder>(DiffTaskUtil()) {

    interface TasksDetailsAdapterListener {
        fun onAddActionClick()
        fun onEditActionClick(actionPosition: Int)
        fun onEditTriggerClick()
        fun onDeleteActionClick(actionPosition: Int)
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun updateTaskModel(taskModel: TaskModel) {
        adapterScope.launch {

            fun triggerToTaskDetailUiList(triggerModel: TriggerModel): List<TaskDetailUi> {
                return listOf(triggerModel).map {
                    triggerToTaskDetailUi(triggerModel)
                }
            }

            fun actionListToTaskDetailUiList(actions: List<ActionModel>): List<TaskDetailUi> {
                return actions.mapIndexed { index, actionModel ->
                    actionToTaskDetailUi(actionModel).copy(actionPosition = index)
                }
            }

            val items: List<TaskDetailUi> = concatenate(
                    listOf( TaskDetailUi.General(
                            id = taskModel.id,
                            name = taskModel.name,
                            isActive = taskModel.isActive
                    )),

                    listOf(
                    TaskDetailUi.Title(
                            id = UUID.randomUUID().toString(),
                            type = TitleType.trigger,
                            title = "Trigger"
                    )),

                    triggerToTaskDetailUiList(triggerModel = taskModel.trigger) +
                            listOf(
                    TaskDetailUi.Title(
                            id = UUID.randomUUID().toString(),
                            type = TitleType.action,
                            title = "Actions"
                    )),

                    actionListToTaskDetailUiList(actions =  taskModel.actionList)
            )


            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    fun <T> concatenate(vararg lists: List<T>): List<T> {
        return listOf(*lists).flatten()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_GENERAL -> ViewHolderGeneral.from(parent)
            ITEM_VIEW_TYPE_TITLE -> ViewHolderTitle.from(parent, listener)
            ITEM_VIEW_TYPE_ARGS -> ViewHolderArgs.from(parent, listener)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TaskDetailUi.General -> ITEM_VIEW_TYPE_GENERAL
            is TaskDetailUi.Title -> ITEM_VIEW_TYPE_TITLE
            is TaskDetailUi.Args -> ITEM_VIEW_TYPE_ARGS
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
         when(holder) {
             is ViewHolderGeneral ->  {
                 val item = getItem(position) as TaskDetailUi.General
                 holder.bind(item)
             }
             is ViewHolderTitle ->  {
                 val item = getItem(position) as TaskDetailUi.Title
                 holder.bind(item)
             }
             is ViewHolderArgs -> {
                 val item = getItem(position) as TaskDetailUi.Args
                 holder.bind(item)
             }
         }

    }

    class ViewHolderGeneral(val itemTaskBinding: ItemTaskDetailsGeneralBinding): RecyclerView.ViewHolder(itemTaskBinding.root) {

        fun bind(generalData: TaskDetailUi.General) {
            itemTaskBinding.apply {
                id.text = generalData.id
                name.text = generalData.name
                isActive.text = generalData.isActive.toString()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolderGeneral {
                val inflater = LayoutInflater.from(parent.context)
                return ViewHolderGeneral(ItemTaskDetailsGeneralBinding.inflate(inflater, parent, false))
            }
        }
    }

    class ViewHolderTitle(val itemTaskBinding: ItemTaskDetailsTitleBinding, val listener: TasksDetailsAdapterListener?): RecyclerView.ViewHolder(itemTaskBinding.root) {

        fun bind(titleData: TaskDetailUi.Title) {
            itemTaskBinding.apply {
                title.text = titleData.title
                add.setOnClickListener{
                    listener?.onAddActionClick()
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup, listener: TasksDetailsAdapterListener?): ViewHolderTitle {
                val inflater = LayoutInflater.from(parent.context)
                return ViewHolderTitle(ItemTaskDetailsTitleBinding.inflate(inflater, parent, false), listener)
            }
        }
    }

    class ViewHolderArgs(val itemTaskTriggerBinding: ItemTaskDetailsArgsBinding, val listener: TasksDetailsAdapterListener?): RecyclerView.ViewHolder(itemTaskTriggerBinding.root) {

        fun bind(argsData: TaskDetailUi.Args) {
            itemTaskTriggerBinding.apply {
                title.text = argsData.title


                edit.setOnClickListener {
                    when(argsData.type) {
                        TaskDetailUiArgsType.action -> {
                            listener?.onEditActionClick(argsData.actionPosition)
                        }
                        TaskDetailUiArgsType.trigger -> {
                            listener?.onEditTriggerClick()
                        }
                    }
                }

                when(argsData.type) {
                    TaskDetailUiArgsType.action -> {
                        edit.setOnClickListener {
                            listener?.onEditActionClick(argsData.actionPosition)
                        }
                        delete.setOnClickListener {
                            listener?.onDeleteActionClick(argsData.actionPosition)
                        }
                        delete.visibility = View.VISIBLE
                    }
                    TaskDetailUiArgsType.trigger -> {
                        edit.setOnClickListener {
                            listener?.onEditTriggerClick()
                        }
                        delete.visibility = View.GONE
                    }
                }

                val keyValueList = listOf(
                        itemTaskTriggerBinding.keyValue0,
                        itemTaskTriggerBinding.keyValue1,
                        itemTaskTriggerBinding.keyValue2,
                        itemTaskTriggerBinding.keyValue3,
                        itemTaskTriggerBinding.keyValue4,
                )

                keyValueList.forEachIndexed { index, item ->
                    if(index < argsData.list.size ) {
                        item.root.visibility = View.VISIBLE
                        item.key.text = argsData.list[index].key
                        item.value.text = argsData.list[index].value
                    } else {
                        item.root.visibility = View.GONE
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup, listener: TasksDetailsAdapterListener?): ViewHolderArgs {
                val inflater = LayoutInflater.from(parent.context)
                return ViewHolderArgs(ItemTaskDetailsArgsBinding.inflate(inflater, parent, false), listener)
            }
        }
    }

    class DiffTaskUtil: DiffUtil.ItemCallback<TaskDetailUi>() {
        override fun areItemsTheSame(oldItem: TaskDetailUi, newItem: TaskDetailUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskDetailUi, newItem: TaskDetailUi): Boolean {
            return oldItem == newItem
        }
    }

}