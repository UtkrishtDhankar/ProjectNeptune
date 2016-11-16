package utkrishtdhankar.projectneptune;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Shreyak Kumar on 28-10-2016.
 * This pops up to let the player enter/edit the oldtask
 */

public class InputFragment extends DialogFragment implements View.OnClickListener,DatePickerDialog.OnDateSetListener {

    // A reference to the database. Used when adding/editing tasks
    DatabaseHelper databaseHelper;

    // The place where the user types in their oldtask name
    private EditText inboxEditText;

    // The button that is pressed when the user has added the thing
    private Button inboxAddButton;
    private EditText cEditText;
    private Spinner contextDropDown;
    private Spinner statusDropDown;

    // For updating contexts
    int openedForEdit = 0;
    Task oldtask;

    Calendar calendar;

    /**
     * Default constructor
     */
    public InputFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    /**
     * Creates this input fragment
     * @param title The title of this fragment
     * @param context The context this is called in
     * @return The new input fragment that we created
     */
    public static InputFragment newInstance(String title, Context context) {
        InputFragment frag = new InputFragment();

        // Set the arguments for the fragment
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        return frag;
    }

    public static InputFragment newInstance(String title,String taskText,String taskContext,String taskStatus,long id) {
        InputFragment frag = new InputFragment();

        // Set the arguments for the fragment
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("taskText", taskText);
        args.putString("oldContext", taskContext);
        args.putString("taskStatus", taskStatus);
        args.putLong("taskId",id);
        frag.setArguments(args);

        return frag;
    }

    /**
     * Inflates this fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if(getArguments().getString("title") == "edit") {
            openedForEdit = 1;
        }else{
            openedForEdit = 0;
        }

        return inflater.inflate(R.layout.input_fragment, container);
    }

    /**
     * Sets up all of the things (the edit texts, etc.) with references from the code
     * And also does some other housekeeping like opening up the soft keyboard
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing database helper
        databaseHelper = new DatabaseHelper(getActivity());

        // Get field from view
        inboxEditText = (EditText) view.findViewById(R.id.addTextInput);
        inboxAddButton = (Button) view.findViewById(R.id.addTaskbutton) ;
        inboxAddButton.setOnClickListener(this);

        // Setting the context drop down menu
        contextDropDown = (Spinner) view.findViewById(R.id.contextSpinner);

        // Fetching all contexts from table
        ArrayList<TaskContext> contextsArray = databaseHelper.getAllContexts();
        String[] contextsNames= new String[contextsArray.size()];
        for (int i = 0; i < contextsArray.size(); i++) {
            contextsNames[i] = contextsArray.get(i).getName();
        }

        // populating the drop down menu
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, contextsNames); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contextDropDown.setAdapter(spinnerArrayAdapter);

        // Setting the status drop down menu
        statusDropDown = (Spinner) view.findViewById(R.id.statusSpinner);

        // Create an ArrayAdapter using the string array and a default colorDropDown layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.status_names, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the statusDropDown list
       statusDropDown.setAdapter(adapter);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        // For Editing Tasks
        if(openedForEdit == 1){

            oldtask = new Task(getArguments().getString("taskText"));
            oldtask.addContext(new TaskContext(getArguments().getString("oldContext")));
            oldtask.changeStatus(TaskStatus.valueOf(getArguments().getString("taskStatus")));
            oldtask.setId(getArguments().getLong("taskId"));
            inboxEditText.setText(oldtask.getName());
            for(int i = 0; i < contextsNames.length; i++){
                if(contextsNames[i].equals(getArguments().getString("oldContext"))){
                    contextDropDown.setSelection(i);
                }
            }
            for(int i = 0; i < adapter.getCount(); i++){
                if(adapter.getItem(i).toString().equals(getArguments().getString("taskStatus"))){
                    statusDropDown.setSelection(i);
                    //TODO open corresponding fragment
                    DatePickerFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
            }

        }

        statusDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(adapter.getItem(position).toString().equals("Waiting") ){
                    //open corresponding fragment
                }

                if(adapter.getItem(position).toString().equals("Scheduled") ){
                    //open corresponding fragment
                    DatePickerFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        //The Calendar variable is now set.

        // Show soft keyboard automatically and request focus to field
        inboxEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * Called when the user clicks the add button
     * @param view The view that this was a part of
     */
    @Override
    public void onClick(View view) {
        // Fill out the new oldtask
        String newTaskName = inboxEditText.getText().toString();
        String newContextName = contextDropDown.getSelectedItem().toString();
        String newStatusName = statusDropDown.getSelectedItem().toString();

        Task newTask = new Task(newTaskName);

        // Add the context for the oldtask
        TaskContext taskContext = new TaskContext(newContextName);
        newTask.addContext(taskContext);
        newTask.changeStatus(TaskStatus.valueOf(newStatusName));

        if(openedForEdit == 1){
            // Call the editing function use the oldtask variable for old values
            databaseHelper.updateTask(oldtask,newTask);
        }else{
            // Add said newtask to the database
            databaseHelper.addTask(newTask);
        }



        //Reloading the fragment so that values from tables are updated
        //HOME fragment is opened
        Fragment fragment = new InboxFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).addToBackStack(null).commit();

        //Closes the pop-up
        dismiss();
    }

    /**
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (0-11 for compatibility with
     *                   {@link Calendar#MONTH})
     * @param dayOfMonth th selected day of the month (1-31, depending on
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    }
}
