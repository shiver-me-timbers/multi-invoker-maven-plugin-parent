package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import java.util.ArrayList;
import java.util.List;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;

/**
 * @author Karl Bennett
 */
@Mojo(name = "invoker", defaultPhase = PACKAGE, threadSafe = true)
public class MultiInvokerMojo extends AbstractMojo implements MultiInvokerConfiguration {

    static final String INVOCATION_ID = "invocation.id";

    @Parameter(required = true, readonly = true, defaultValue = "${project}")
    private MavenProject project;

    @Parameter(required = true, readonly = true, defaultValue = "${session}")
    private MavenSession session;

    @Component
    private MultiInvokerConfigurationFactory configurationFactory;

    @Component
    private InvocationRequestsFactory requestsFactory;

    @Component
    private Invoker invoker;

    @Parameter(readonly = true, defaultValue = "false")
    private boolean forEachProfile;

    /**
     * All Maven plugins must have a default constructor.
     */
    MultiInvokerMojo() {
    }

    MultiInvokerMojo(
        MavenProject project,
        MavenSession session,
        MultiInvokerConfigurationFactory configurationFactory,
        InvocationRequestsFactory requestsFactory,
        Invoker invoker
    ) {
        this.project = project;
        this.session = session;
        this.configurationFactory = configurationFactory;
        this.requestsFactory = requestsFactory;
        this.invoker = invoker;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final List<InvocationRequest> requests = requestsFactory
            .create(project, session, configurationFactory.copy(this));
        for (InvocationRequest request : requests) {
            try {
                final InvocationResult result = invoker.execute(request);
                if (result.getExitCode() > 0) {
                    throw new MojoExecutionException("Multi invocation has failed.", result.getExecutionException());
                }
            } catch (MavenInvocationException e) {
                throw new MojoExecutionException("Multi invocation is invalid.", e);
            }
        }
    }

    MultiInvokerMojo forEachProfile(boolean forEachProfile) {
        this.forEachProfile = forEachProfile;
        return this;
    }

    @Override
    public String getInvocationId() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isForEachProfile() {
        return forEachProfile;
    }

    @Override
    public List<String> getProfiles() {
        return new ArrayList<>();
    }
}
