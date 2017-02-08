package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.ArrayList;
import java.util.List;

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
        final MultiInvokerConfiguration copy = configurationFactory.copy(configuration);
        final String invocationId = copy.getInvocationId();
        final List<String> replacedProfiles = replace(copy.getProfiles(), invocationId);
        final List<String> replacedGoals = replace(copy.getGoals(), invocationId);
        return new MultiInvokerConfiguration() {
            @Override
            public Log getLog() {
                return copy.getLog();
            }

            @Override
            public String getInvocationId() {
                return invocationId;
            }

            @Override
            public boolean isForEachProfile() {
                return copy.isForEachProfile();
            }

            @Override
            public List<String> getInvocations() {
                return copy.getInvocations();
            }

            @Override
            public List<String> getProfiles() {
                return replacedProfiles;
            }

            @Override
            public List<String> getGoals() {
                return replacedGoals;
            }
        };
    }

    private List<String> replace(List<String> profiles, String invocationId) {
        final List<String> replacedProfiles = new ArrayList<>(profiles.size());
        for (String profile : profiles) {
            replacedProfiles.add(stringReplacer.replace(profile, "@invocation.id@", invocationId));
        }
        return replacedProfiles;
    }
}
