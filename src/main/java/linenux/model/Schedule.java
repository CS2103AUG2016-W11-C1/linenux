package linenux.model;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Contains all outstanding tasks.
 */
public class Schedule {
    private static final int MAX_STATES = 10;
    private final LinkedList<State> states;

    /**
     * Constructs an empty schedule
     */
    public Schedule() {
        this.states = new LinkedList<State>();
        states.add(new State());
    }

    /**
     * Constructs the schedule with the given data.
     */
    public Schedule(ArrayList<Task> taskList) {
        this.states = new LinkedList<State>();
        states.add(new State(taskList));
    }

    /**
     * Adds a task to the schedule
     */
    public void addTask(Task task) {
        State newState = getMostRecentState().copyState();
        newState.addTask(task);
        addState(newState);
    }

    /**
     * Delete the specified task.
     * @param task The task to delete.
     */
    public void deleteTask(Task task) {
        State newState = getMostRecentState().copyState();
        newState.deleteTask(task);
        addState(newState);
    }

    /**
     * Clears all tasks from the schedule
     */
    public void clear() {
        State newState = new State();
        addState(newState);
    }

    /**
     * Performs case-insensitive search using keywords.
     * @param keywords Search keywords
     * @return List of {@code Task} matching the keywords.
     */
    public ArrayList<Task> search(String[] keywords) {
        return getMostRecentState().search(keywords);
    }

    /**
     * Returns the list of tasks.
     */
    public ArrayList<Task> getTaskList() {
        return getMostRecentState().getTaskList();
    }

    /**
     * Returns the most recent state of schedule
     */
    private State getMostRecentState() {
        return states.getLast();
    }

    /**
     * Adds a new state to states.
     * @param state
     */
    private void addState(State state) {
        while (states.size() + 1 > MAX_STATES && states.size() > 1) {
            states.removeFirst();
        }
        states.add(state);
    }
}
