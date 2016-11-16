package utkrishtdhankar.projectneptune.TaskStatusPackage;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

public class Waiting implements TaskStatus {
    private static final String name = "Waiting";

    private String waitingForName;

    public Waiting(String newWaitingForName) {
        waitingForName = newWaitingForName;
    }

    public String getWaitingForName() {
        return waitingForName;
    }

    public void setWaitingForName(String waitingForName) {
        this.waitingForName = waitingForName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSpecial() {
        return waitingForName;
    }

    @Override
    public String encode() {
        return name + " " + waitingForName;
    }
}
