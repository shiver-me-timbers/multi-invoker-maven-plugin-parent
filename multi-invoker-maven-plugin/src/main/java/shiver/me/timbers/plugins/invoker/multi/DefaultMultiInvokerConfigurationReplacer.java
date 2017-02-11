package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Karl Bennett
 */
@Component(role = MultiInvokerConfigurationReplacer.class, hint = "default")
class DefaultMultiInvokerConfigurationReplacer implements MultiInvokerConfigurationReplacer {

    @Requirement
    private MultiInvokerConfigurationFactory configurationFactory;

    @Requirement
    private StringReplacer stringReplacer;

    DefaultMultiInvokerConfigurationReplacer() {
    }

    DefaultMultiInvokerConfigurationReplacer(
        MultiInvokerConfigurationFactory configurationFactory,
        StringReplacer stringReplacer
    ) {
        this.configurationFactory = configurationFactory;
        this.stringReplacer = stringReplacer;
    }

    @Override
    public MultiInvokerConfiguration resolveSubstitutions(MultiInvokerConfiguration configuration) {
        return new ResolvedMultiInvokerConfiguration(configurationFactory.copy(configuration));
    }

    private List<String> replace(List<String> profiles, String invocationId) {
        final List<String> replacedProfiles = new ArrayList<>(profiles.size());
        for (String profile : profiles) {
            replacedProfiles.add(stringReplacer.replace(profile, "@invocation.id@", invocationId));
        }
        return replacedProfiles;
    }

    private class ResolvedMultiInvokerConfiguration implements MultiInvokerConfiguration {

        private final MultiInvokerConfiguration configuration;
        private final String invocationId;
        private final List<String> replacedProfiles;
        private final List<String> replacedGoals;

        private ResolvedMultiInvokerConfiguration(MultiInvokerConfiguration configuration) {
            this.configuration = configuration;
            this.invocationId = configuration.getInvocationId();
            this.replacedProfiles = replace(configuration.getProfiles(), invocationId);
            this.replacedGoals = replace(configuration.getGoals(), invocationId);
        }

        @Override
        public MavenProject getProject() {
            return configuration.getProject();
        }

        @Override
        public MavenSession getSession() {
            return configuration.getSession();
        }

        @Override
        public Log getLog() {
            return configuration.getLog();
        }

        @Override
        public String getInvocationId() {
            return invocationId;
        }

        @Override
        public boolean isForEachProfile() {
            return configuration.isForEachProfile();
        }

        @Override
        public List<String> getInvocations() {
            return configuration.getInvocations();
        }

        @Override
        public List<String> getProfiles() {
            return replacedProfiles;
        }

        @Override
        public List<String> getGoals() {
            return replacedGoals;
        }

        @Override
        public Properties getProperties() {
            return configuration.getProperties();
        }
    }
}
