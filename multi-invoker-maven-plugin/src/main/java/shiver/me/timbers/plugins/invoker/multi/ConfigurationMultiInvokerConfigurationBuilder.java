package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Karl Bennett
 */
class ConfigurationMultiInvokerConfigurationBuilder implements MultiInvokerConfigurationBuilder {

    private final MavenProject project;
    private final MavenSession session;
    private final boolean forEachProfile;
    private final List<String> invocations;
    private final List<String> profiles;
    private String invocationId;
    private Log log;
    private final List<String> goals;
    private final Properties properties;
    private final LogFactory logFactory;

    ConfigurationMultiInvokerConfigurationBuilder(MultiInvokerConfiguration configuration, LogFactory logFactory) {
        this.project = configuration.getProject();
        this.session = configuration.getSession();
        this.forEachProfile = configuration.isForEachProfile();
        this.invocations = new ArrayList<>(configuration.getInvocations());
        this.profiles = new ArrayList<>(configuration.getProfiles());
        this.invocationId = configuration.getInvocationId();
        this.log = configuration.getLog();
        this.goals = new ArrayList<>(configuration.getGoals());
        this.properties = new Properties();
        this.properties.putAll(configuration.getProperties());
        this.logFactory = logFactory;
    }

    @Override
    public MultiInvokerConfigurationBuilder withLogLevel(LogLevel logLevel) {
        // If no log level is supplied then keep using the default log.
        if (logLevel == null) {
            return this;
        }
        this.log = logFactory.create(logLevel);
        return this;
    }

    @Override
    public MultiInvokerConfigurationBuilder withInvocationId(String invocationId) {
        this.invocationId = invocationId;
        return this;
    }

    @Override
    public MultiInvokerConfigurationBuilder withProfile(String profile) {
        profiles.add(profile);
        return this;
    }

    @Override
    public MultiInvokerConfiguration build() {
        return new MultiInvokerConfiguration() {
            @Override
            public MavenProject getProject() {
                return project;
            }

            @Override
            public MavenSession getSession() {
                return session;
            }

            @Override
            public Log getLog() {
                return log;
            }

            @Override
            public String getInvocationId() {
                return invocationId;
            }

            @Override
            public boolean isForEachProfile() {
                return forEachProfile;
            }

            @Override
            public List<String> getInvocations() {
                return invocations;
            }

            @Override
            public List<String> getProfiles() {
                return profiles;
            }

            @Override
            public List<String> getGoals() {
                return goals;
            }

            @Override
            public Properties getProperties() {
                return properties;
            }
        };
    }
}
