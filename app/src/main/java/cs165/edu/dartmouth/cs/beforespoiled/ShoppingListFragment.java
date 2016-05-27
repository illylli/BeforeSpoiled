package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
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
    private ImageButton speechButton;
    private TextView addItem;

    private OnFragmentInteractionListener mListener;

    private BroadcastReceiver receiverSync = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) { cardArrayAdapter.notifyDataSetChanged(); }
    };

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
        updateList();

        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.header)
                .contentLayout(R.layout.fragment_shopping_list);
        helper.initActionBar(getActivity());

        IntentFilter filter = new IntentFilter("Check");
        getActivity().registerReceiver(receiverSync, filter);

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
        addItem = (EditText) shoppingListView.findViewById(R.id.add_item);
        addItem.setCursorVisible(false);
        addItem.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_NULL:
                        System.out.println("null for default_content: " + v.getText());
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        System.out.println("action send for email_content: " + v.getText());
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        String itemName = addItem.getText().toString();
                        if(!itemName.equals("")) {
                            addItem.clearFocus();
                            addItem.setCursorVisible(false);
                            ShoppingListItem listItem = new ShoppingListItem();
                            listItem.setItemName(itemName);
                            listItem.setItemNumber(1);
                            listItem.setSelected(false);
                            addNewItem(listItem);
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(addItem.getWindowToken(), 0);
                        }
                        break;
                }
                return true;
            }
        });

        addItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    addItem.setText("");
                }
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (v.getId() != R.id.add_item) {
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    addItem.setCursorVisible(false);
//                    imm.hideSoftInputFromWindow(addItem.getWindowToken(), 0);
//                    Log.d("Edittext", "Clcik outside1");
//                }
                addItem.setCursorVisible(true);
            }
        });

        cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), cardList);
        shoppingList.setAdapter(cardArrayAdapter);

        shoppingList.setLongClickable(true);
        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.check_box);
                TextView tv = (TextView) view.findViewById(R.id.item_name);
                ShoppingListItem item = cardList.get(position - 1);
                UpdateShoppingItem task;
                if (checkBox.isChecked()) {
                    item.setSelected(false);
                    task = new UpdateShoppingItem(getActivity().getApplicationContext(), item);
                    task.execute();
                } else {
                    item.setSelected(true);
                    task = new UpdateShoppingItem(getActivity().getApplicationContext(), item);
                    task.execute();
                }
                cardArrayAdapter.notifyDataSetChanged();
            }
        });

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
                if (checkedItemsNumber > 0) {
//                    finishShoppingButton.setProgress(50);
                    Log.d("debug", "new list!!");
                    ShoppingLists shoppingLists = new ShoppingLists(new Date());
                    CreateShoppingList task = new CreateShoppingList(getActivity().getApplicationContext(), shoppingLists);
                    task.execute();
                    try {
                        shoppingLists = task.get();
                        long listID = shoppingLists.getId();
                        Log.d("debug", "ListId" + listID);
                        for (ShoppingListItem item : checkedItems) {
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


        speechButton = (ImageButton) shoppingListView.findViewById(R.id.SpeakButton2);
        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getActivity().getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
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
//            case R.id.add_item:
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Add an item");
//                final EditText itemText = new EditText(getActivity());
//                builder.setView(itemText);
//                builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String itemName = itemText.getText().toString();
//                        ContentValues values = new ContentValues();
//                        values.clear();
//                        ShoppingListItem listItem = new ShoppingListItem();
//                        listItem.setItemName(itemName);
//                        listItem.setItemNumber(1);
//                        listItem.setSelected(false);
//                        addNewItem(listItem);
//                    }
//                });
//                builder.setNegativeButton("Cancel", null);
//                builder.create().show();
//                return true;
//            case R.id.speech:
//                Log.d("lly", "access to speech.");
//                Intent intent = new Intent(
//                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
//
//                try {
//                    startActivityForResult(intent, RESULT_SPEECH);
//                    txtText.setText("");
//                } catch (ActivityNotFoundException a) {
//                    Toast t = Toast.makeText(getActivity().getApplicationContext(),
//                            "Opps! Your device doesn't support Speech to Text",
//                            Toast.LENGTH_SHORT);
//                    t.show();
//                }
//
//                return true;
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
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        switch(actionId){
//            case EditorInfo.IME_NULL:
//                System.out.println("null for default_content: " + v.getText() );
//                break;
//            case EditorInfo.IME_ACTION_SEND:
//                System.out.println("action send for email_content: "  + v.getText());
//                break;
//            case EditorInfo.IME_ACTION_DONE:
//                Log.d("ADD", "laimei");
//                System.out.println("action done for number_content: "  + v.getText());
//                String itemName = addItem.getText().toString();
//                ShoppingListItem listItem = new ShoppingListItem();
//                listItem.setItemName(itemName);
//                listItem.setItemNumber(1);
//                listItem.setSelected(false);
//                addNewItem(listItem);
//                break;
//        }
        //Toast.makeText(this, v.getText()+"--" + actionId, Toast.LENGTH_LONG).show();
//        return true;
//    }

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
