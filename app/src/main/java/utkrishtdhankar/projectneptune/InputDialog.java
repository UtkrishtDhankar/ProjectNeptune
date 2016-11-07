package utkrishtdhankar.projectneptune;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Shreyak Kumar on 28-10-2016.
 */

public class InputDialog extends DialogFragment implements View.OnClickListener {

    DatabaseHelper databaseHelper ;
    private EditText mEditText;
    private Button mAddButton;
   // private static Context localContext;

    public InputDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static InputDialog newInstance(String title,Context context) {
        InputDialog frag = new InputDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        //localContext = context;
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.input_popup, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.addTextInput);
        mAddButton = (Button) view.findViewById(R.id.addTaskbutton) ;
        mAddButton.setOnClickListener(this);
        databaseHelper = new DatabaseHelper(getActivity());
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }



    @Override
    public void onClick(View v) {
        String newTaskName = mEditText.getText().toString();
        Task newTask = new Task(newTaskName);

        databaseHelper.addTask(newTask);
    }
}
