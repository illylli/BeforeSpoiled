package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import cs165.edu.dartmouth.cs.beforespoiled.view.SlidingTabLayout;

public class MainActivity extends Activity {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private ArrayList<Fragment> mFragments;
    private ArrayList<ShoppingListItem> shoppingListItems;

    private Messenger mMessenger = new Messenger( new IncomingMessageHandler());
    private Messenger mServiceMessenger = null;
    private Message fragmentMessage;

    public final static int READSHOPPINGLIST = 1;
    public final static int SAVESHOPPINGLIST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, MainService.class);
        startService(i);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(ReminderFragment.newInstance());
            mFragments.add(ShoppingListFragment.newInstance());
            mFragments.add(SettingsFragment.newInstance());
        }

        mViewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);

        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        public static final int DISPLAY = 0;
        public static final int SHOPPING = 1;
        public static final int SETTINGS = 2;
        public static final String UI_TAB_DISPLAY = "DISPLAY";
        public static final String UI_TAB_SHOPPING = "SHOPPING";
        public static final String UI_TAB_SETTINGS = "SETTINGS";
        private ArrayList<Fragment> fragments;

        public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case DISPLAY:
                    return UI_TAB_DISPLAY;
                case SHOPPING:
                    return UI_TAB_SHOPPING;
                case SETTINGS:
                    return UI_TAB_SETTINGS;
                default:
                    break;
            }
            return null;
        }
    }

    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("Fanzy", "MainActivity:handleMessage");
            switch (msg.what) {
                case MainService.MSG_REGISTER_CLIENT:
                    Bundle bundle = msg.getData();
                    ((ReminderFragment) mFragments.get(0)).changeText(bundle.getString("hello"));
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
