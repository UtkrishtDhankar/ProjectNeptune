package utkrishtdhankar.projectneptune;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Shreyak Kumar on 28-10-2016.
 * This pops up to let the player enter/edit the task
 */

public class InputFragment extends DialogFragment implements View.OnClickListener {

    // A reference to the database. Used when adding/editing tasks
    DatabaseHelper databaseHelper;

    // The place where the user types in their task name
    private EditText inboxEditText;

    // The button that is pressed when the user has added the thing
    private Button inboxAddButton;
    private EditText cEditText;
    private Spinner contextDropDown;
    private Spinner statusDropDown;

    // For updating contexts
    int openedForEdit = 0;
    Task task;

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

    public static InputFragment newInstance(String title,String taskText,String taskContext,String taskStatus) {
        InputFragment frag = new InputFragment();

        // Set the arguments for the fragment
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("taskText", taskText);
        args.putString("taskContext", taskContext);
        args.putString("taskStatus", taskStatus);
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
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

            task = new Task(getArguments().getString("taskText"));
            task.addContext(new TaskContext(getArguments().getString("taskContext")));
            task.changeStatus(TaskStatusHelper.decode(getArguments().getString("taskStatus")));

            inboxEditText.setText(task.getName());
            for(int i = 0; i < contextsNames.length; i++){
                if(contextsNames[i].equals(getArguments().getString("taskContext"))){
                    contextDropDown.setSelection(i);
                }
            }
            for(int i = 0; i < adapter.getCount(); i++){
                if(adapter.getItem(i).toString().equals(getArguments().getString("taskStatus"))){
                    statusDropDown.setSelection(i);
                }
            }

        }

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
        // Fill out the new task
        String newTaskName = inboxEditText.getText().toString();
        String newContextName = contextDropDown.getSelectedItem().toString();
        String newStatusName = statusDropDown.getSelectedItem().toString();

        Task newTask = new Task(newTaskName);

        // Add the context for the task
        TaskContext taskContext = new TaskContext(newContextName);
        newTask.addContext(taskContext);
        newTask.changeStatus(TaskStatusHelper.decode(newStatusName));

        if(openedForEdit == 1){
            // Call the editing function use the task variable for old values
        }else{
            // Add said task to the database
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
}
