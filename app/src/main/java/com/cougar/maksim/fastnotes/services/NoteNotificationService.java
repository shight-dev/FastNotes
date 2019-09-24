package com.cougar.maksim.fastnotes.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.cougar.maksim.fastnotes.AppState;
import com.cougar.maksim.fastnotes.activities.NoteCombinedActivity;
import com.cougar.maksim.fastnotes.dataClasses.Note;
import com.cougar.maksim.fastnotes.dbWork.NoteLab;
import com.cougar.maksim.fastnotes.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class NoteNotificationService extends IntentService {

    private static final String NOTE_NOTIFICATION_CHANNEL_ID = "NoteChannel";
    private static final int NOTIFICATION_ID = 0;
    private static final int INTERVAL = 1000 * 60; //60 seconds

    public NoteNotificationService() {
        super("DisplayNotification");
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, NoteNotificationService.class);
    }

    public static void setServiceAlarm(Context context) {
        Intent intent = NoteNotificationService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (AppState.State.getNotificationsEnable()) {
            /*alarmManager.setInexactRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime()
                    , INTERVAL, pendingIntent);*/
            alarmManager.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis()+INTERVAL,pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NoteLab noteLab = NoteLab.get(this);
        List<Note> noteList = noteLab.getActualNotes();
        int listSize = noteList.size();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        CharSequence name = getString(R.string.notification_channel_name);
        String description = getString(R.string.notification_channel_description);

        NotificationChannel notificationChannel = new NotificationChannel(NOTE_NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);

        if (listSize > 0) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, NoteCombinedActivity.Companion.newIntent(this, true), 0);
            Notification notification;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTE_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.presence_online)
                    .setContentTitle(getString(R.string.today_notes))
                    .setContentIntent(pendingIntent)
                    .setContentText("You have actual notes!")
                    .setAutoCancel(false)
                    .setOngoing(false);
            notification = builder.build();
            boolean isActive = Arrays.stream(notificationManager.getActiveNotifications()).anyMatch(activeNotification -> activeNotification.getId()== NOTIFICATION_ID);
            if(!isActive) {
                notificationManagerCompat.notify(NOTIFICATION_ID, notification);
            }
        } else {
            notificationManagerCompat.cancel(NOTIFICATION_ID);
        }
        //устанавливает очередное уведомление, повторяющиеся уведомления могут быть сброшены системой
        setServiceAlarm(this);
    }
}
