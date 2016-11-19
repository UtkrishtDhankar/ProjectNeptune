package utkrishtdhankar.projectneptune.TaskStatusPackage;

/**
 * Created by utkrishtdhankar on 16/11/16.
 */

/**
 * This represents a task that has been completed
 */
public class Done implements TaskStatus {

    private static final String name = "Done";
    private static final String special = "";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSpecial() {
        return special;
    }

    @Override
    public String encode() {
        return name;
    }
}
