package com.cougar.maksim.fastnotes.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cougar.maksim.fastnotes.services.NoteNotificationService;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NoteNotificationService.setServiceAlarm(context, true);
    }
}
