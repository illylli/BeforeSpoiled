package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ImageFragment extends DialogFragment {

    private static final String ARG_IMAGE = "image";
    private byte[] image;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(byte[] image) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putByteArray(ARG_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fanzy", "ImageFragment: OnCreate");
        if (getArguments() != null) {
            image = getArguments().getByteArray(ARG_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_image, container, false);
    }
}
