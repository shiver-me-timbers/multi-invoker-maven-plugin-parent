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
        if (configuration.isForEachProfile()) {
            return createRequestsForProfiles(project, session, configuration);
        }
        return createRequestsForItems(project, session, configuration);
    }

    private List<InvocationRequest> createRequestsForProfiles(
        MavenProject project,
        MavenSession session,
        MultiInvokerConfiguration configuration
    ) {
        final List<InvocationRequest> requests = new ArrayList<>();
        for (Profile profile : project.getModel().getProfiles()) {
            requests.add(
                requestFactory.create(
                    project,
                    session,
                    configurationFactory.forProfile(configuration, profile)
                )
            );
        }
        return requests;
    }

    private List<InvocationRequest> createRequestsForItems(
        MavenProject project,
        MavenSession session,
        MultiInvokerConfiguration configuration
    ) {
        final List<InvocationRequest> requests = new ArrayList<>();
        for (String item : configuration.getItems()) {
            requests.add(
                requestFactory.create(
                    project,
                    session,
                    configurationFactory.forItem(configuration, item)
                )
            );
        }
        return requests;
    }
}
