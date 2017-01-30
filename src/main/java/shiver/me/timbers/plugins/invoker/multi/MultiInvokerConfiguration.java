package shiver.me.timbers.plugins.invoker.multi;

import java.util.List;

/**
 * @author Karl Bennett
 */
interface MultiInvokerConfiguration {

    String getInvocationId();

    boolean isForEachProfile();

    List<String> getProfiles();
}
