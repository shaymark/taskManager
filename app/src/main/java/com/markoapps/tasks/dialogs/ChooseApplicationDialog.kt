package com.markoapps.tasks.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.markoapps.tasks.adapters.ApplicationsAdapter
import com.markoapps.tasks.databinding.ChooseApplicationDialogBinding
import com.markoapps.tasks.databinding.ChoosePhoneDialogBinding
import kotlinx.coroutines.*
import java.io.Serializable

class ChooseApplicationDialog(context: Context, val appInfoCallback: (AppInfo) -> Unit): Dialog(context) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private var _binding: ChooseApplicationDialogBinding? = null

    private val binding get() = _binding!!

    private var listAdapter: ApplicationsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ChooseApplicationDialogBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        val window = this.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT)

        initViews()

        setOnDismissListener {
            scope.cancel()
            _binding = null
        }
    }

    fun initViews(){

        getApplicationList()

        binding.apply {
            listAdapter = ApplicationsAdapter {
                appInfoCallback(it)
                dismiss()
            }

            list.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(context)
            }

            filter.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    refreshApps(s.toString())
                }
            })

            back.setOnClickListener {
                dismiss()
            }
        }

        refreshApps(null)

    }



    fun refreshApps(filter: String?) {
        scope.launch (Dispatchers.Default) {
            val appInfoList = getAppInfo(filter)
            withContext(Dispatchers.Main) {
                listAdapter?.submitList(appInfoList)
            }
        }
    }

    fun getAppInfo(fillter: String?): List<AppInfo> {
        return getApplicationListResulver().filter {
            it.packageName.contains(fillter ?: "") ||
                    it.appName.contains(fillter ?: "")
        }
    }


    fun getApplicationList() : List<ApplicationInfo> {
        val pm: PackageManager = context.packageManager
        return pm.getInstalledApplications(PackageManager.GET_META_DATA)
    }

    fun getApplicationListResulver(): List<AppInfo> {
        val pm: PackageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgAppsList = context.packageManager.queryIntentActivities(mainIntent, 0)
        return pkgAppsList.map {
            AppInfo(
                it.loadLabel(pm).toString(),
                it.activityInfo.packageName,
                it.loadIcon(pm)
            )
        }
    }
}

data class AppInfo(
    val appName: String,
    val packageName: String,
    val appIcon: Drawable
): Serializable