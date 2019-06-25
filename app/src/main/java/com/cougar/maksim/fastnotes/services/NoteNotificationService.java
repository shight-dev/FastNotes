package com.cougar.maksim.fastnotes.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.cougar.maksim.fastnotes.activities.NoteCombinedActivity;
import com.cougar.maksim.fastnotes.dataClasses.Note;
import com.cougar.maksim.fastnotes.dbWork.NoteLab;
import com.cougar.maksim.fastnotes.R;

import java.util.List;


public class NoteNotificationService extends IntentService {

    private static final String TAG = "NoteService";
    private static final String NOTE_NOTIFICATION_CHANNEL_ID = "NoteChannel";
    private static final int NOTIFICATION_ID = 0;
    private static final int NOTIFICATION_CHANNEL_ID = 0;
    private static final int INTERVAL = 1000 * 60; //60 seconds

    public NoteNotificationService() {
        super("DisplayNotification");
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, NoteNotificationService.class);
    }

    public static void setServiceAlarm(Context context, boolean isWorking) {
        Intent intent = NoteNotificationService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isWorking) {
            alarmManager.setInexactRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime()
                    , INTERVAL, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NoteLab noteLab = NoteLab.get(this);
        List<Note> noteList = noteLab.getTodayNotes();
        int listSize = noteList.size();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        //TODO check current sdk version and create (must be 26 or above)
        /*CharSequence name = getString(R.string.notification_channel_name);
        String description = getString(R.string.notification_channel_description);
        int importance = NotificationManagerCompat.IMPORTANCE_LOW;

        NotificationChannel notificationChannel = new NotificationChannel(NOTE_NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);*/

        if (listSize > 0) {
            //TODO rework with new arhitecture
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, NoteCombinedActivity.Companion.newIntent(this, true), 0);
            Notification notification;
            if (listSize != 1) {
                //TODO may produce error with channel id
                notification = new NotificationCompat.Builder(this/*, NOTE_NOTIFICATION_CHANNEL_ID*/)
                        .setSmallIcon(android.R.drawable.presence_online)
                        .setContentTitle(getString(R.string.today_notes))
                        .setContentText("You have " + listSize + " notes!")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .build();
            } else {
                notification = new NotificationCompat.Builder(this/*, NOTE_NOTIFICATION_CHANNEL_ID*/)
                        .setSmallIcon(android.R.drawable.presence_online)
                        .setContentTitle(getString(R.string.today_notes))
                        .setContentText("You have " + listSize + " note!")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .build();
            }

            notificationManagerCompat.notify(NOTIFICATION_ID, notification);
        } else {
            notificationManagerCompat.cancel(NOTIFICATION_ID);
        }
    }
}
