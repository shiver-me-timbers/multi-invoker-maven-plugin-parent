package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.model.Profile;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karl Bennett
 */
@Component(role = InvocationRequestsFactory.class, hint = "default")
class DefaultInvocationRequestsFactory implements InvocationRequestsFactory {

    @Requirement
    private MultiInvokerConfigurationFactory configurationFactory;

    @Requirement
    private InvocationRequestFactory requestFactory;

    DefaultInvocationRequestsFactory() {
    }

    DefaultInvocationRequestsFactory(
        MultiInvokerConfigurationFactory configurationFactory,
        InvocationRequestFactory requestFactory
    ) {
        this.configurationFactory = configurationFactory;
        this.requestFactory = requestFactory;
    }

    @Override
    public List<InvocationRequest> create(
        MultiInvokerConfiguration configuration
    ) {
        if (configuration.isForEachProfile()) {
            return createRequestsForProfiles(configuration);
        }
        return createRequestsForItems(configuration);
    }

    private List<InvocationRequest> createRequestsForProfiles(MultiInvokerConfiguration configuration) {
        final List<InvocationRequest> requests = new ArrayList<>();
        for (Profile profile : configuration.getProject().getModel().getProfiles()) {
            requests.add(
                requestFactory.create(
                    configurationFactory.forProfile(configuration, profile)
                )
            );
        }
        return requests;
    }

    private List<InvocationRequest> createRequestsForItems(
        MultiInvokerConfiguration configuration
    ) {
        final List<InvocationRequest> requests = new ArrayList<>();
        for (String item : configuration.getInvocations()) {
            requests.add(
                requestFactory.create(
                    configurationFactory.forInvocation(configuration, item)
                )
            );
        }
        return requests;
    }
}
