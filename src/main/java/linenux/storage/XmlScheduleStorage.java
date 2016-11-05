package linenux.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import linenux.config.Config;
import linenux.model.Schedule;
import linenux.model.adapted.AdaptedSchedule;
import linenux.util.LogsCenter;
import linenux.util.ThrowableUtil;
import linenux.view.Alerts;

//@@author A0135788M
public class XmlScheduleStorage implements ScheduleStorage {
    private static Logger logger = LogsCenter.getLogger(XmlScheduleStorage.class);

    private Config config;

    public XmlScheduleStorage(Config config) {
        this.config = config;
    }

    @Override
    public Schedule loadScheduleFromFile() {
        logger.info("Loading schedule from " + this.getFilePath());

        Schedule output;
        try {
            JAXBContext context = JAXBContext.newInstance(AdaptedSchedule.class);
            Unmarshaller u = context.createUnmarshaller();

            if (!hasScheduleFile()) {
                createFile();
            }

            AdaptedSchedule aSchedule = (AdaptedSchedule) u.unmarshal(this.getFilePath().toFile());
            output = aSchedule.convertToModel();
        } catch (Exception e) {
            logger.warning(ThrowableUtil.getStackTrace(e));
            Alerts.alert("Error Reading Schedule", "Schedule cannot be read from\n" + this.getFilePath().toString() + "\nPlease use the load command to load another schedule.");
            output = new Schedule();
        }

        logger.info("Done loading schedule from " + this.getFilePath());
        return output;
    }

    @Override
    public void saveScheduleToFile(Schedule schedule) {
        logger.info("Saving schedule to " + this.getFilePath());

        try {
            AdaptedSchedule aSchedule = new AdaptedSchedule();
            aSchedule.convertToXml(schedule);
            JAXBContext context = JAXBContext.newInstance(aSchedule.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            if (!hasScheduleFile()) {
                createFile();
            }

            m.marshal(aSchedule, this.getFilePath().toFile());
        } catch (Exception e) {
            logger.warning(ThrowableUtil.getStackTrace(e));
            Alerts.alert("Error Writing Schedule", "Schedule cannot be saved to\n" + this.getFilePath().toString() + "\nPlease use the save command to specify another location.");
        }

        logger.info("Done saving schedule to " + this.getFilePath());
    }

    @Override
    public boolean hasScheduleFile() {
        return Files.exists(this.getFilePath());
    }

    private void createFile() throws IOException {
        logger.info("Creating " + this.getFilePath());

        try {
            Files.createFile(this.getFilePath());
        } catch (IOException i) {
            new File(this.getFilePath().getParent().toString()).mkdirs();
            createFile();
        } catch (Exception e) {
            logger.warning(ThrowableUtil.getStackTrace(e));
            Alert alert = throwAlert("Creating File Error", "Could not create file at: \n" + this.getFilePath().toString());
            alert.showAndWait();
        }

        logger.info("Done creating " + this.getFilePath());
    }

    private Alert throwAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert;
    }

    private Path getFilePath() {
        return Paths.get(this.config.getScheduleFilePath());
    }
}
