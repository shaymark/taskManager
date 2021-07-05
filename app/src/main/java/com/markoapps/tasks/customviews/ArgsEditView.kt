package com.markoapps.tasks.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.markoapps.tasks.databinding.ViewTaskDetailsEditBinding
import com.markoapps.tasks.uimodels.KeyValuePair

class ArgsEditView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attributeSet, defStyleAttr) {

    private val itemTaskDetailsEditBinding =
            ViewTaskDetailsEditBinding.inflate(LayoutInflater.from(context), this, true)

    private var argsListOrigin: List<KeyValuePair>? = null

    fun setArgs(argsList: List<KeyValuePair>) {
        argsListOrigin = argsList
        itemTaskDetailsEditBinding.apply {

            val keyValueList = listOf(
                    keyValue0,
                    keyValue1,
                    keyValue2,
                    keyValue3,
                    keyValue4,
            )

            keyValueList.forEachIndexed { index, item ->
                if (index < argsList.size) {
                    item.root.visibility = View.VISIBLE
                    val arg = argsList[index]
                    item.key.text = arg.key
                    item.value.setText(arg.value)
                    item.isEnabled.isChecked = arg.isEnabled
                } else {
                    item.root.visibility = View.GONE
                }
            }
        }
    }

    fun getArgs() : List<KeyValuePair>? {
        itemTaskDetailsEditBinding.apply {
            if(argsListOrigin == null) return null

            val keyValueList = listOf(
                    keyValue0,
                    keyValue1,
                    keyValue2,
                    keyValue3,
                    keyValue4,
            )
            return argsListOrigin!!.mapIndexed{ index, item ->

                item.copy(
                        value = keyValueList[index].value.text.toString()
                )

            }


        }


    }
}




