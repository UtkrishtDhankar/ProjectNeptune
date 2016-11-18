package utkrishtdhankar.projectneptune.TaskStatusPackage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

public class Scheduled implements TaskStatus {
    private static final String name = "Scheduled";

    private Calendar scheduledForDate;

    public Scheduled(Calendar date) {
        scheduledForDate = date;
    }

    /**
     * This returns a status. If scheduled is scheduled for a later date, then it returns it back.
     * Otherwise
     * @param scheduled
     * @return
     */
    public static TaskStatus updateScheduledStatus(Scheduled scheduled) {
        // If the time has passed, then return a next status, otherwise return scheduled
        if (scheduled.getScheduledForDate().compareTo(Calendar.getInstance()) <  0) {
            return new Next();
        } else {
            return scheduled;
        }
    }

    public Calendar getScheduledForDate() {
        return scheduledForDate;
    }

    public void setScheduledForDate(Calendar scheduledForDate) {
        this.scheduledForDate = scheduledForDate;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSpecial() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(scheduledForDate.getTime());
    }

    @Override
    public String encode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return name + " " + sdf.format(scheduledForDate.getTime());
    }
}
