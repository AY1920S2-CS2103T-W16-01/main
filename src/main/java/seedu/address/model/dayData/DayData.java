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

    /**
     * Returns true if both dayDatas has the same Date field. his defines a weaker notion of
     * equality between two dayDatas.
     */
    public boolean isSameDayData(DayData otherDayData) {
        if (otherDayData == this) {
            return true;
        }

        return otherDayData != null && otherDayData.getDate().equals(getDate());
    }

    /**
     * Returns true if both tasks have the same identity and data fields. This defines a stronger
     * notion of equality between two tasks.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DayData)) {
            return false;
        }

        DayData otherDayData = (DayData) other;
        return otherDayData.getDate().equals(getDate())
                && otherDayData.getPomDurationData().equals(getPomDurationData())
                && otherDayData.getTasksDoneData().equals(getTasksDoneData());
    }

    @Override
    public String toString() {
        return "DayData: "
                + this.getDate()
                + " pom: "
                + getPomDurationData()
                + " tasksDone: "
                + getTasksDoneData();
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
