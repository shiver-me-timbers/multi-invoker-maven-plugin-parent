package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;

import java.util.List;

/**
 * @author Karl Bennett
 */
interface MultiInvokerConfiguration {

    Log getLog();

    String getInvocationId();

    boolean isForEachProfile();

    List<String> getProfiles();
}
