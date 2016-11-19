package utkrishtdhankar.projectneptune.TaskStatusPackage;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

/**
 * This represents a task to be done as soon as possible.
 */
public class Next implements TaskStatus {
    private static final String name = "Next";
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
