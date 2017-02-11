package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.console.ConsoleLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Karl Bennett
 */
class ConfigurationMultiInvokerConfigurationBuilder implements MultiInvokerConfigurationBuilder {

    private final boolean forEachProfile;
    private final List<String> invocations;
    private final List<String> profiles;
    private String invocationId;
    private final Log log;
    private final List<String> goals;
    private final Properties properties;
    private LogLevel logLevel;

    ConfigurationMultiInvokerConfigurationBuilder(MultiInvokerConfiguration configuration) {
        forEachProfile = configuration.isForEachProfile();
        invocations = new ArrayList<>(configuration.getInvocations());
        profiles = new ArrayList<>(configuration.getProfiles());
        invocationId = configuration.getInvocationId();
        log = configuration.getLog();
        goals = new ArrayList<>(configuration.getGoals());
        properties = new Properties();
        properties.putAll(configuration.getProperties());
    }

    @Override
    public MultiInvokerConfigurationBuilder withLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
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
            public Log getLog() {
                return logLevel != null ?
                    new DefaultLog(new ConsoleLogger(logLevel.getThreshold(), "multi-invoker")) : log;
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
