package utkrishtdhankar.projectneptune;

import android.graphics.Color;

/**
 * Created by utkrishtdhankar on 11/11/16.
 */

/**
 * This represents a context in the GTD sense.
 */
public class TaskContext extends Identifiable {

    // The default color of a context that hasn't been assigned any colors
    // No context should ever have this color in the UI, but this is here as a placeholder
    private static final int defaultColor = Color.LTGRAY;

    // The default name of a context
    private static final String defaultName = "context";

    // The current name of the context
    private String name;

    // The current color of the context as an rgba value. Please use the Color class when
    // modifying this
    private int color;

    /**
     * Default constructor
     * Sets all the things to their default values
     */
    TaskContext() {
        name = defaultName;
        color = defaultColor;
    }

    /**
     * Creates a new context with the name contextName
     * Sets the color to default
     * @param contextName the name of the new context
     */
    TaskContext(String contextName) {
        name = contextName;
        color = defaultColor;
    }

    /**
     * Creates a new context with the given parameters
     * @param contextName the name of the new context
     * @param contextColor the color of the new context
     */
    TaskContext(String contextName, int contextColor) {
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
