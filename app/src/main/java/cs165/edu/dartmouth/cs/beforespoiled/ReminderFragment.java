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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import cs165.edu.dartmouth.cs.beforespoiled.view.ReminderGridAdapter;

public class ReminderFragment extends Fragment {


    public ReminderFragment() {

    }

    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gv_reminder);
        gridView.setAdapter(new ReminderGridAdapter(getActivity()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // send necessary parameters to SubmitActivity
                Log.d("Fanzy", "click on " + position);
            }
        });

        return view;
    }
    
}
