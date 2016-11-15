package utkrishtdhankar.projectneptune;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

public class Inbox implements TaskStatus {

    private static final String name = "Inbox";
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
