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
import java.util.Properties;

import static java.lang.String.format;
import static java.util.Arrays.asList;
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
    private MavenStrings mvns;

    @Component
    private Invoker invoker;

    @Parameter
    private LogLevel logLevel;

    @Parameter(defaultValue = "false")
    private boolean forEachProfile;

    @Parameter
    private String invocations;

    @Parameter
    private String profiles;

    @Parameter
    private String goals;

    @Parameter
    private Properties properties;

    /**
     * All Maven plugins must have a default constructor.
     */
    MultiInvokerMojo() {
    }

    MultiInvokerMojo(
        LogLevel logLevel,
        MavenProject project,
        MavenSession session,
        MultiInvokerConfigurationFactory configurationFactory,
        InvocationRequestsFactory requestsFactory,
        MavenStrings mvns,
        Invoker invoker
    ) {
        this.logLevel = logLevel;
        this.project = project;
        this.session = session;
        this.configurationFactory = configurationFactory;
        this.requestsFactory = requestsFactory;
        this.mvns = mvns;
        this.invoker = invoker;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isRunFromMultiInvocation()) {
            return;
        }
        final List<InvocationRequest> requests = requestsFactory
            .create(project, session, configurationFactory.forLogLevel(this, logLevel));
        for (InvocationRequest request : requests) {
            try {
                getLog().info(format("Invoking: mvn %s %s", mvns.toGoals(request), mvns.toProfiles(request)));
                final InvocationResult result = invoker.execute(request);
                if (result.getExitCode() > 0) {
                    throw new MojoExecutionException("Multi invocation has failed.", result.getExecutionException());
                }
            } catch (MavenInvocationException e) {
                throw new MojoExecutionException("Multi invocation is invalid.", e);
            }
        }
    }

    @Override
    public String getInvocationId() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isForEachProfile() {
        return forEachProfile;
    }

    MultiInvokerMojo forEachProfile(boolean forEachProfile) {
        this.forEachProfile = forEachProfile;
        return this;
    }

    @Override
    public List<String> getProfiles() {
        return splitByCommas(profiles);
    }

    MultiInvokerMojo withProfiles(String profiles) {
        this.profiles = profiles;
        return this;
    }

    @Override
    public List<String> getInvocations() {
        return splitByCommas(invocations);
    }

    MultiInvokerMojo withInvocations(String invocations) {
        this.invocations = invocations;
        return this;
    }

    @Override
    public List<String> getGoals() {
        return splitByCommas(goals);
    }

    MultiInvokerMojo withGoals(String goals) {
        this.goals = goals;
        return this;
    }

    @Override
    public Properties getProperties() {
        return properties == null ? new Properties() : properties;
    }

    MultiInvokerMojo withProperties(Properties properties) {
        this.properties = properties;
        return this;
    }

    private boolean isRunFromMultiInvocation() {
        final Properties properties = session.getUserProperties();
        if (properties != null) {
            final String invocationId = properties.getProperty(INVOCATION_ID);
            return invocationId != null && !invocationId.isEmpty();
        }
        return false;
    }

    private static List<String> splitByCommas(String string) {
        if (string == null || string.isEmpty()) {
            return new ArrayList<>();
        }
        return asList(string.split(","));
    }
}
