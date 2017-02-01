package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.model.Profile;

/**
 * @author Karl Bennett
 */
interface MultiInvokerConfigurationFactory {

    MultiInvokerConfiguration copy(MultiInvokerConfiguration configuration);

    MultiInvokerConfiguration forProfile(MultiInvokerConfiguration configuration, Profile profile);

    MultiInvokerConfiguration forItem(MultiInvokerConfiguration configuration, String item);
}
