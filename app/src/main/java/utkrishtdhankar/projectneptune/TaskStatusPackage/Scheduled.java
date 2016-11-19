package utkrishtdhankar.projectneptune.TaskStatusPackage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

/**
 * Represents a task scheduled for execution on a later, specific date.
 */
public class Scheduled implements TaskStatus {
    private static final String name = "Scheduled";

    private Calendar scheduledForDate;

    /**
     * Sets the date for this task as the supplied parameter. There must never be a Scheduled
     * without a date, so there's no default constructor.
     * @param date the date to set this Scheduled to
     */
    public Scheduled(Calendar date) {
        scheduledForDate = date;
    }

    /**
     * This returns a status. If scheduled is scheduled for a later date, then it returns it back.
     * Otherwise returns a Next object
     * @param scheduled the status
     * @return the new value of this status
     */
    public static TaskStatus updateScheduledStatus(Scheduled scheduled) {
        // If the time has passed, then return a next status, otherwise return scheduled
        if (scheduled.getScheduledForDate().compareTo(Calendar.getInstance()) <  0) {
            return new Next();
        } else {
            return scheduled;
        }
    }

    /**
     * @return the date that this is scheduled for, as a Calendar object
     */
    public Calendar getScheduledForDate() {
        return scheduledForDate;
    }

    /**
     * Sets the date when this task is supposed to be executed
     * @param scheduledForDate the new date
     */
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
