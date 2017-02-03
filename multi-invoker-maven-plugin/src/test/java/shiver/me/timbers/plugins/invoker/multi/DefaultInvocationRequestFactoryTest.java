package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.plugins.invoker.multi.MultiInvokerMojo.INVOCATION_ID;

public class DefaultInvocationRequestFactoryTest {

    private OutputHandlerFactory outputHandlerFactory;
    private PropertiesAppender propertiesAppender;
    private DefaultInvocationRequestFactory factory;
    private MultiInvokerConfigurationReplacer configurationReplacer;

    @Before
    public void setUp() {
        outputHandlerFactory = mock(OutputHandlerFactory.class);
        propertiesAppender = mock(PropertiesAppender.class);
        configurationReplacer = mock(MultiInvokerConfigurationReplacer.class);
        factory = new DefaultInvocationRequestFactory(configurationReplacer, outputHandlerFactory, propertiesAppender);
    }

    @Test
    public void Instantiation_for_coverage() {
        new DefaultInvocationRequestFactory();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_an_invocation_request() {

        final MavenProject project = mock(MavenProject.class);
        final MavenSession session = mock(MavenSession.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final MultiInvokerConfiguration replacedConfiguration = mock(MultiInvokerConfiguration.class);
        final Log log = mock(Log.class);
        final List<String> profiles = mock(List.class);
        final InvocationOutputHandler outputHandler = mock(InvocationOutputHandler.class);
        final File baseDir = mock(File.class);
        final File pomFile = mock(File.class);
        final List<String> goals = mock(List.class);
        final Properties systemProperties = new Properties();
        final Properties userProperties = new Properties();
        final Properties properties = mock(Properties.class);
        final String invocationId = someString();

        // Given
        given(configurationReplacer.resolveSubstitutions(configuration)).willReturn(replacedConfiguration);
        given(replacedConfiguration.getLog()).willReturn(log);
        given(replacedConfiguration.getProfiles()).willReturn(profiles);
        given(outputHandlerFactory.createFrom(log)).willReturn(outputHandler);
        given(project.getBasedir()).willReturn(baseDir);
        given(project.getFile()).willReturn(pomFile);
        given(session.getGoals()).willReturn(goals);
        given(session.getSystemProperties()).willReturn(systemProperties);
        given(session.getUserProperties()).willReturn(userProperties);
        given(propertiesAppender.append(systemProperties, userProperties)).willReturn(properties);
        given(replacedConfiguration.getInvocationId()).willReturn(invocationId);

        // When
        final InvocationRequest actual = factory.create(project, session, configuration);

        // Then
        verify(properties).setProperty(INVOCATION_ID, invocationId);
        assertThat(actual.getOutputHandler(null), is(outputHandler));
        assertThat(actual.getProfiles(), is(profiles));
        assertThat(actual.getBaseDirectory(), is(baseDir));
        assertThat(actual.getPomFile(), is(pomFile));
        assertThat(actual.getGoals(), is(goals));
        assertThat(actual.getProperties(), is(properties));
    }
}