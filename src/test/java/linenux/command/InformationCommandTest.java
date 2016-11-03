package linenux.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.config.Config;
import linenux.model.Schedule;

/**
 * JUnit test for Information Command.
 */
// @@author A0127694U
public class InformationCommandTest {
    public static final String VERSION_NO = "v0.5rc";
    public static final String DEFAULT_FILE_PATH = Paths.get(".").toAbsolutePath().toString();
    public static final String CONFIG_FILENAME = "ConfigTest.json";
    public static final String SCHEDULE_FILENAME = "ScheduleTest.xml";

    private InformationCommand informationCommand;
    private Schedule schedule;
    private Config config;

    @Before
    public void setupInformationCommand() {
        this.schedule = new Schedule();
        this.config = new Config();
        this.informationCommand = new InformationCommand(this.config);
    }

    /**
     * Test that respondTo detects various versions of the commands. It should
     * return true even if the format of the arguments are invalid.
     */
    @Test
    public void testRespondToInformationCommand() {
        assertTrue(this.informationCommand.respondTo("information"));
    }

    /**
     * Test that respondTo is case-insensitive.
     */
    @Test
    public void testCaseInsensitiveInformationCommand() {
        assertTrue(this.informationCommand.respondTo("inFORMatIon"));
    }

    /**
     * Test that respondTo will return false for commands not related to add
     * tasks.
     */
    @Test
    public void testNotRespondToOtherCommands() {
        assertFalse(this.informationCommand.respondTo("halp"));
    }

    /**
     * Test information command.
     */
    @Test
    public void testInformationCommand() {
        CommandResult result = this.informationCommand.execute("information");
        String output = "Version: " + this.config.getVersionNo() + "\nSchedule Location: \n"
                + this.config.getScheduleFilePath() + "\n\nCurrentWorkingDirectory: \n"
                + Paths.get("").toAbsolutePath().toString();
        assertEquals(output, result.getFeedback());
    }
}