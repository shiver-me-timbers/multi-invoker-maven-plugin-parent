package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Karl Bennett
 */
interface LogFactory {
    Log create(LogLevel logLevel);
}
