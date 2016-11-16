package utkrishtdhankar.projectneptune;

import java.util.Calendar;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

public class Scheduled implements TaskStatus {
    private static final String name = "Scheduled";

    private Calendar scheduledForDate;

    Scheduled(Calendar date) {
        scheduledForDate = date;
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
        return scheduledForDate.toString();
    }

    @Override
    public String encode() {
        return name + " " + scheduledForDate.toString();
    }
}
