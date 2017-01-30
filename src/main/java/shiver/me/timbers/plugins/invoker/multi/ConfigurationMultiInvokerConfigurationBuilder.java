package shiver.me.timbers.plugins.invoker.multi;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karl Bennett
 */
class ConfigurationMultiInvokerConfigurationBuilder implements MultiInvokerConfigurationBuilder {

    private final boolean forEachProfile;
    private final List<String> profiles;
    private String invocationId;

    ConfigurationMultiInvokerConfigurationBuilder(MultiInvokerConfiguration configuration) {
        forEachProfile = configuration.isForEachProfile();
        profiles = new ArrayList<>(configuration.getProfiles());
        invocationId = configuration.getInvocationId();
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
        });
    }
}
