package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.database.Label;
import cs165.edu.dartmouth.cs.beforespoiled.database.LabelDataSource;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntry;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntryDataSource;

public class ReminderReceiver extends BroadcastReceiver {
    public ReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ReminderEntryDataSource reminderEntryDataSource = new ReminderEntryDataSource(context);
        reminderEntryDataSource.open();
        List<ReminderEntry> entries = reminderEntryDataSource.fetchEntries();
        reminderEntryDataSource.close();
        LabelDataSource labelDataSource = new LabelDataSource(context);
        labelDataSource.open();
        List<Label> labels = labelDataSource.fetchEntries();
        labelDataSource.close();

        List<ReminderEntry> spoilingEntry = new ArrayList<>();

        for (ReminderEntry entry : entries) {
            int dayBeforeSpoiled = labels.get(entry.getLabel()).getDaysBeforeSpoiled();
            Calendar notifyDate = Calendar.getInstance();
            notifyDate.setTime(entry.getExpireDate().getTime());
            notifyDate.add(Calendar.DATE, -dayBeforeSpoiled);
            if (notifyDate.before(Calendar.getInstance())) {
                spoilingEntry.add(entry);
            }
        }

        if (spoilingEntry.isEmpty()) {
            return;
        }

        StringBuffer buffer = new StringBuffer();
        if (spoilingEntry.size() <= 3) {
            buffer.append("Your ");
            for (ReminderEntry entry : spoilingEntry) {
                buffer.append(entry.getName() + " ");
            }
            if (spoilingEntry.size() == 1) {
                buffer.append("is being spoiled...");
            } else {
                buffer.append("are being spoiled...");
            }
        } else {
            buffer.append(String.format("Your have %d foods being spoiled...", spoilingEntry.size()));
        }

        //show notification
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                i, 0);
        Notification notification = new Notification.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(buffer.toString())
                .setSmallIcon(R.mipmap.appicon2)
                .setContentIntent(contentIntent).build();

        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
    }
}
