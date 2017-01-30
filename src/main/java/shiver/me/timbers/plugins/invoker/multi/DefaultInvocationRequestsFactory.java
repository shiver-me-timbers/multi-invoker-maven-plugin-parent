package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Profile;
import org.apache.maven.project.MavenProject;
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
        MavenProject project,
        MavenSession session,
        MultiInvokerConfiguration configuration
    ) {
        final List<InvocationRequest> requests = new ArrayList<>();
        for (Profile profile : project.getModel().getProfiles()) {
            final String id = profile.getId();
            requests.add(requestFactory.create(
                project,
                session,
                configurationFactory.buildFrom(configuration).withInvocationId(id).withProfile(id).build())
            );
        }
        return requests;
    }
}
