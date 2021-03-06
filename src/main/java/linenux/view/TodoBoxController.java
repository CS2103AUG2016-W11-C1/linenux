package linenux.view;

import java.util.ArrayList;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import linenux.control.ControlUnit;
import linenux.model.State;
import linenux.model.Task;
import linenux.util.ArrayListUtil;
import linenux.view.components.TodoCell;

//@@author A0127694U
/**
 * Controller for the todo box, which displays all the todos
 */
public class TodoBoxController {
    @FXML
    private ListView<Task> todosList;

    private ControlUnit controlUnit;
    private ObservableList<Task> todos = FXCollections.observableArrayList();

    /**
     * Initializes subviews.
     */
    @FXML
    private void initialize() {
        todosList.itemsProperty().setValue(todos);
        todosList.setCellFactory(TodoCell::new);
    }

    /**
     * Update the application {@code ControlUnit}.
     * @param controlUnit The new {@code ControlUnit}.
     */
    public void setControlUnit(ControlUnit controlUnit) {
        this.controlUnit = controlUnit;
        updateTodos();
        this.controlUnit.getSchedule().getStates().addListener((ListChangeListener<? super State>) c -> {
            updateTodos();
        });
        this.controlUnit.getSchedule().getFilteredTaskList().addListener((ListChangeListener<? super ArrayList<Task>>) c -> {
            updateFilteredTodos();
        });
    }

    /**
     * Render new todos.
     */
    private void updateTodos() {
        ArrayList<Task> tasks = this.controlUnit.getSchedule().getTaskList();
        ArrayList<Task> undoneTasks = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(((Predicate<Task>) Task::isDone).negate())
                .value();
        ArrayList<Task> todos = filterToDos(undoneTasks);
        this.todos.setAll(todos);
    }

    /**
     * Render filtered todos.
     */
    private void updateFilteredTodos() {
        ArrayList<Task> filteredTasks = this.controlUnit.getSchedule().getFilteredTasks();
        ArrayList<Task> todos = filterToDos(filteredTasks);
        this.todos.setAll(todos);
    }

    /**
     * Sieve out only todos.
     * @param tasks The original {@code ArrayList} of {@code Task}.
     * @return An {@code ArrayList} of {@code Task} which are todos.
     */
    private ArrayList<Task> filterToDos(ArrayList<Task> tasks) {
        ArrayList<Task> todos = new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isTodo)
                .sortBy(Task::getTaskName)
                .value();

        return todos;
    }
}
