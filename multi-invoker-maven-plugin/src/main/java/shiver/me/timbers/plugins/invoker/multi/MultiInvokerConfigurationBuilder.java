package shiver.me.timbers.plugins.invoker.multi;

/**
 * @author Karl Bennett
 */
interface MultiInvokerConfigurationBuilder {

    MultiInvokerConfigurationBuilder withInvocationId(String invocationId);

    MultiInvokerConfigurationBuilder withProfile(String profile);

    MultiInvokerConfiguration build();
}
