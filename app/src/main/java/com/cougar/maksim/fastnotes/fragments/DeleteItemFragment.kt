package com.cougar.maksim.fastnotes.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

import com.cougar.maksim.fastnotes.dbWork.NoteLab

import java.util.UUID

class DeleteItemFragment : DialogFragment() {

    private var itemId: UUID? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val id = arguments?.getSerializable(ID) as UUID
        itemId = id

        return AlertDialog.Builder(activity!!)
                .setTitle("Delete item")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val noteLab = NoteLab.get(activity)
                    noteLab.deleteNote(itemId)
                    sendResult(Activity.RESULT_OK)
                }
                .create()
    }

    private fun sendResult(resultCode: Int) {
        if (targetFragment == null) {
            return
        }

        val intent = Intent()
        intent.putExtra(ID, itemId)

        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }

    companion object {

        const val ID = "id"

        fun newInstance(id: UUID): DeleteItemFragment {
            val bundle = Bundle()
            bundle.putSerializable(ID, id)

            val deleteItemFragment = DeleteItemFragment()
            deleteItemFragment.arguments = bundle
            return deleteItemFragment
        }
    }
}
