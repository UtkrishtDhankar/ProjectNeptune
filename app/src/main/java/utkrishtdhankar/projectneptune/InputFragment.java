package utkrishtdhankar.projectneptune;

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
import android.widget.Button;
import android.widget.EditText;

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
     * Inflates this fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        // Get field from view
        inboxEditText = (EditText) view.findViewById(R.id.addTextInput);
        inboxAddButton = (Button) view.findViewById(R.id.addTaskbutton) ;
        inboxAddButton.setOnClickListener(this);
        databaseHelper = new DatabaseHelper(getActivity());

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

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
        Task newTask = new Task(newTaskName);

        // Add said task to the database
        databaseHelper.addTask(newTask);

        //Reloading the fragment so that values from tables are updated
        //HOME fragment is opened
        Fragment fragment = new InboxFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).addToBackStack(null).commit();

        //Closes the pop-up
        dismiss();
    }
}
