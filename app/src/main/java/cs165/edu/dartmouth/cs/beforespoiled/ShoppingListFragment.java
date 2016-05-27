package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.vstechlab.easyfonts.EasyFonts;

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
public class ShoppingListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ShoppingListItem>>, SwipeRefreshLayout.OnRefreshListener {

    public static final int RESULT_OK = -1;
    protected static final int RESULT_SPEECH = 1;

    private View shoppingListView;
    private CircularProgressButton finishShoppingButton;
    private CircularProgressButton callTemplateButton;

    private CardArrayAdapter cardArrayAdapter;
    private ListView shoppingList;
    private List<ShoppingListItem> cardList;
    private SwipeRefreshLayout swipeRefreshLayout;


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

        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.header)
                .contentLayout(R.layout.fragment_shopping_list);
        helper.initActionBar(getActivity());

        shoppingListView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        shoppingList = (ListView) shoppingListView.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) shoppingListView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        shoppingList.addHeaderView(new View(getActivity()));
        shoppingList.addFooterView(new View(getActivity()));

        cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), cardList);
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
                        deleteItem(position - 1);
                        warningDialog.cancel();
                    }
                });
                warningDialog.show();
                return true;
            }
        });

        callTemplateButton = (CircularProgressButton) shoppingListView.findViewById(R.id.btn_call_template_activity);
        callTemplateButton.setTypeface(EasyFonts.caviarDreamsBold(getActivity().getApplicationContext()));
        callTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TemplateActivity.class);
                startActivity(intent);
            }
        });

        finishShoppingButton = (CircularProgressButton) shoppingListView.findViewById(R.id.btn_finish_shopping);
        finishShoppingButton.setTypeface(EasyFonts.caviarDreamsBold(getActivity().getApplicationContext()));
        finishShoppingButton.setIndeterminateProgressMode(true);
        finishShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ShoppingListItem> checkedItems = new ArrayList<>();
                int number = cardArrayAdapter.getCount();
                for (int i = 0; i < number; i++) {
                    ShoppingListItem item = cardArrayAdapter.getItem(i);
                    boolean checked = item.isSelected();
                    if (checked) checkedItems.add(item);
                }

                int checkedItemsNumber = checkedItems.size();
                if(checkedItemsNumber > 0){
//                    finishShoppingButton.setProgress(50);
                    Log.d("debug", "new list!!");
                    ShoppingLists shoppingLists = new ShoppingLists(new Date());
                    CreateShoppingList task = new CreateShoppingList(getActivity().getApplicationContext(), shoppingLists);
                    task.execute();
                    try {
                        shoppingLists = task.get();
                        long listID = shoppingLists.getId();
                        Log.d("debug", "ListId" + listID);
                        for(ShoppingListItem item : checkedItems){
                            item.setListId(listID);
                            UpdateShoppingItem update = new UpdateShoppingItem(getActivity().getApplicationContext(), item);
                            update.execute();
                            Log.d("debug", "ShoppingListItem" + item.getItemName() + " itemId " + item.getId());
                            finishShoppingButton.setProgress(100);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    finishShoppingButton.setProgress(-1);
                }
                Log.d("ITEM", cardList.size() + "shoppinglist size");
                cardList.removeAll(checkedItems);
                cardArrayAdapter.clear();
                cardArrayAdapter.addAll(cardList);
                cardArrayAdapter.notifyDataSetChanged();
                finishShoppingButton.setProgress(0);
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
            case R.id.speech:
                Log.d("lly", "access to speech.");
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
//                    txtText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getActivity().getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }

                return true;
            default:
                return false;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    ShoppingListItem listItem = new ShoppingListItem();
                    listItem.setItemName(text.get(0).toString());
                    Log.d("lly", text.get(0).toString());
                    listItem.setItemNumber(1);
                    listItem.setSelected(false);
                    addNewItem(listItem);
                }
                break;
            }

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
        try {
            card.setId(task.get().getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
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

