package utkrishtdhankar.projectneptune;

import java.util.ArrayList;

/**
 * Created by utkrishtdhankar on 17/11/16.
 */

public class Project extends Identifiable {
    private String name;

    private ArrayList<Task> tasks;

    public Project(String name) {
        this.name = name;
        tasks = new ArrayList<>();
    }

    /**
     * This adds a task to this project
     * @param task
     */
    public void addTask(Task task) {
        task.setProject(this);
        tasks.add(task);
    }

    /**
     * Removes a project from the given task
     * @param task
     */
    public void removeTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            // TODO make a equals function for task
            if (tasks.get(i).getName().equals(task)) {
                task.unsetProject();
                tasks.get(i).unsetProject();

                tasks.remove(i);

                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
