package com.cougar.maksim.fastnotes.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.cougar.maksim.fastnotes.DataClasses.NoteStatus
import com.cougar.maksim.fastnotes.R

class StatusFragment : DialogFragment() {

    companion object {
        const val STATUS: String = "status"

        fun newInstance(status: String): StatusFragment {
            val bundle: Bundle = Bundle()
            bundle.putSerializable(STATUS, status)

            val statusFragment = StatusFragment()
            statusFragment.arguments = bundle
            return statusFragment
        }
    }

    lateinit var mStatusGroup: RadioGroup

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val statusObj = arguments?.getSerializable(STATUS)
        val status: String = if (statusObj is String) statusObj else ""

        val view: View = LayoutInflater.from(activity).inflate(R.layout.status_fragment, null)
        mStatusGroup = view.findViewById(R.id.statusRadioGroup)

        val statusList = NoteStatus.values()
        for ((counter, dbStatus) in statusList.withIndex()) {
            val radioBtn = RadioButton(activity)
            //TODO can be reworked with companion object ans stringvals without counter
            radioBtn.text = dbStatus.stringVal
            radioBtn.id = counter
            mStatusGroup.addView(radioBtn, counter)
            if (status.contentEquals(dbStatus.toString())) {
                mStatusGroup.check(counter)
            }
        }

        return AlertDialog.Builder(activity)
                .setTitle("Chose status")
                .setView(mStatusGroup)
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    val checkedId = mStatusGroup.checkedRadioButtonId
                    val selectedBtn: Button = mStatusGroup.findViewById(checkedId)
                    val noteStatus: NoteStatus = NoteStatus.values()[selectedBtn.id]
                    sendResult(Activity.RESULT_OK, noteStatus)
                }
                .create()
    }

    private fun sendResult(resultCode: Int, status: NoteStatus) {
        val targetFragment = this.targetFragment
        val intent: Intent = Intent()
        intent.putExtra(STATUS, status)
        targetFragment?.onActivityResult(targetRequestCode, resultCode, intent)
    }
}