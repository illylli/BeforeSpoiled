package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.DialogFragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ImageFragment extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
        // Inflate the layout for this fragment
        ImageView view = new ImageView(getActivity());
        view.setImageBitmap(BitmapFactory.decodeByteArray(image, 100, image.length));
        container.addView(view);
        return view;
    }
}
