package utkrishtdhankar.projectneptune;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import utkrishtdhankar.projectneptune.TaskStatusPackage.Scheduled;
import utkrishtdhankar.projectneptune.TaskStatusPackage.TaskStatusHelper;
import utkrishtdhankar.projectneptune.TaskStatusPackage.Waiting;

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
    private EditText waitingText;
    private TextView waitingTextView;

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

    /**
     * Called when the input fragment is opened for editing a card
     * @param title The title of the calling fragment
     * @param taskText The text of the task
     * @param taskContext The context of the task
     * @param taskStatusEncoded The Complete status of the task (including the special attribute)
     * @param taskStatus The status of the task (without the special attribute)
     * @param taskStatusSpecial The special attribute of the status of the task
     * @param id THe id of the task (used in setting the oldTask's id)
     * @return THe fragment to be inflated
     */
    public static InputFragment newInstance(String title,
                                            String taskText,
                                            String taskContext,
                                            String taskStatusEncoded,
                                            String taskStatus,
                                            String taskStatusSpecial,
                                            long id) {
        InputFragment frag = new InputFragment();

        // Set the arguments for the fragment
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("taskText", taskText);
        args.putString("oldContext", taskContext);
        args.putString("taskStatus", taskStatus);
        args.putString("taskStatusSpecial", taskStatusSpecial);
        args.putString("encodedStatus", taskStatusEncoded);
        args.putLong("taskId",id);
        frag.setArguments(args);

        return frag;
    }

    /**
     * Inflates this fragment
     * @param inflater The layout inflater used to make the popup
     * @param container The container for the layout
     * @param savedInstanceState The Bundle used to retrieve data during some resume actions(not used)
     * @return The inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if(getArguments().getString("title") != "title") {
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
        waitingText = (EditText) view.findViewById(R.id.waitingText);
        waitingTextView = (TextView) view.findViewById(R.id.waitingFor);
        waitingText.setVisibility(View.GONE);
        waitingTextView.setVisibility(View.GONE);
        inboxAddButton.setOnClickListener(this);

        // Setting the context drop down menu
        contextDropDown = (Spinner) view.findViewById(R.id.contextSpinner);

        // Fetching all contexts from table
        ArrayList<TaskContext> contextsArray = databaseHelper.getAllContexts();
        String[] contextsNames= new String[contextsArray.size() + 1];
        contextsNames[0] = "";
        for (int i = 1; i <= contextsArray.size(); i++) {
            contextsNames[i] = contextsArray.get(i - 1).getName();
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
            oldtask.changeStatus(TaskStatusHelper.decode(getArguments().getString("encodedStatus")));
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
                }
            }

        }

        statusDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(adapter.getItem(position).toString().equals("Waiting") ){
                    //set visibility of Waiting for editText
                    waitingText.setVisibility(View.VISIBLE);
                    waitingTextView.setVisibility(View.VISIBLE);
                    waitingText.setText(getArguments().getString("taskStatusSpecial"));
                }else{
                    waitingText.setVisibility(View.GONE);
                    waitingTextView.setVisibility(View.GONE);
                }

                if(adapter.getItem(position).toString().equals("Scheduled") ){
                    //open corresponding fragment
                    if(openedForEdit == 1){
                        DatePickerFragment newFragment = DatePickerFragment.newInstance(getArguments().getString("taskStatusSpecial"));
                        newFragment.setTargetFragment(InputFragment.this,300);
                        newFragment.show(getFragmentManager(), "datePicker");
                    }else{
                        DatePickerFragment newFragment = DatePickerFragment.newInstance();
                        newFragment.setTargetFragment(InputFragment.this,300);
                        newFragment.show(getFragmentManager(), "datePicker");
                    }

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
     * Called when the user clicks the add button in the input pop-up
     * @param view The view that this was a part of
     */
    @Override
    public void onClick(View view) {
        // Fill out the new oldtask
        String newTaskName = inboxEditText.getText().toString();
        String newContextName = contextDropDown.getSelectedItem().toString();
        String newStatusName = statusDropDown.getSelectedItem().toString();

        Task newTask = new Task(newTaskName);

        if(newStatusName.equals("Waiting")){
            String waitingFor = waitingText.getText().toString();
            newTask.changeStatus(new Waiting(waitingFor));
        } else if(newStatusName.equals("Scheduled")) {
            newTask.changeStatus(new Scheduled(calendar));
        } else {
            newTask.changeStatus(TaskStatusHelper.decode(newStatusName));
        }

        // Add the context for the oldtask
        TaskContext taskContext = new TaskContext(newContextName);

        if(!taskContext.getName().equals("")){
            newTask.addContext(taskContext);
        }

       // newTask.changeStatus(TaskStatusHelper.decode(newStatusName));

        if(openedForEdit == 1) {
            // Call the editing function use the oldtask variable for old values
            databaseHelper.updateTask(oldtask, newTask);
        }else{
            // Add said newtask to the database
            databaseHelper.addTask(newTask);
        }

        // Reloading the appropriate fragment so that values from tables are updated
        Fragment fragment;
        switch(getArguments().getString("title")){
            case "All Tasks":
                fragment = AllTaskFragment.newInstance();
                break;
            case "Inbox":
                fragment = InboxFragment.newInstance();
                break;
            case "Next":
                fragment = NextFragment.newInstance();
                break;
            case "Waiting":
                fragment = WaitingFragment.newInstance();
                break;
            case "Scheduled":
                fragment = ScheduledFragment.newInstance();
                break;
            case "Someday":
                fragment = SomedayFragment.newInstance();
                break;
            default:
                fragment = InboxFragment.newInstance();
                break;
        }
        // The Fragment is opened
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).addToBackStack(null).commit();


        // Setting the apt. toolbar title after reloading
        if(openedForEdit == 0){
            ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("Inbox");
        }else{
            ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText(getArguments().getString("title"));
        }


        // Closes the pop-up
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
