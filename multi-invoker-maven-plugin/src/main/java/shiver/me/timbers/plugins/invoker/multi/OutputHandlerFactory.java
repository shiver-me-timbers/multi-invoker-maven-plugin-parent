package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.shared.invoker.InvocationOutputHandler;

/**
 * @author Karl Bennett
 */
interface OutputHandlerFactory {

    InvocationOutputHandler createInfoFrom(Log log);

    InvocationOutputHandler createErrorFrom(Log log);
}
