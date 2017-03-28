package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.codehaus.plexus.component.annotations.Component;

import static shiver.me.timbers.plugins.invoker.multi.LogLevel.DEBUG;
import static shiver.me.timbers.plugins.invoker.multi.LogLevel.ERROR;
import static shiver.me.timbers.plugins.invoker.multi.LogLevel.FATAL;
import static shiver.me.timbers.plugins.invoker.multi.LogLevel.WARN;

/**
 * @author Karl Bennett
 */
@Component(role = OutputHandlerFactory.class, hint = "default")
class DefaultOutputHandlerFactory implements OutputHandlerFactory {

    @Override
    public InvocationOutputHandler createInfoFrom(final Log log) {
        return new InvocationOutputHandler() {
            @Override
            public void consumeLine(String line) {
                if (line.startsWith(DEBUG.prefix())) {
                    log.debug(line);
                    return;
                }

                if (line.startsWith(WARN.prefix())) {
                    log.warn(line);
                    return;
                }

                if (line.startsWith(ERROR.prefix()) || line.startsWith(FATAL.prefix())) {
                    log.error(line);
                    return;
                }

                log.info(line);
            }
        };
    }

    @Override
    public InvocationOutputHandler createErrorFrom(final Log log) {
        return new InvocationOutputHandler() {
            @Override
            public void consumeLine(String line) {
                log.error(line);
            }
        };
    }
}
