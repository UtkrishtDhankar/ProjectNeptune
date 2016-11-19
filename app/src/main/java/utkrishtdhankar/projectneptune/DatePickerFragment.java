package utkrishtdhankar.projectneptune;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Shreyak Kumar on 16-11-2016.
 */
public class DatePickerFragment extends DialogFragment {


    /**
     * Creates this input fragment
     * @return The new input fragment that we created
     */
    public static DatePickerFragment newInstance() {
        DatePickerFragment frag = new DatePickerFragment();

        // Set the arguments for the fragment
        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }

    /**
     * Creates this input fragment
     * @param special The title of this fragment
     * @return The new input fragment that we created
     */
    public static DatePickerFragment newInstance(String special) {
        // Making the fragment
        DatePickerFragment frag = new DatePickerFragment();

        // Set the arguments for the fragment
        Bundle args = new Bundle();
        args.putString("ScheduledSpecial", special);
        frag.setArguments(args);

        return frag;
    }

    /**
     * Called when the dialog is created
     * @param savedInstanceState Used to retrieve data items (not used)
     * @return The date picker dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        String temp = null;
        if(getArguments().containsKey("ScheduledSpecial")){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                c.setTime(sdf.parse(getArguments().getString("ScheduledSpecial")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();


        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }


}
