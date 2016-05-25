package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import cs165.edu.dartmouth.cs.beforespoiled.database.Label;
import cs165.edu.dartmouth.cs.beforespoiled.database.LabelAsyncTask;

public class SingleLabelFragment extends DialogFragment {
    private static final String ARG_LABEL = "label";

    private Label label;

    public SingleLabelFragment() {
        // Required empty public constructor
    }

    public static SingleLabelFragment newInstance(Label label) {
        SingleLabelFragment fragment = new SingleLabelFragment();
        if(label != null) {
            Bundle args = new Bundle();
            args.putString(ARG_LABEL, (new Gson()).toJson(label));
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            label = (new Gson()).fromJson(getArguments().getString(ARG_LABEL), Label.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_label, container, false);
        final EditText name = (EditText) v.findViewById(R.id.et_settings_labels_single_name);
        final EditText period = (EditText) v.findViewById(R.id.et_settings_labels_single_period);
        final EditText days = (EditText) v.findViewById(R.id.et_settings_labels_single_days);
        if(label != null) {
            name.setText(label.getName());
            period.setText(label.getStoragePeriod() + "");
            if(label.getDaysBeforeSpoiled() != null){
                days.setText(label.getDaysBeforeSpoiled()+"");
            }
        }

        ((Button) v.findViewById(R.id.btn_settings_labels_single_save)).setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                boolean newLabel = false;
                if(label == null){
                    newLabel = true;
                    label = new Label();
                }
                if(!name.getText().toString().equals("")){
                    label.setName(name.getText().toString());
                }
                if(!period.getText().toString().equals("")){
                    label.setStoragePeriod(Integer.parseInt(period.getText().toString()));
                }
                if(!days.getText().toString().equals("")){
                    label.setDaysBeforeSpoiled(Integer.parseInt(days.getText().toString()));
                }
                if(newLabel) {
                    new LabelAsyncTask(getActivity()).execute(LabelAsyncTask.INSERT, label);
                }else{
                    new LabelAsyncTask(getActivity()).execute(LabelAsyncTask.UPDATE, label);
                }
                getDialog().cancel();
            }
        });

        ((Button) v.findViewById(R.id.btn_settings_labels_single_cancel)).setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        return v;
    }
}