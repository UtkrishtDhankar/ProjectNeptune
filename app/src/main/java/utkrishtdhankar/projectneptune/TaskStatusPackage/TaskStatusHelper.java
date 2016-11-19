package utkrishtdhankar.projectneptune.TaskStatusPackage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by utkrishtdhankar on 16/11/16.
 */

/**
 * This class is just there to help with decoding statuses from the database
 */
public class TaskStatusHelper {

    /**
     * This parses the status encoded string and returns a status
     * @param encodedStatus
     * @return
     */
    public static TaskStatus decode(String encodedStatus) {
        TaskStatus status;

        if (encodedStatus.equals("Inbox")) {
            status = new Inbox();
        } else if (encodedStatus.equals("Next")) {
            status = new Next();
        } else if (encodedStatus.startsWith("Waiting")) {
            // The Waiting special string will follow the Waiting after a whitespace
            // So the starting index is length of waiting + 1 (whitespace)
            status = new Waiting(encodedStatus.substring("Waiting".length() + 1));
        } else if(encodedStatus.startsWith("Scheduled")) {
            try {
                Calendar cal = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                // This special string behaves similarly to the waiting string, so same logic
                cal.setTime(sdf.parse(encodedStatus.substring("Scheduled".length() + 1)));
                status = new Scheduled(cal);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Scheduled date format is illegal. Given substring is: "
                        + encodedStatus.substring("Scheduled".length() + 1));
            }
        } else if (encodedStatus.equals("Someday")) {
            status = new Someday();
        } else if (encodedStatus.equals("Done")) {
            status = new Done();
        } else {
            // If we don't recognise a status, then the app should crash as it gives us plenty of
            // warning in the Android Monitor that something is wrong
            throw new IllegalArgumentException("encodedStatus is not a legal status");
        }

        return status;
    }
}
