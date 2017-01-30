package shiver.me.timbers.plugins.invoker.multi;

import org.codehaus.plexus.component.annotations.Component;

/**
 * @author Karl Bennett
 */
@Component(role = MultiInvokerConfigurationBuilderFactory.class, hint = "default")
class DefaultMultiInvokerConfigurationBuilderFactory implements MultiInvokerConfigurationBuilderFactory {

    @Override
    public MultiInvokerConfigurationBuilder createWith(MultiInvokerConfiguration configuration) {
        return new ConfigurationMultiInvokerConfigurationBuilder(configuration);
    }
}
