package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karl Bennett
 */
class ConfigurationMultiInvokerConfigurationBuilder implements MultiInvokerConfigurationBuilder {

    private final boolean forEachProfile;
    private final List<String> items;
    private final List<String> profiles;
    private String invocationId;
    private final Log log;

    ConfigurationMultiInvokerConfigurationBuilder(MultiInvokerConfiguration configuration) {
        forEachProfile = configuration.isForEachProfile();
        items = new ArrayList<>(configuration.getInvocations());
        profiles = new ArrayList<>(configuration.getProfiles());
        invocationId = configuration.getInvocationId();
        log = configuration.getLog();
    }

    @Override
    public MultiInvokerConfigurationBuilder withInvocationId(String invocationId) {
        this.invocationId = invocationId;
        return this;
    }

    @Override
    public MultiInvokerConfigurationBuilder withProfile(String profile) {
        profiles.add(profile);
        return this;
    }

    @Override
    public MultiInvokerConfiguration build() {
        return new ImmutableMultiInvokerConfiguration(new MultiInvokerConfiguration() {
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
        });
    }
}
