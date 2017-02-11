package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.shared.invoker.InvocationRequest;

/**
 * @author Karl Bennett
 */
interface InvocationRequestFactory {

    InvocationRequest create(MultiInvokerConfiguration configuration);
}
