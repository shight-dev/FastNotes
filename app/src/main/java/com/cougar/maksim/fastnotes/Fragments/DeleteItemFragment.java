package com.cougar.maksim.fastnotes.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.cougar.maksim.fastnotes.DbWork.NoteLab;

import java.util.UUID;

public class DeleteItemFragment extends DialogFragment {

    public static final String ID = "id";

    private UUID itemId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        UUID id = (UUID)getArguments().getSerializable(ID);
        //TODO fix case with wrong data
        itemId = id;

        return new AlertDialog.Builder(getActivity())
                .setTitle("Delete item")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteLab noteLab = NoteLab.get(getActivity());
                        noteLab.deleteNote(itemId);
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    public static DeleteItemFragment newInstance(UUID id){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ID, id);

        DeleteItemFragment deleteItemFragment = new DeleteItemFragment();
        deleteItemFragment.setArguments(bundle);
        return deleteItemFragment;
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(ID, itemId);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
