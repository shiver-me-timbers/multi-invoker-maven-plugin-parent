package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.shared.invoker.InvocationRequest;

import java.util.List;

/**
 * @author Karl Bennett
 */
interface InvocationRequestsFactory {

    List<InvocationRequest> create(MultiInvokerConfiguration configuration);
}
