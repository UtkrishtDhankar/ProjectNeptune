package utkrishtdhankar.projectneptune.TaskStatusPackage;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by utkrishtdhankar on 16/11/16.
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
            // + 1 (to get to the index of the starting position)
            status = new Waiting(encodedStatus.substring("Waiting".length() + 2));
        } else if(encodedStatus.startsWith("Scheduled")) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(DateFormat.getDateInstance()
                        .parse(encodedStatus.substring("Scheduled".length() + 2)));
                status = new Scheduled(cal);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Scheduled date format is illegal. Given substring is: "
                        + encodedStatus.substring("Scheduled".length() + 2));
            }
        } else if (encodedStatus.equals("Someday")) {
            status = new Someday();
        } else {
            throw new IllegalArgumentException("encodedStatus is not a legal status");
        }

        return status;
    }
}
