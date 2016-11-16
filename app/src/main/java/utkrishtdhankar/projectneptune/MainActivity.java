package utkrishtdhankar.projectneptune;

import android.app.DatePickerDialog;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public DatabaseHelper databaseHelper;

    private Toolbar toolbar;
    private View navHeader;
    private NavigationView navigationDrawer;
    private String[] navDrawerItemNames;
    private DrawerLayout drawerLayout;
    private ListView navDrawerMenuList;
    private TextView navHeaderName, navHeaderSubText;

    // Toolbar titles respective to selected nav menu item
    private String[] activityTitles;
    // Index to identify current nav menu item
    public static int navItemIndex = 0;

    private Handler mHandler;
    Fragment fragment;
    FloatingActionButton fab;
    //-----------------


    /**
     * Creates a new ToolBar and sets it as Action Bar
     * Opens the Inbox Fragment
     * Creates the Navigation Drawer
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        // Setting the custom toolbar as the Action Bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        // Setting the main layout
        // Create a new fragment and inserting the fragment by replacing view of FrameLayout in main_activity
        fragment = new InboxFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();

        // Finding all the elements
        navigationDrawer = (NavigationView) findViewById(R.id.nav_view);
        navDrawerItemNames = getResources().getStringArray(R.array.nav_item_titles);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navHeader = navigationDrawer.getHeaderView(0);
        navHeaderName = (TextView) navHeader.findViewById(R.id.name);
        navHeaderSubText = (TextView) navHeader.findViewById(R.id.website);
        fab = (FloatingActionButton) findViewById(R.id.addButton);

        // Load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_toolbar_titles);

        // Load nav menu header data
        loadNavHeader();

        // Initializing navigation menu
        setUpNavigationView();
        Log.d("MainActivity.java","WE REACHED THIS POINT");
    }

    /**
     *
     */
    private void setUpNavigationView() {

        //Setting click listeners for items in the navigation drawer
        navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    // Creating the fragment which will replace the current fragment
                    // Setting the selected item's index
                    case R.id.nav_home:
                        navItemIndex = 0;
                        fragment = new InboxFragment();
                        break;

                    case R.id.nav_context:
                        navItemIndex = 1;
                        fragment = new ContextsFragment();
                        break;

                    case R.id.nav_settings:
                        navItemIndex = 2;
                        fragment = new SettingsFragment();
                        break;
                }

                // Replacing the current fragment
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

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawerLayout closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawerLayout open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        // Setting the actionbarToggle(clicking the hamburger) to open Navigation drawer
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        // Calling sync state to set the hamburger icon
        actionBarDrawerToggle.syncState();
    }

    /**
     *
     */
    private void loadHomeFragment() {

        // Setting the selected Nav drawer item as checked
        navigationDrawer.getMenu().getItem(navItemIndex).setChecked(true);

        // Setting the Toolbar's Title
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // Update the main content by replacing fragments
                // Create a new fragment and specify the fragment to show based on position
                Fragment fragment = getHomeFragment();

                // Insert the fragment by replacing view of FrameLayout in main_activity
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
            }
        };

        toggleFab();

        // Closing drawerLayout on item click
        drawerLayout.closeDrawers();

        // Refresh toolbar menu
        invalidateOptionsMenu();
    }

    /**
     * Uses selected item's index to return the fragment to be opened
     * @return The fragment to be opened according to the item selected in the nav drawer
     */
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // Creating the Inbox Fragment
                InboxFragment inboxFragment = new InboxFragment();
                return inboxFragment;
            case 1:
                // Creating the Settings Fragment
                ContextsFragment contextsFragment = new ContextsFragment();
                return contextsFragment;
            case 2:
                // Creating the Settings Fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return new InboxFragment();
        }
    }


    /**
     * Setting Nav header and subtext
     */
    private void loadNavHeader() {
        // Setting the respective Textviews
        navHeaderName.setText("Shreyak Kumar");
        navHeaderSubText.setText("kumarshreyak@gmail.com");
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
                fragment = new ContextsFragment();
                break;
            case 2:
                fragment = new SettingsFragment();
                break;
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();

        // Highlight the selected item, update the title, and close the drawerLayout
        navDrawerMenuList.setItemChecked(position, true);
        setTitle(navDrawerItemNames[position]);
        drawerLayout.closeDrawer(navDrawerMenuList);
    }


    /**
     *
     * @param view The view for the Floating Action Button
     */
    public void onFABPress(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        InputFragment inputFragment = InputFragment.newInstance("Some Title",getApplicationContext());
        inputFragment.show(fragmentManager, "fragment_edit_name");
    }

    /**
     *
     * @param view The view for the Floating Action Button
     */
    public void onContextFABPress(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ContextInputFragment contextInputFragment = ContextInputFragment.newInstance("add",getApplicationContext());
        contextInputFragment.show(fragmentManager, "fragment_edit_name");
    }

    /**
     * Show the fab only when Inbox fragment is opened
     */
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

}
