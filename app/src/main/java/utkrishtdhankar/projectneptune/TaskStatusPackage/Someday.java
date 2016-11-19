package utkrishtdhankar.projectneptune.TaskStatusPackage;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

/**
 * This represents a task status that is to be done someday in the future
 */
public class Someday implements TaskStatus {
    private static final String name = "Someday";
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
