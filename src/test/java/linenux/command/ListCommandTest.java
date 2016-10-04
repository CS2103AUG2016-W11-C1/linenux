package linenux.command;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by yihangho on 10/4/16.
 */
public class ListCommandTest {
    private Schedule schedule;
    private ListCommand listCommand;

    @Before
    public void setupListCommand() {
        this.schedule = new Schedule();
        this.listCommand = new ListCommand(this.schedule);
    }

    @Test
    public void testRespondToListWithoutParams() {
        assertTrue(this.listCommand.respondTo("list"));
    }

    @Test
    public void testCaseInsensitiveRespondToList() {
        assertTrue(this.listCommand.respondTo("LiSt"));
    }

    @Test
    public void testDoesNotRespondToOtherCommands() {
        assertFalse(this.listCommand.respondTo("whaddup"));
    }

    /**
     * Test that list without params should display all tasks
     */
    @Test
    public void testDisplayTheEntireList() {
        this.schedule.addTask(new Task("First Task"));
        this.schedule.addTask(new Task("Second Task"));

        CommandResult result = this.listCommand.execute("list");

        String expectedFeedback = "1. First Task\n2. Second Task\n";
        assertEquals(expectedFeedback, result.getFeedback());
    }
}