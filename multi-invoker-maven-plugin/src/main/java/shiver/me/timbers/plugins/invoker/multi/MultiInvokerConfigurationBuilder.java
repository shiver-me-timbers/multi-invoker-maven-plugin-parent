package shiver.me.timbers.plugins.invoker.multi;

/**
 * @author Karl Bennett
 */
interface MultiInvokerConfigurationBuilder {

    MultiInvokerConfigurationBuilder withLogLevel(LogLevel logLevel);

    MultiInvokerConfigurationBuilder withInvocationId(String invocationId);

    MultiInvokerConfigurationBuilder withProfile(String profile);

    MultiInvokerConfiguration build();
}
