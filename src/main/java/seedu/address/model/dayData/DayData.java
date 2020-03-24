package seedu.address.model.dayData;

public class DayData {

    private final Date date;
    private final PomDurationData pomDurationData;
    private final TasksDoneData tasksDoneData;

    public DayData(Date date, PomDurationData pomDurationData, TasksDoneData tasksDoneData) {
        this.date = date;
        this.pomDurationData = pomDurationData;
        this.tasksDoneData = tasksDoneData;
    }

    public DayData(Date date) {
        this.date = date;
        this.pomDurationData = new PomDurationData();
        this.tasksDoneData = new TasksDoneData();
    }

    public Date getDate() {
        return this.date;
    }

    public PomDurationData getPomDurationData() {
        return this.pomDurationData;
    }

    public TasksDoneData getTasksDoneData() {
        return this.tasksDoneData;
    }

    /*
    public void setDate(Date date) {
        this.date = date;
    }

    public void setPomDurationData(PomDurationData pomDurationData) {
        this.pomDurationData = pomDurationData;
    }

    public void setTasksDoneData(TasksDoneData tasksDoneData) {
        this.tasksDoneData = tasksDoneData;
    }
    */

}
