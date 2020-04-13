package seedu.address.model.task;

/** Enum for Recurring type, either daily or weekly. */
public enum RecurType {
    DAILY {
        @Override
        public long getInterval() {
            return 60000l; // for testing
        }

        @Override
        public long getDayInterval() {
            return dayIntervalDays;
        }
    },
    WEEKLY {
        @Override
        public long getInterval() {
            return weekIntervalMilliseconds;
        }

        @Override
        public long getDayInterval() {
            return weekIntervalDays;
        }
    };

    public static final long weekIntervalMilliseconds = 1000l * 60 * 60 * 24 * 7;
    public static final long weekIntervalDays = 7;

    public static final long dayIntervalMilliseconds = 1000l * 60 * 60 * 24;
    public static final long dayIntervalDays = 1;

    /**
     * Gets time interval in days based on recurring type.
     *
     * @return long
     */
    public abstract long getDayInterval();

    /**
     * Gets time interval in milliseconds based on recurring type.
     *
     * @return long
     */
    public abstract long getInterval();
}
