package shiver.me.timbers.plugins.invoker.multi;

/**
 * @author Karl Bennett
 */
interface MultiInvokerConfigurationFactory {

    MultiInvokerConfiguration copy(MultiInvokerConfiguration configuration);

    MultiInvokerConfigurationBuilder buildFrom(MultiInvokerConfiguration configuration);
}
