package linenux.data;

import java.util.ArrayList;

import linenux.data.task.Task;

/**
 * Represents the entire schedule.
 */
public class Schedule {
    private final ArrayList<Task> taskList;
    
    /** Constructs an empty schedule */
    public Schedule() {
        this.taskList = new ArrayList<Task>();
    }
    
    /** Constructs the schedule with the given data. */
    public Schedule(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }
    
    /** Adds a task to the schedule */
    public void addTask(Task task) {
        taskList.add(task);
    }
    
    /** Clears all tasks from the schedule */
    public void clear() {
        taskList.clear();
    }
}