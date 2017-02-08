package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.List;
import java.util.Properties;

import static shiver.me.timbers.plugins.invoker.multi.MultiInvokerMojo.INVOCATION_ID;

/**
 * @author Karl Bennett
 */
@Component(role = InvocationRequestFactory.class, hint = "default")
class DefaultInvocationRequestFactory implements InvocationRequestFactory {

    @Requirement
    private MultiInvokerConfigurationReplacer configurationReplacer;

    @Requirement
    private OutputHandlerFactory outputHandlerFactory;

    @Requirement
    private PropertiesAppender propertiesAppender;

    DefaultInvocationRequestFactory() {
    }

    DefaultInvocationRequestFactory(
        MultiInvokerConfigurationReplacer configurationReplacer,
        OutputHandlerFactory outputHandlerFactory,
        PropertiesAppender propertiesAppender
    ) {
        this.configurationReplacer = configurationReplacer;
        this.outputHandlerFactory = outputHandlerFactory;
        this.propertiesAppender = propertiesAppender;
    }

    @Override
    public InvocationRequest create(MavenProject project, MavenSession session, MultiInvokerConfiguration configuration) {
        final MultiInvokerConfiguration replacedConfiguration = configurationReplacer.resolveSubstitutions(configuration);
        final InvocationRequest request = new DefaultInvocationRequest();
        request.setOutputHandler(outputHandlerFactory.createFrom(replacedConfiguration.getLog()));
        request.setProfiles(replacedConfiguration.getProfiles());
        request.setBaseDirectory(project.getBasedir());
        request.setPomFile(project.getFile());
        request.setGoals(chooseGoals(replacedConfiguration, session));
        request.setProperties(configureProperties(session, replacedConfiguration));
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

    private static List<String> chooseGoals(MultiInvokerConfiguration configuration, MavenSession session) {
        final List<String> goals = configuration.getGoals();
        if (goals != null && !goals.isEmpty()) {
            return goals;
        }
        return session.getGoals();
    }
}
