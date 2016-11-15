package utkrishtdhankar.projectneptune;

import java.util.Date;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

public class Scheduled implements TaskStatus {
    private static final String name = "Scheduled";

    private Date scheduledForDate;

    Scheduled(Date date) {
        scheduledForDate = date;
    }

    public Date getScheduledForDate() {
        return scheduledForDate;
    }

    public void setScheduledForDate(Date scheduledForDate) {
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
