package utkrishtdhankar.projectneptune;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

public class Identifiable {
    private static final long INVALID_ID = -1;

    private long id;

    Identifiable() {
        id = INVALID_ID;
    }

    public static boolean isInvalid(long id) {
        return id == INVALID_ID;
    }

    public long getId() {
        return id;
    }

    public void setId(long newId) {
        id = newId;
    }

    public void unsetId() {
        id = INVALID_ID;
    }

}
