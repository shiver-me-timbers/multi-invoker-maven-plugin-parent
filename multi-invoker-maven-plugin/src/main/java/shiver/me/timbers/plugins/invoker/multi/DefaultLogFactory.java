package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.console.ConsoleLogger;

/**
 * @author Karl Bennett
 */
class DefaultLogFactory implements LogFactory {

    @Override
    public Log create(LogLevel logLevel) {
        return new DefaultLog(new ConsoleLogger(logLevel.getThreshold(), "multi-invoker"));

    }
}
