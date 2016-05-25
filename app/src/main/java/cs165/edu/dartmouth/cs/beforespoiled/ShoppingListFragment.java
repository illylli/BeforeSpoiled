package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Loader;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cs165.edu.dartmouth.cs.beforespoiled.database.CreateShoppingList;
import cs165.edu.dartmouth.cs.beforespoiled.database.DeleteShoppingItemFromDatabase;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReadShoppingListFromDatabase;
import cs165.edu.dartmouth.cs.beforespoiled.database.SaveShoppingItemToDatabase;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingLists;
import cs165.edu.dartmouth.cs.beforespoiled.database.UpdateShoppingItem;
import cs165.edu.dartmouth.cs.beforespoiled.view.CardArrayAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ShoppingListItem>> {
    private View shoppingListView;
    private Button finishShoppingButton;

    private CardArrayAdapter cardArrayAdapter;
    private ListView shoppingList;
    private List<ShoppingListItem> cardList;


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

        cardList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        updateList();

        shoppingListView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        shoppingList = (ListView) shoppingListView.findViewById(R.id.listView);

        shoppingList.addHeaderView(new View(getActivity()));
        shoppingList.addFooterView(new View(getActivity()));

        cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), cardList);
//        shoppingListAdapter = new ShoppingListAdapter(getActivity().getApplicationContext(), shoppingListItems);
//        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(shoppingListAdapter);
//        animationAdapter.setAbsListView(shoppingList);
//        shoppingList.setAdapter(animationAdapter);
        shoppingList.setAdapter(cardArrayAdapter);
//        shoppingList.setDraggableManager(new TouchViewDraggableManager(R.id.itemrow_gripview));
        shoppingList.setLongClickable(true);
        shoppingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("daniel", "long press");
                final SweetAlertDialog warningDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                warningDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                warningDialog.setTitleText("Delete this item?");
                warningDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        SweetAlertDialog successDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                        successDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        successDialog.setTitle("Deleted!");
                        successDialog.setTitleText("This item is deleted!");
                        successDialog.show();
                        deleteItem(position);
                        warningDialog.cancel();
                    }
                });
                warningDialog.show();
                return true;
            }
        });

        finishShoppingButton = (Button) shoppingListView.findViewById(R.id.btn_finish_shopping);
        finishShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardArrayAdapter temp = cardArrayAdapter;
                ShoppingLists shoppingLists = new ShoppingLists(new Date());
//                ShoppingLists shoppingLists = new ShoppingLists(new Date(), item.getItemName(), item.getItemNumber());
                CreateShoppingList task = new CreateShoppingList(getActivity().getApplicationContext(), shoppingLists);
                task.execute();

                try {
                    shoppingLists = task.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                long listID = shoppingLists.getId();
                Log.d("ITEM", "listID" + listID);
                ArrayList<ShoppingListItem> index = new ArrayList<>();
//                ArrayList<Long> poses = new ArrayList<>();
                int number = temp.getCount();
                for (int i = 0; i < number; i++) {
                    ShoppingListItem item = (ShoppingListItem) temp.getItem(i);
                    boolean checked = item.isSelected();
                    // for test
//                    index.add(item);
                    if (checked) {
                        item.setListId(listID);
                        index.add(item);
//                        poses.add(item.getId());
                        UpdateShoppingItem update = new UpdateShoppingItem(getActivity().getApplicationContext(), item);
                        update.execute();
                        Log.d("ITEM", item.getItemName() + "checked");
                    }

                }
//                DeleteShoppingItemFromDatabase task2 = new DeleteShoppingItemFromDatabase(getActivity().getApplicationContext(), poses);
//                task2.execute();
                Log.d("ITEM", cardList.size() + "shoppinglist size");
                cardList.removeAll(index);
                cardArrayAdapter.clear();
                cardArrayAdapter.addAll(cardList);
                cardArrayAdapter.notifyDataSetChanged();
            }
        });

        return shoppingListView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // create a dialog to get the input of the item
    // and add the item to the list
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add an item");
                final EditText itemText = new EditText(getActivity());
                builder.setView(itemText);
                builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String itemName = itemText.getText().toString();
                        ContentValues values = new ContentValues();
                        values.clear();
                        ShoppingListItem listItem = new ShoppingListItem();
                        listItem.setItemName(itemName);
                        listItem.setItemNumber(1);
                        listItem.setSelected(false);
                        addNewItem(listItem);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();
                return true;
            default:
                return false;
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void updateList(){
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    public void addNewItem(ShoppingListItem card) {
        SaveShoppingItemToDatabase task = new SaveShoppingItemToDatabase(getActivity().getApplicationContext(), card);
        task.execute();
        cardList.add(card);
        cardArrayAdapter.clear();
        cardArrayAdapter.addAll(cardList);
        cardArrayAdapter.notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ShoppingListItem card = cardList.get(position);
        cardList.remove(position);

        DeleteShoppingItemFromDatabase task = new DeleteShoppingItemFromDatabase(getActivity().getApplicationContext(), card.getId());
        task.execute();

        cardArrayAdapter.clear();
        cardArrayAdapter.addAll(cardList);
        cardArrayAdapter.notifyDataSetChanged();
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
        cardArrayAdapter.clear();
        cardList = items;
        cardArrayAdapter.addAll(cardList);
        cardArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<ShoppingListItem>> loader) {
        cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), new ArrayList<ShoppingListItem>());
        shoppingList.setAdapter(cardArrayAdapter);
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
        void onFragmentInteraction(Uri uri);
    }


}
