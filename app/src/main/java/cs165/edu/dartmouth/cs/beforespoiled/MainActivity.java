package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v13.app.FragmentPagerAdapter;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.view.SlidingTabLayout;


public class MainActivity extends Activity {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private ArrayList<Fragment> mFragments;
    private ArrayList<ShoppingListItem> shoppingListItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        ReminderScheduler.setSchedule(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public static final int DISPLAY = 0;
        public static final int SHOPPING = 1;
        public static final int SETTINGS = 2;
        public static final String UI_TAB_DISPLAY = "REMINDER";
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
}
