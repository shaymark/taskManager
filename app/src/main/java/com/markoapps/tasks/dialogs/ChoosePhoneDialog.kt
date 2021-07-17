package com.markoapps.tasks.dialogs

import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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
import com.markoapps.tasks.adapters.PhonesAdapter
import com.markoapps.tasks.databinding.ChoosePhoneDialogBinding
import kotlinx.coroutines.*
import java.io.Serializable


class ChoosePhoneDialog(context: Context, val phoneInfoCallback: (PhoneInfo) -> Unit): Dialog(context) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private var _binding: ChoosePhoneDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var listAdapter: PhonesAdapter? = null

    private var phoneInfoList: List<PhoneInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ChoosePhoneDialogBinding.inflate(layoutInflater, null, false)
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

    private fun initViews() {

        binding.apply {
            listAdapter = PhonesAdapter {
                phoneInfoCallback(it)
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
                    refreshContants(s.toString())
                }
            })

            back.setOnClickListener {
                dismiss()
            }
        }

        refreshContants(null)

    }


    fun refreshContants(filter: String?) {
        scope.launch (Dispatchers.Default) {
            phoneInfoList = phoneInfoList ?: getContacts(context)
            val filteredPhoneInfoList = phoneInfoList?.filter {
                it.name.contains(filter ?: "")
            }
            withContext(Dispatchers.Main) {
                listAdapter?.submitList(filteredPhoneInfoList)
            }
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        scope.cancel()
    }

    fun getContacts(ctx: Context): List<PhoneInfo> {
        val list: MutableList<PhoneInfo> = ArrayList()
        val contentResolver: ContentResolver = ctx.getContentResolver()
        val cursor: Cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)!!
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                val id: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val cursorInfo: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
//                    val inputStream: InputStream =
//                        ContactsContract.Contacts.openContactPhotoInputStream(
//                            ctx.getContentResolver(),
//                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong())
//                        )
                    val person: Uri =
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong())
                    val pURI: Uri = Uri.withAppendedPath(
                        person,
                        ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                    )
//                    var photo: Bitmap? = null
//                    if (inputStream != null) {
//                        photo = BitmapFactory.decodeStream(inputStream)
//                    }
                    while (cursorInfo?.moveToNext() == true) {
                        val info = PhoneInfo(
                            id,
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                            cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                            pURI
                        )
                        list.add(info)
                    }
                    cursorInfo?.close()
                }
            }
            cursor.close()
        }
        return list
    }


}



data class PhoneInfo(
    val id: String,
    val name: String,
    val mobileNumber: String,
    val photoURI: Uri
): Serializable