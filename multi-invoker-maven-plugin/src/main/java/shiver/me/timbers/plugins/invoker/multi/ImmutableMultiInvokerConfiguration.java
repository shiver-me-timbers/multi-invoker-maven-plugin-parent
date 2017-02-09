package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Karl Bennett
 */
class ImmutableMultiInvokerConfiguration implements MultiInvokerConfiguration {

    private final boolean forEachProfile;
    private final List<String> profiles;
    private final String invocationId;
    private final Log log;
    private final List<String> items;
    private final List<String> goals;
    private final Properties properties;

    ImmutableMultiInvokerConfiguration(MultiInvokerConfiguration configuration) {
        forEachProfile = configuration.isForEachProfile();
        items = new ArrayList<>(configuration.getInvocations());
        profiles = new ArrayList<>(configuration.getProfiles());
        invocationId = configuration.getInvocationId();
        log = configuration.getLog();
        goals = new ArrayList<>(configuration.getGoals());
        properties = new Properties();
        properties.putAll(configuration.getProperties());
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
    public List<String> getInvocations() {
        return items;
    }

    @Override
    public List<String> getProfiles() {
        return profiles;
    }

    @Override
    public List<String> getGoals() {
        return goals;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
