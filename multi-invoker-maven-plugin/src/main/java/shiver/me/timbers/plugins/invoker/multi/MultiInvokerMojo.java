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
    private MultiInvokerLogger logger;

    @Component
    private MultiInvokerConfigurationFactory configurationFactory;

    @Component
    private InvocationRequestsFactory requestsFactory;

    @Component
    private Invoker invoker;

    @Parameter(property = "multi-invoker.logLevel")
    private LogLevel logLevel;

    @Parameter(defaultValue = "false", property = "multi-invoker.forEachProfile")
    private boolean forEachProfile;

    @Parameter(property = "multi-invoker.invocations")
    private String invocations;

    @Parameter(property = "multi-invoker.profiles")
    private String profiles;

    @Parameter(property = "multi-invoker.goals")
    private String goals;

    @Parameter
    private Properties properties;

    /**
     * All Maven plugins must have a default constructor.
     */
    MultiInvokerMojo() {
    }

    MultiInvokerMojo(
        MavenProject project,
        MavenSession session,
        MultiInvokerLogger logger,
        MultiInvokerConfigurationFactory configurationFactory,
        InvocationRequestsFactory requestsFactory,
        Invoker invoker,
        LogLevel logLevel
    ) {
        this.project = project;
        this.session = session;
        this.logger = logger;
        this.configurationFactory = configurationFactory;
        this.requestsFactory = requestsFactory;
        this.invoker = invoker;
        this.logLevel = logLevel;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isRunFromMultiInvocation()) {
            return;
        }
        for (InvocationRequest request : requestsFactory.create(configurationFactory.forLogLevel(this, logLevel))) {
            try {
                logger.log(this, request);
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
    public MavenProject getProject() {
        return project;
    }

    @Override
    public MavenSession getSession() {
        return session;
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
