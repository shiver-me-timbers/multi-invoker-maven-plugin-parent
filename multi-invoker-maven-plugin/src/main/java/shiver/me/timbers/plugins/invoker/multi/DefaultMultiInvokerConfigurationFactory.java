package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.model.Profile;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

/**
 * @author Karl Bennett
 */
@Component(role = MultiInvokerConfigurationFactory.class, hint = "default")
class DefaultMultiInvokerConfigurationFactory implements MultiInvokerConfigurationFactory {

    @Requirement
    private MultiInvokerConfigurationBuilderFactory configurationBuilderFactory;

    DefaultMultiInvokerConfigurationFactory() {
    }

    DefaultMultiInvokerConfigurationFactory(MultiInvokerConfigurationBuilderFactory configurationBuilderFactory) {
        this.configurationBuilderFactory = configurationBuilderFactory;
    }

    @Override
    public MultiInvokerConfiguration copy(MultiInvokerConfiguration configuration) {
        return new ImmutableMultiInvokerConfiguration(configuration);
    }

    @Override
    public MultiInvokerConfiguration forProfile(MultiInvokerConfiguration configuration, Profile profile) {
        final String id = profile.getId();
        return configurationBuilderFactory.createWith(configuration).withInvocationId(id).withProfile(id).build();
    }

    @Override
    public MultiInvokerConfiguration forItem(MultiInvokerConfiguration configuration, String item) {
        return configurationBuilderFactory.createWith(configuration).withInvocationId(item).build();
    }
}
