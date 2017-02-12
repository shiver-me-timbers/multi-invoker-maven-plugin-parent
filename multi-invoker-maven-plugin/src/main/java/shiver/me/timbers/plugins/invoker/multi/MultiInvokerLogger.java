package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.shared.invoker.InvocationRequest;

/**
 * @author Karl Bennett
 */
interface MultiInvokerLogger {

    void log(MultiInvokerConfiguration configuration, InvocationRequest request);
}
