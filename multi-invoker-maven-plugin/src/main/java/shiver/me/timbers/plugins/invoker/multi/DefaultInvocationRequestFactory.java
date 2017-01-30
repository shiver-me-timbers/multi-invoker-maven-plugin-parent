package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.Properties;

import static shiver.me.timbers.plugins.invoker.multi.MultiInvokerMojo.INVOCATION_ID;

/**
 * @author Karl Bennett
 */
@Component(role = InvocationRequestFactory.class, hint = "default")
class DefaultInvocationRequestFactory implements InvocationRequestFactory {

    @Requirement
    private OutputHandlerFactory outputHandlerFactory;

    @Requirement
    private PropertiesAppender propertiesAppender;

    DefaultInvocationRequestFactory() {
    }

    DefaultInvocationRequestFactory(
        OutputHandlerFactory outputHandlerFactory,
        PropertiesAppender propertiesAppender
    ) {
        this.outputHandlerFactory = outputHandlerFactory;
        this.propertiesAppender = propertiesAppender;
    }

    @Override
    public InvocationRequest create(MavenProject project, MavenSession session, MultiInvokerConfiguration configuration) {
        final InvocationRequest request = new DefaultInvocationRequest();
        request.setOutputHandler(outputHandlerFactory.createFrom(configuration.getLog()));
        request.setProfiles(configuration.getProfiles());
        request.setBaseDirectory(project.getBasedir());
        request.setPomFile(project.getFile());
        request.setGoals(session.getGoals());
        request.setProperties(configureProperties(session, configuration));
        return request;
    }

    private Properties configureProperties(MavenSession session, MultiInvokerConfiguration configuration) {
        final Properties properties = propertiesAppender.append(
            session.getSystemProperties(),
            session.getUserProperties()
        );
        properties.setProperty(INVOCATION_ID, configuration.getInvocationId());
        return properties;
    }
}
