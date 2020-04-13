package seedu.address;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Version;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.logic.PetManager;
import seedu.address.logic.PomodoroManager;
import seedu.address.logic.StatisticsManager;
import seedu.address.model.InvalidPetException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Pet;
import seedu.address.model.Pomodoro;
import seedu.address.model.ReadOnlyPet;
import seedu.address.model.ReadOnlyPomodoro;
import seedu.address.model.ReadOnlyStatistics;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.Statistics;
import seedu.address.model.TaskList;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.JsonPetStorage;
import seedu.address.storage.JsonPomodoroStorage;
import seedu.address.storage.JsonStatisticsStorage;
import seedu.address.storage.JsonTaskListStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.PetStorage;
import seedu.address.storage.PomodoroStorage;
import seedu.address.storage.StatisticsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.TaskListStorage;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/** Runs the application. */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 3, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected PomodoroManager pomodoroManager;
    protected PetManager petManager;
    protected StatisticsManager statisticsManager;

    @Override
    public void init() throws Exception {
        logger.info(
                "=============================[ Initializing TaskList ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        TaskListStorage taskListStorage = new JsonTaskListStorage(userPrefs.getTaskListFilePath());
        PetStorage petStorage = new JsonPetStorage(userPrefs.getPetFilePath());
        PomodoroStorage pomodoroStorage = new JsonPomodoroStorage(userPrefs.getPomodoroFilePath());
        StatisticsStorage statisticsStorage =
                new JsonStatisticsStorage(userPrefs.getStatisticsFilePath());

        storage =
                new StorageManager(
                        taskListStorage,
                        petStorage,
                        pomodoroStorage,
                        statisticsStorage,
                        userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        pomodoroManager = new PomodoroManager(model);

        PetManager petManager = new PetManager();

        StatisticsManager statisticsManager = new StatisticsManager();

        pomodoroManager.setDefaultStartTime(
                Float.valueOf(model.getPomodoro().getDefaultTime()).floatValue());

        pomodoroManager.setRestTime(Float.valueOf(model.getPomodoro().getRestTime()).floatValue());

        model.setPomodoroManager(pomodoroManager);

        model.setPetManager(petManager);

        model.setStatisticsManager(statisticsManager);

        ui = new UiManager(logic, pomodoroManager, petManager, statisticsManager);

        model.addObserver(ui);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s task list and {@code
     * userPrefs}. <br>
     * The data from the sample task list will be used instead if {@code storage}'s task list is not
     * found, or an empty task list will be used instead if errors occur when reading {@code
     * storage}'s task list.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        Optional<ReadOnlyTaskList> taskListOptional;
        Optional<ReadOnlyPet> petOptional;
        Optional<ReadOnlyPomodoro> pomodoroOptional;
        Optional<ReadOnlyStatistics> statisticsOptional;

        ReadOnlyTaskList initialData;
        ReadOnlyPet initialPet;
        ReadOnlyPomodoro initialPomodoro;
        ReadOnlyStatistics statistics;

        try {
            taskListOptional = storage.readTaskList();
            if (!taskListOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample TaskList");
            }
            initialData = taskListOptional.orElseGet(SampleDataUtil::getSampleTaskList);
        } catch (DataConversionException e) {
            logger.warning(
                    "Data file not in the correct format. Will be starting with an empty TaskList");
            initialData = new TaskList();
        } catch (IOException e) {
            logger.warning(
                    "Problem while reading from the file. Will be starting with an empty TaskList");
            initialData = new TaskList();
        }

        try {
            petOptional = storage.readPet();
            if (!petOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample Pet");
            }
            initialPet = petOptional.orElse(new Pet());
        } catch (DataConversionException e) {
            logger.warning(
                    "Data file not in the correct format. Will be starting with an empty Pet");
            initialPet = new Pet();
        } catch (IOException e) {
            logger.warning(
                    "Problem while reading from the file. Will be starting with an empty Pet");
            initialPet = new Pet();
        } catch (InvalidPetException e) {
            logger.warning(String.format("%s. Will be starting with an empty Pet", e.toString()));
            initialPet = new Pet();
        }

        try {
            pomodoroOptional = storage.readPomodoro();
            if (!pomodoroOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample Pomodoro");
            }
            initialPomodoro = pomodoroOptional.orElse(new Pomodoro());
        } catch (DataConversionException e) {
            logger.warning(
                    "Data file not in the correct format. Will be starting with an empty Pomodoro");
            initialPomodoro = new Pomodoro();
        } catch (IOException e) {
            logger.warning(
                    "Problem while reading from the file. Will be starting with an empty Pomodoro");
            initialPomodoro = new Pomodoro();
        }

        try {
            statisticsOptional = storage.readStatistics();
            if (!statisticsOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample Statistics");
            }
            statistics = statisticsOptional.orElse(new Statistics());
        } catch (DataConversionException e) {
            logger.warning(
                    "Data file not in the correct format. Will be starting with an empty Statistics");
            statistics = new Statistics();
        } catch (IOException e) {
            logger.warning(
                    "Problem while reading from the file. Will be starting with an empty Statistics");
            statistics = new Statistics();
        }

        return new ModelManager(initialData, initialPet, initialPomodoro, statistics, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead if {@code
     * configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning(
                    "Config file at "
                            + configFilePathUsed
                            + " is not in the correct format. "
                            + "Using default config properties");
            initializedConfig = new Config();
        }

        // Update config file in case it was missing to begin with or there are
        // new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path, or a
     * new {@code UserPrefs} with default configuration if errors occur when reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning(
                    "UserPrefs file at "
                            + prefsFilePath
                            + " is not in the correct format. "
                            + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning(
                    "Problem while reading from the file. Will be starting with an empty TaskList");
            initializedPrefs = new UserPrefs();
        }

        // Update prefs file in case it was missing to begin with or there are
        // new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting TaskList " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info(
                "============================ [ Stopping Address Book ] =============================");
        try {
            storage.savePomodoro(model.getPomodoro());
            storage.saveStatistics(model.getStatistics());
            storage.saveUserPrefs(model.getUserPrefs());
            System.exit(0);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
