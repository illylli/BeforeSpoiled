package cs165.edu.dartmouth.cs.beforespoiled;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.app.Fragment;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReminderFragment extends Fragment {


    public ReminderFragment() {

    }

    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        ((Button) view.findViewById(R.id.btn_change)).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                changeClicked();
            }
        });

        return view;
    }

    public void changeClicked() {
        Message msg = Message.obtain(null, MainService.MSG_REGISTER_CLIENT);
    }

    public void changeText(String text){
        ((TextView)getView().findViewById(R.id.tv_test)).setText(text);
    }


}
