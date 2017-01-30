package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karl Bennett
 */
class ImmutableMultiInvokerConfiguration implements MultiInvokerConfiguration {

    private final boolean forEachProfile;
    private final List<String> profiles;
    private final String invocationId;
    private final Log log;

    ImmutableMultiInvokerConfiguration(MultiInvokerConfiguration configuration) {
        forEachProfile = configuration.isForEachProfile();
        profiles = new ArrayList<>(configuration.getProfiles());
        invocationId = configuration.getInvocationId();
        log = configuration.getLog();
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public String getInvocationId() {
        return invocationId;
    }

    @Override
    public boolean isForEachProfile() {
        return forEachProfile;
    }

    @Override
    public List<String> getProfiles() {
        return profiles;
    }
}
