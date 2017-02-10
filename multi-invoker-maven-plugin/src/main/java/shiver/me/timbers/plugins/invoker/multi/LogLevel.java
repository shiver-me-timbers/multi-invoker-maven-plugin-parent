package shiver.me.timbers.plugins.invoker.multi;

/**
 * @author Karl Bennett
 */
enum LogLevel {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3),
    FATAL(4),
    DISABLED(5);

    private final int threshold;

    LogLevel(int threshold) {
        this.threshold = threshold;
    }

    public int getThreshold() {
        return threshold;
    }
}
