package utkrishtdhankar.projectneptune;

import android.content.res.Configuration;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public DatabaseHelper databaseHelper;

    //NEW DECLARATIONS
    private Toolbar toolbar;
    private View navHeader;
    private NavigationView navigationView;
    // toolbar titles respective to selected nav menu item
    private String[] activityTitles;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private Handler mHandler;
    Fragment fragment;
    FloatingActionButton fab;
    //-----------------

    //Navigation drawer titles
    private String[] menuItems;
    private DrawerLayout drawer;
    private ListView mDrawerList;
    //private ActionBarDrawerToggle mDrawerToggle;

    //This is the dataset which will be used for inflation
    private ArrayList<Task> myDataset = new ArrayList<Task>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView txtName,txtWebsite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        //Setting the main layout
        // Create a new fragment and specify the fragment to show based on position
        fragment = new InboxFragment();
        // Insert the fragment by replacing view of FrameLayout in main_activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menuItems = getResources().getStringArray(R.array.menuItems);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        fab = (FloatingActionButton) findViewById(R.id.addButton);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();
        Log.d("MainActivity.java","WE REACHED THIS POINT");
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        //OPEN FRAGMENT HERE
                        navItemIndex = 0;
                        fragment = new InboxFragment();
                        // Insert the fragment by replacing view of FrameLayout in main_activity

                        break;
                    case R.id.nav_settings:
                        //OPEN FRAGMENT HERE
                        navItemIndex = 1;
                        fragment = new SettingsFragment();
                        break;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }

    private void loadHomeFragment() {
        //selectNavView
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);

        //set Tool Bar title
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                // Create a new fragment and specify the fragment to show based on position
                Fragment fragment = getHomeFragment();

                // Insert the fragment by replacing view of FrameLayout in main_activity
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
            }
        };

        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                InboxFragment inboxFragment = new InboxFragment();
                return inboxFragment;
            case 1:
                // settings fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return new InboxFragment();
        }
    }



    private void loadNavHeader() {
        // name, email
        txtName.setText("Shreyak Kumar");
        txtWebsite.setText("kumarshreyak@gmail.com");
    }


    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        Fragment fragment = new InboxFragment();
        // Create a new fragment and specify the fragment to show based on position
        switch(position)
        {
            case 0:
                fragment = new InboxFragment();
                break;
            case 1:
                fragment = new SettingsFragment();
                break;
        }
        //Fragment fragment = new SettingsFragment();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(menuItems[position]);
        drawer.closeDrawer(mDrawerList);
    }



    public void onFABPress(View view) {
        FragmentManager fm = getSupportFragmentManager();
        InputFragment inputFragment = InputFragment.newInstance("Some Title",getApplicationContext());
        inputFragment.show(fm, "fragment_edit_name");
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

}
