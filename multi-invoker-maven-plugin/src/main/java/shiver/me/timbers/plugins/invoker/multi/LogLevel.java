package shiver.me.timbers.plugins.invoker.multi;

import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
enum LogLevel {
    DEBUG(0, "DEBUG"),
    INFO(1, "INFO"),
    WARN(2, "WARNING"),
    ERROR(3, "ERROR"),
    FATAL(4, "FATAL"),
    DISABLED(5, "DISABLED");

    private final int threshold;
    private final String longName;

    LogLevel(int threshold, String longName) {
        this.threshold = threshold;
        this.longName = longName;
    }

    public int getThreshold() {
        return threshold;
    }

    public String longName() {
        return longName;
    }

    public String prefix() {
        return format("[%s]", longName());
    }
}
