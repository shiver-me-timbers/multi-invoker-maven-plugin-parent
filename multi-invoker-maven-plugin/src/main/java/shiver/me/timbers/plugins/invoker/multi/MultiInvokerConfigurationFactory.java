package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.model.Profile;

/**
 * @author Karl Bennett
 */
interface MultiInvokerConfigurationFactory {

    MultiInvokerConfiguration copy(MultiInvokerConfiguration configuration);

    MultiInvokerConfiguration forLogLevel(MultiInvokerConfiguration configuration, LogLevel logLevel);

    MultiInvokerConfiguration forProfile(MultiInvokerConfiguration configuration, Profile profile);

    MultiInvokerConfiguration forInvocation(MultiInvokerConfiguration configuration, String item);
}
