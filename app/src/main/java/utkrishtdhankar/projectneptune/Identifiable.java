package utkrishtdhankar.projectneptune;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

/**
 * Extend this class for any objects that are stored in a database using long ids.
 * This handles binding the object to it's id and so on.
 */
public class Identifiable {
    // Any Identifiables that do not have an id have this set as a default
    public static final long INVALID_ID = -1;

    // The id of the thing
    private long id;

    /**
     * Constructs a new Identifiable and sets it's id to be invalid (ie, not bound)
     */
    Identifiable() {
        id = INVALID_ID;
    }

    /**
     * Checks if this Identifiable is valid (bound correctly) or not
     * @return true if it is bound incorrectly, false otherwise
     */
    public boolean isInvalid() {
        return id == INVALID_ID;
    }

    /**
     * Gets the id of this identifiable
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of this identifiable to be the newId
     * @param newId
     */
    public void setId(long newId) {
        id = newId;
    }

    /**
     * Sets the id of this Identifiable to be invalid
     */
    public void unsetId() {
        id = INVALID_ID;
    }

}
