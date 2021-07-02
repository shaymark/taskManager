package com.markoapps.tasks.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.afollestad.materialdialogs.utils.MDUtil.inflate
import com.markoapps.tasks.R
import com.markoapps.tasks.databinding.ItemTaskDetailsArgsBinding

class ArgsView constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attributeSet, defStyleAttr) {

    private val binding =
            ItemTaskDetailsArgsBinding.inflate(LayoutInflater.from(context), this, true)

    fun setArgs() {

    }


}




