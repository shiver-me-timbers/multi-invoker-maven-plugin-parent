package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.shared.invoker.InvocationRequest;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
@Component(role = MultiInvokerLogger.class, hint = "default")
class DefaultMultiInvokerLogger implements MultiInvokerLogger {

    @Requirement
    private MavenStrings mavenStrings;

    DefaultMultiInvokerLogger() {
    }

    DefaultMultiInvokerLogger(MavenStrings mavenStrings) {
        this.mavenStrings = mavenStrings;
    }

    @Override
    public void log(MultiInvokerConfiguration configuration, InvocationRequest request) {
        configuration.getLog().info(
            format(
                "Invoking: mvn %s%s%s",
                mavenStrings.toGoals(request),
                mavenStrings.toProfiles(request),
                mavenStrings.toProperties(configuration, request)
            )
        );
    }
}
