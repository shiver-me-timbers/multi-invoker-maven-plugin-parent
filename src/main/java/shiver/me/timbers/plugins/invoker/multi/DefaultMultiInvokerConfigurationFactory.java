package shiver.me.timbers.plugins.invoker.multi;

import org.codehaus.plexus.component.annotations.Component;

/**
 * @author Karl Bennett
 */
@Component(role = MultiInvokerConfigurationFactory.class, hint = "default")
class DefaultMultiInvokerConfigurationFactory implements MultiInvokerConfigurationFactory {

    @Override
    public MultiInvokerConfiguration copy(MultiInvokerConfiguration configuration) {
        return new ImmutableMultiInvokerConfiguration(configuration);
    }

    @Override
    public MultiInvokerConfigurationBuilder buildFrom(MultiInvokerConfiguration configuration) {
        return new ConfigurationMultiInvokerConfigurationBuilder(configuration);
    }
}
