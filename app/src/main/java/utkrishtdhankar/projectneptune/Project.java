package utkrishtdhankar.projectneptune;

/**
 * Created by utkrishtdhankar on 17/11/16.
 */

public class Project extends Identifiable {
    public String name;

    public Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
