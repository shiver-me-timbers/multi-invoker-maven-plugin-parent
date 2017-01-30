package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.codehaus.plexus.component.annotations.Component;

/**
 * @author Karl Bennett
 */
@Component(role = OutputHandlerFactory.class, hint = "default")
class DefaultOutputHandlerFactory implements OutputHandlerFactory {

    @Override
    public InvocationOutputHandler createFrom(final Log log) {
        return new InvocationOutputHandler() {
            @Override
            public void consumeLine(String line) {
                log.info(line);
            }
        };
    }
}
