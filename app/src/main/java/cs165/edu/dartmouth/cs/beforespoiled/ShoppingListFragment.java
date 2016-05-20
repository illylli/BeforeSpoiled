package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ShoppingListItem>>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View shoppingListView;
    private ListView shoppingList;
    private ShoppingListAdapter shoppingListAdapter;
    private List<ShoppingListItem> shoppingListItems;

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        shoppingListItems = new ArrayList<>();
//        shoppingListItems.add(new ShoppingListItem("a", a2, true));
//        shoppingListItems.add(new ShoppingListItem("B", 2, false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        updateList();

        shoppingListView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        shoppingList = (ListView) shoppingListView.findViewById(R.id.listView);
        shoppingListAdapter = new ShoppingListAdapter(getActivity().getApplicationContext(), shoppingListItems);
        shoppingList.setAdapter(shoppingListAdapter);
        return shoppingListView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_item, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    // create a dialog to get the input of the item
    // and add the item to the list
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                AlertDialog.Builder addItemBuilder = new AlertDialog.Builder(getActivity());
                addItemBuilder.setTitle("Please add a new item");
                final EditText itemText = new EditText(getActivity().getApplicationContext());
                addItemBuilder.setView(itemText);
                addItemBuilder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String itemInput = itemText.getText().toString();
                        ContentValues values = new ContentValues();
                        values.clear();

                        ShoppingListItem item = new ShoppingListItem();
                        item.setItemName(itemInput);
                        addNewItem(item);
                    }
                });

                addItemBuilder.setNegativeButton("Cancel", null);

                addItemBuilder.create().show();
                return true;
            default:
                return false;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    public void updateList(){
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    public void addNewItem(ShoppingListItem shoppingListItem){
        SaveShoppingItemToDatabase task = new SaveShoppingItemToDatabase(getActivity().getApplicationContext(), shoppingListItem);
        task.execute();
        shoppingListItems.add(shoppingListItem);
        shoppingListAdapter.clear();
        Log.d("TEST", shoppingListItems.size() + " length");
        shoppingListAdapter.addAll(shoppingListItems);
        shoppingListAdapter.notifyDataSetChanged();
    }

    public void deleteItem(int position){
        updateList();
        ShoppingListItem item = shoppingListItems.get(position);
        shoppingListItems.remove(position);

        DeleteShoppingItemFromDatabase task = new DeleteShoppingItemFromDatabase(getActivity().getApplicationContext(), item.getId());
        task.execute();

        shoppingListAdapter.clear();
        Log.d("TEST", shoppingListItems.size() + " length");
        shoppingListAdapter.addAll(shoppingListItems);
        shoppingListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<List<ShoppingListItem>> onCreateLoader(int i, Bundle bundle) {
        return new ReadShoppingListFromDatabase(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<ShoppingListItem>> loader, List<ShoppingListItem> items) {
        shoppingListAdapter.clear();
        Log.d("Database", "items " + items.size());
        shoppingListItems = items;
        Log.d("Database", "shoppingListItems " + shoppingListItems.size());
        shoppingListAdapter.addAll(shoppingListItems);
        Log.d("Database", "shoppingListItems changes");
        shoppingListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<ShoppingListItem>> loader) {
        Log.d("CS65", "RESET");
        shoppingListAdapter = new ShoppingListAdapter(getActivity().getApplicationContext(), new ArrayList<ShoppingListItem>());
        shoppingList.setAdapter(shoppingListAdapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
