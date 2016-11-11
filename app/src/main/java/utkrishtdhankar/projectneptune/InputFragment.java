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
 */

public class InputFragment extends DialogFragment implements View.OnClickListener {

    DatabaseHelper databaseHelper ;
    private EditText inboxEditText;
    private Button inboxAddButton;
   // private static Context localContext;

    public InputFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static InputFragment newInstance(String title, Context context) {
        InputFragment frag = new InputFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        //localContext = context;
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.input_fragment, container);

    }

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



    @Override
    public void onClick(View v) {
        String newTaskName = inboxEditText.getText().toString();
        Task newTask = new Task(newTaskName);

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
