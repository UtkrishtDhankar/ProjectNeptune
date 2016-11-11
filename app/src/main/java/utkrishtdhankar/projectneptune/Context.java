package utkrishtdhankar.projectneptune;

import android.graphics.Color;

/**
 * Created by utkrishtdhankar on 11/11/16.
 */

public class Context {

    private static final int defaultColor = Color.LTGRAY;
    private static final String defaultName = "context";

    private String name;

    private int color;

    /**
     * Default constructor
     * Sets all the things to their default values
     */
    Context() {
        name = defaultName;
        color = defaultColor;
    }

    /**
     * Creates a new context with the name contextName
     * Sets the color to default
     * @param contextName the name of the new context
     */
    Context(String contextName) {
        name = contextName;
        color = defaultColor;
    }

    /**
     * Creates a new context with the given parameters
     * @param contextName the name of the new context
     * @param contextColor the color of the new context
     */
    Context(String contextName, int contextColor) {
        name = contextName;
        color = contextColor;
    }

    /**
     * @return the name of this context
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the context to newName
     * @param newName the name to be set
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * @return The color of this context
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the color to newColor
     * @param newColor the color to be set
     */
    public void setColor(int newColor) {
        color = newColor;
    }
}
