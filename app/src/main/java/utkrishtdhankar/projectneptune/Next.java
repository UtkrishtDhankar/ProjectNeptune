package utkrishtdhankar.projectneptune;

/**
 * Created by utkrishtdhankar on 15/11/16.
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
