package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.Calendar;

public class MainService extends Service {
    public MainService() {
    }

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_UPDATE_ENTRY = 3;
    private static boolean isRunning = false;
    private final Messenger mMessenger = new Messenger(
            new IncomingMessageHandler());
    private NotificationManager mNotificationManager;
    private Messenger mClient = null;

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public void onCreate() {
        Log.d("Fanzy", "TrackingService onCreate");
        super.onCreate();

        isRunning = true;
        showNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        mNotificationManager.cancelAll();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Fanzy", "Service:onBind() - return mMessenger.getBinder()");

        // getBinder()
        // Return the IBinder that this Messenger is using to communicate with
        // its associated Handler; that is, IncomingMessageHandler().

        return mMessenger.getBinder();
    }

    /**
     * Display a notification in the notification bar.
     */
    private void showNotification() {

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(
                        getResources().getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentIntent(contentIntent).build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags = notification.flags
                | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(0, notification);
    }

    /**
     * Handle incoming messages from MainActivity
     */
    private class IncomingMessageHandler extends Handler { // Handler of
        // incoming messages
        // from clients.
        @Override
        public void handleMessage(Message msg) {
            Log.d("Fanzy", "Service:handleMessage: " + msg.what);
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    break;
                case MSG_UNREGISTER_CLIENT:;
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
