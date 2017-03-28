package shiver.me.timbers.plugins.invoker.multi;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

/**
 * @author Karl Bennett
 */
@Component(role = MultiInvokerConfigurationBuilderFactory.class, hint = "default")
class DefaultMultiInvokerConfigurationBuilderFactory implements MultiInvokerConfigurationBuilderFactory {

    @Requirement
    private LogFactory logFactory;

    DefaultMultiInvokerConfigurationBuilderFactory() {
    }

    DefaultMultiInvokerConfigurationBuilderFactory(LogFactory logFactory) {
        this.logFactory = logFactory;
    }

    @Override
    public MultiInvokerConfigurationBuilder createWith(MultiInvokerConfiguration configuration) {
        return new ConfigurationMultiInvokerConfigurationBuilder(configuration, logFactory);
    }
}
