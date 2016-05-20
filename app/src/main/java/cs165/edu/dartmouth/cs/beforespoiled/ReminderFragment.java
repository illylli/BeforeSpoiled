package cs165.edu.dartmouth.cs.beforespoiled;

import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.util.Log;

public class ReminderFragment extends Fragment {
    public ReminderFragment() {

    }

    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }

    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("Fanzy", "ReminderFragment:IncomingHandler:handleMessage");
            switch (msg.what) {
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
