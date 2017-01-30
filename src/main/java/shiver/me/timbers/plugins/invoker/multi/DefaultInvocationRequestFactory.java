package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.codehaus.plexus.component.annotations.Component;

import java.util.Map;
import java.util.Properties;

import static shiver.me.timbers.plugins.invoker.multi.MultiInvokerMojo.INVOCATION_ID;

/**
 * @author Karl Bennett
 */
@Component(role = InvocationRequestFactory.class, hint = "default")
class DefaultInvocationRequestFactory implements InvocationRequestFactory {

    @Override
    public InvocationRequest create(MavenProject project, MavenSession session, MultiInvokerConfiguration configuration) {
        final InvocationRequest request = new DefaultInvocationRequest();
        request.setProfiles(configuration.getProfiles());
        request.setBaseDirectory(project.getBasedir());
        request.setPomFile(project.getFile());
        request.setGoals(session.getGoals());
        request.setProperties(
            combineProperties(session.getSystemProperties(), session.getUserProperties(), configuration)
        );
        return request;
    }

    private static Properties combineProperties(
        Properties systemProperties,
        Properties userProperties,
        MultiInvokerConfiguration configuration
    ) {
        final Properties properties = new Properties();
        addProperties(properties, systemProperties);
        addProperties(properties, userProperties);
        properties.setProperty(INVOCATION_ID, configuration.getInvocationId());
        return properties;
    }

    private static void addProperties(Properties origin, Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            origin.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
    }
}
