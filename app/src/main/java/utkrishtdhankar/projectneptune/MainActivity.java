package utkrishtdhankar.projectneptune;

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

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


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
        getSupportActionBar();

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

        // Fetching all contexts from table
        ArrayList<TaskContext> contextsArray = databaseHelper.getAllContexts();
        String[] contextsNames= new String[contextsArray.size()];
        for (int i = 0; i < contextsArray.size(); i++) {
            contextsNames[i] = contextsArray.get(i).getName();
        }

        // populating the drop down menu
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, contextsNames); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        context.setAdapter(spinnerArrayAdapter);

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
                    case R.id.nav_inbox:
                        navItemIndex = 0;
                        fragment = new InboxFragment();
                        toolbar.setTitle("Inbox");
                        break;

                    case R.id.nav_next:
                        navItemIndex = 1;
                        fragment = new NextFragment();
                        toolbar.setTitle("Next");
                        break;

                    case R.id.nav_waiting:
                        navItemIndex = 2;
                        fragment = new WaitingFragment();
                        toolbar.setTitle("Waiting");
                        break;

                    case R.id.nav_scheduled:
                        navItemIndex = 3;
                        fragment = new ScheduledFragment();
                        toolbar.setTitle("Scheduled");
                        break;

                    case R.id.nav_someday:
                        navItemIndex = 4;
                        fragment = new SomedayFragment();
                        toolbar.setTitle("Someday");
                        break;

                    case R.id.nav_all_tasks:
                        navItemIndex = 5;
                        fragment = new AllTaskFragment();
                        toolbar.setTitle("All Tasks");
                        break;

                    case R.id.nav_context:
                        navItemIndex = 6;
                        fragment = new ContextsFragment();
                        toolbar.setTitle("Contexts");
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

                // Toggle the fab
                toggleFab();

                // Closing drawerLayout on item click
                drawerLayout.closeDrawers();

                // Refresh toolbar menu
                invalidateOptionsMenu();

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
     * Setting Nav header and subtext
     */
    private void loadNavHeader() {
        // Setting the respective Textviews
        navHeaderName.setText("Shreyak Kumar");
        navHeaderSubText.setText("kumarshreyak@gmail.com");
    }


    /** Swaps fragments in the main content view */
//    private void selectItem(int position) {
//        Fragment fragment = new AllTaskFragment();
//        // Create a new fragment and specify the fragment to show based on position
//        switch(position)
//        {
//            case 0:
//                fragment = new AllTaskFragment();
//                break;
//            case 1:
//                fragment = new ContextsFragment();
//                break;
//            case 2:
//                fragment = new SettingsFragment();
//                break;
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
//
//        // Highlight the selected item, update the title, and close the drawerLayout
//        navDrawerMenuList.setItemChecked(position, true);
//        setTitle(navDrawerItemNames[position]);
//        drawerLayout.closeDrawer(navDrawerMenuList);
//    }


    /**
     *
     * @param view The view for the Floating Action Button
     */
    public void onFABPress(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        InputFragment inputFragment = InputFragment.newInstance("title",getApplicationContext());
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
