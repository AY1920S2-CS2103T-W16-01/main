package seedu.address.logic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import seedu.address.model.Pet;
import seedu.address.ui.PetDisplay;

public class PetManager {

    public final String HANGRY_MOOD_STRING = "HANGRY";
    public final String HAPPY_MOOD_STRING = "HAPPY";

    private Pet pet;
    private LocalDateTime lastDoneTaskTime;
    private LocalDateTime timeForHangry;
    private PetDisplay petDisplay;
    private Timer timer;
    private TimerTask timerTask;
    // private Timer secondTimer;
    // private Timer thirdTimer;
    private boolean hasStarted;

    public PetManager() {
        this.timer = new Timer();
        this.timerTask =
                new TimerTask() {
                    public void run() {
                        changeToHangry();
                        updatePetDisplayToHangry();
                    }
                };
        this.hasStarted = false;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
        this.lastDoneTaskTime = LocalDateTime.parse(pet.getLastDoneTaskTime());
        // use this for ACTUAL
        // this.timeForHangry = lastDoneTaskTime.plusHours(24);
        // use this for TESTING
        this.timeForHangry = lastDoneTaskTime.plusMinutes(1);
    }

    public void setPetDisplay(PetDisplay petDisplay) {
        this.petDisplay = petDisplay;
    }

    public void updateMoodWhenLogIn() {
        LocalDateTime now = LocalDateTime.now();
        if (pet.getMood().equals(HAPPY_MOOD_STRING)) {
            Duration duration = Duration.between(now, timeForHangry);
            if (duration.isNegative()) {
                changeToHangry();
            } else {
                hasStarted = true;
                // System.out.println("HASSTARTED IS TRUE WHEN LOG IN");
                Date timeForMoodChange =
                        Date.from(timeForHangry.atZone(ZoneId.systemDefault()).toInstant());
                // System.out.println("task is scheduled when logged in");
                timer.schedule(timerTask, timeForMoodChange);

                // System.out.print("THIS IS FIRST COUNTDOWN??!!!");
                // secondTimer = new Timer();
                // TimerTask task = new TimerTask() {
                // public void run() {
                // Duration duration = Duration.between(LocalDateTime.now(), timeForHangry);
                // System.out.println("DURATION LOG IN: " + duration);
                // if (duration.isNegative()) {
                // cancel();
                // }
                // }
                // };
                // Date date =
                // Date.from(lastDoneTaskTime.atZone(ZoneId.systemDefault()).toInstant());
                // secondTimer.scheduleAtFixedRate(task, date, 10000);

            }
        }
    }

    public void updateMoodWhenTaskDone() {
        if (hasStarted) {
            // System.out.println("timer has been cancelled starting new one");
            // secondTimer.cancel();
            timer.cancel();
        }
        timer = new Timer();
        lastDoneTaskTime = LocalDateTime.now();
        pet.setLastDoneTaskTime(lastDoneTaskTime.toString());
        // For ACTUAL
        // timeForHangry = lastDoneTaskTime.plusHours(24);
        // For TESTING
        timeForHangry = lastDoneTaskTime.plusMinutes(1);
        timerTask =
                new TimerTask() {
                    @Override
                    public void run() {
                        changeToHangry();
                        updatePetDisplayToHangry();
                    }
                };
        Date timeForMoodChange =
                Date.from(timeForHangry.atZone(ZoneId.systemDefault()).toInstant());
        timer.schedule(timerTask, timeForMoodChange);
        changeToHappy();

        // System.out.print("SCEHDULE TIMERTASK....... THIS IS FINAL COUNTDOWN??!!!");
        // thirdTimer = new Timer();
        // TimerTask task = new TimerTask() {
        // public void run() {
        // Duration duration = Duration.between(LocalDateTime.now(), timeForHangry);
        // System.out.println("DURATION: " + duration);
        // if (duration.isNegative()) {
        // cancel();
        // }
        // }
        // };
        // Date date =
        // Date.from(lastDoneTaskTime.atZone(ZoneId.systemDefault()).toInstant());
        // thirdTimer.scheduleAtFixedRate(task, date, 10000);
    }

    public void updatePetDisplayWhenDone() {
        Path petFilepath;
        String levelText = this.pet.getLevel();

        if (levelText.equals("1")) {
            petFilepath = Paths.get("images", "pet", "level1.png");
        } else if (levelText.equals("2")) {
            petFilepath = Paths.get("images", "pet", "level2.png");
        } else {
            petFilepath = Paths.get("images", "pet", "level3.png");
        }

        petDisplay.setPetImage(petFilepath);
    }

    public void updatePetDisplayToHangry() {
        Path petFilepath;
        String levelText = this.pet.getLevel();

        if (levelText.equals("1")) {
            petFilepath = Paths.get("images", "pet", "level1hangry.png");
        } else if (levelText.equals("2")) {
            petFilepath = Paths.get("images", "pet", "level2hangry.png");
        } else {
            petFilepath = Paths.get("images", "pet", "level3.png");
        }

        petDisplay.setPetImage(petFilepath);
    }

    public void handleExit() {
        hasStarted = false;
        timer.cancel();
        // secondTimer.cancel();
        // thirdTimer.cancel();
    }

    void changeToHangry() {
        pet.changeHangry();
    }

    void changeToHappy() {
        pet.changeHappy();
    }

    public void updatePetDisplay() {
        int exp = Integer.parseInt(pet.getExp());
        int expBarInt = exp % 100;
        String expBarText = String.format("%d XP / 100 XP", expBarInt);

        String levelText = this.pet.getLevel();

        String mood = pet.getMood();
        Path petFilepath;

        if (levelText.equals("1")) {
            Path path =
                    (mood.equals(HAPPY_MOOD_STRING)
                            ? Paths.get("images", "pet", "level1.png")
                            : Paths.get("images", "pet", "level1hangry.png"));
            petFilepath = path;
        } else if (levelText.equals("2")) {
            Path path =
                    (mood.equals(HAPPY_MOOD_STRING)
                            ? Paths.get("images", "pet", "level1.png")
                            : Paths.get("images", "pet", "level1hangry.png"));
            petFilepath = path;
        } else {
            petFilepath = Paths.get("images", "pet", "level3.png");
        }

        int expBarPerc = expBarInt / 10;

        Path expBarFilepath;

        switch (expBarPerc) {
            case 0:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar0%.png");
                break;

            case 1:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar10%.png");
                break;

            case 2:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar20%.png");
                break;

            case 3:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar30%.png");
                break;

            case 4:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar40%.png");
                break;

            case 5:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar50%.png");
                break;

            case 6:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar60%.png");
                break;

            case 7:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar70%.png");
                break;

            case 8:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar80%.png");
                break;

            case 9:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar90%.png");
                break;

            case 10:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar100%.png");
                break;

            default:
                expBarFilepath = Paths.get("images", "pet", "ProgressBar0%.png");
                break;
        }

        petDisplay.setExpBarText(expBarText);
        petDisplay.setLevelText(levelText);

        // set up pet image
        petDisplay.setPetImage(petFilepath);

        // set up experience bar image
        petDisplay.setExpBarImage(expBarFilepath);

        // update pet name
        petDisplay.setPetName(this.pet.getName());
    }

    public String getPetName() {
        return pet.getName();
    }
}
