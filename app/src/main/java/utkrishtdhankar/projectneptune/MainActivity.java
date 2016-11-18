package utkrishtdhankar.projectneptune;

import android.content.Context;
import android.graphics.Color;
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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import utkrishtdhankar.projectneptune.TaskStatusPackage.Scheduled;
import utkrishtdhankar.projectneptune.TaskStatusPackage.Someday;
import utkrishtdhankar.projectneptune.TaskStatusPackage.Waiting;


public class MainActivity extends AppCompatActivity {

    public DatabaseHelper databaseHelper;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private Spinner context_spinner;
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
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        context_spinner = (Spinner) findViewById(R.id.toolbar_context_spinner);
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
        fab = (FloatingActionButton) findViewById(R.id.addButton);

        // Load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_toolbar_titles);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        //databaseHelper.updateAll();



        // Initializing navigation menu
        setUpNavigationView();
        Log.d("MainActivity.java","WE REACHED THIS POINT");

        // Fetching all contexts from table

        final ArrayList<TaskContext> contextsArray = databaseHelper.getAllContexts();
        final String[] contextsNames= new String[contextsArray.size() + 1];
        contextsNames[0] = "All";
        for (int i = 1; i <= contextsArray.size(); i++) {
            contextsNames[i] = contextsArray.get(i - 1).getName();
        }

        // populating the drop down menu
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, contextsNames); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        context_spinner.setAdapter(spinnerArrayAdapter);

        // The on item seleted listener
        context_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // Setting the selected item's color to white
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);

                if(position != 0){
                    TaskContext taskContext = new TaskContext(contextsNames[position]);
                    long contextId = databaseHelper.getContextId(taskContext);
                    switch(navItemIndex){
                        case 0:
                            fragment = InboxFragment.newInstance(contextsNames[position],contextId);
                            toolbar_title.setText("Inbox");
                            break;
                        case 1:
                            fragment = NextFragment.newInstance(contextsNames[position],contextId);
                            toolbar_title.setText("Next");
                            break;
                        case 2:
                            fragment = WaitingFragment.newInstance(contextsNames[position],contextId);
                            toolbar_title.setText("Waiting");
                            break;
                        case 3:
                            fragment = ScheduledFragment.newInstance(contextsNames[position],contextId);
                            toolbar_title.setText("Scheduled");
                            break;
                        case 4:
                            fragment = SomedayFragment.newInstance(contextsNames[position],contextId);
                            toolbar_title.setText("Someday");
                            break;
                        case 5:
                            fragment = AllTaskFragment.newInstance(contextsNames[position],contextId);
                            toolbar_title.setText("All Tasks");
                            break;
                        default:
                            fragment = InboxFragment.newInstance(contextsNames[position],contextId);
                            toolbar_title.setText("Inbox");
                            break;
                    }

                    // Reloading the current fragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();

                }else{
                    switch(navItemIndex){
                        case 0:
                            fragment = InboxFragment.newInstance();
                            toolbar_title.setText("Inbox");
                            break;
                        case 1:
                            fragment = NextFragment.newInstance();
                            toolbar_title.setText("Next");
                            break;
                        case 2:
                            fragment = WaitingFragment.newInstance();
                            toolbar_title.setText("Waiting");
                            break;
                        case 3:
                            fragment = ScheduledFragment.newInstance();
                            toolbar_title.setText("Scheduled");
                            break;
                        case 4:
                            fragment = SomedayFragment.newInstance();
                            toolbar_title.setText("Someday");
                            break;
                        case 5:
                            fragment = AllTaskFragment.newInstance();
                            toolbar_title.setText("All tasks");
                            break;
                        default:
                            fragment = InboxFragment.newInstance();
                            toolbar_title.setText("Inbox");
                            break;
                    }

                    // Reloading the current fragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


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
                        fragment = InboxFragment.newInstance();
                        toolbar_title.setText("Inbox");
                        break;

                    case R.id.nav_next:
                        navItemIndex = 1;
                        fragment = NextFragment.newInstance();
                        toolbar_title.setText("Next");
                        break;

                    case R.id.nav_waiting:
                        navItemIndex = 2;
                        fragment = WaitingFragment.newInstance();
                        toolbar_title.setText("Waiting");
                        break;

                    case R.id.nav_scheduled:
                        navItemIndex = 3;
                        fragment = ScheduledFragment.newInstance();
                        toolbar_title.setText("Scheduled");
                        break;

                    case R.id.nav_someday:
                        navItemIndex = 4;
                        fragment = SomedayFragment.newInstance();
                        toolbar_title.setText("Someday");
                        break;

                    case R.id.nav_all_tasks:
                        navItemIndex = 5;
                        fragment = AllTaskFragment.newInstance();
                        toolbar_title.setText("All Tasks");
                        break;

                    case R.id.nav_context:
                        navItemIndex = 6;
                        fragment = new ContextsFragment();
                        toolbar_title.setText("Contexts");
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
