package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
        final Properties configuredProperties = mock(Properties.class);
        final Properties systemProperties = mock(Properties.class);
        final Properties userProperties = mock(Properties.class);
        final Properties properties = mock(Properties.class);
        final String invocationId = someString();

        // Given
        given(configurationReplacer.resolveSubstitutions(configuration)).willReturn(replacedConfiguration);
        given(replacedConfiguration.getProject()).willReturn(project);
        given(replacedConfiguration.getSession()).willReturn(session);
        given(replacedConfiguration.getLog()).willReturn(log);
        given(replacedConfiguration.getProfiles()).willReturn(profiles);
        given(replacedConfiguration.getGoals()).willReturn(null);
        given(replacedConfiguration.getProperties()).willReturn(configuredProperties);
        given(outputHandlerFactory.createInfoFrom(log)).willReturn(outputHandler);
        given(project.getBasedir()).willReturn(baseDir);
        given(project.getFile()).willReturn(pomFile);
        given(session.getGoals()).willReturn(goals);
        given(session.getSystemProperties()).willReturn(systemProperties);
        given(session.getUserProperties()).willReturn(userProperties);
        given(propertiesAppender.append(configuredProperties, systemProperties, userProperties)).willReturn(properties);
        given(replacedConfiguration.getInvocationId()).willReturn(invocationId);

        // When
        final InvocationRequest actual = factory.create(configuration);

        // Then
        verify(properties).setProperty(INVOCATION_ID, invocationId);
        assertThat(actual.getOutputHandler(null), is(outputHandler));
        assertThat(actual.getProfiles(), is(profiles));
        assertThat(actual.getBaseDirectory(), is(baseDir));
        assertThat(actual.getPomFile(), is(pomFile));
        assertThat(actual.getGoals(), is(goals));
        assertThat(actual.getProperties(), is(properties));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_an_invocation_request_with_configured_goals() {

        final MavenProject project = mock(MavenProject.class);
        final MavenSession session = mock(MavenSession.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final MultiInvokerConfiguration replacedConfiguration = mock(MultiInvokerConfiguration.class);
        final Log log = mock(Log.class);
        final List<String> profiles = mock(List.class);
        final List<String> goals = mock(List.class);
        final InvocationOutputHandler infoOutputHandler = mock(InvocationOutputHandler.class);
        final InvocationOutputHandler errorOutputHandler = mock(InvocationOutputHandler.class);
        final File baseDir = mock(File.class);
        final File pomFile = mock(File.class);
        final Properties configuredProperties = mock(Properties.class);
        final Properties systemProperties = mock(Properties.class);
        final Properties userProperties = mock(Properties.class);
        final Properties properties = mock(Properties.class);
        final String invocationId = someString();

        // Given
        given(configurationReplacer.resolveSubstitutions(configuration)).willReturn(replacedConfiguration);
        given(replacedConfiguration.getProject()).willReturn(project);
        given(replacedConfiguration.getSession()).willReturn(session);
        given(replacedConfiguration.getLog()).willReturn(log);
        given(replacedConfiguration.getProfiles()).willReturn(profiles);
        given(replacedConfiguration.getGoals()).willReturn(goals);
        given(replacedConfiguration.getProperties()).willReturn(configuredProperties);
        given(outputHandlerFactory.createInfoFrom(log)).willReturn(infoOutputHandler);
        given(outputHandlerFactory.createErrorFrom(log)).willReturn(errorOutputHandler);
        given(project.getBasedir()).willReturn(baseDir);
        given(project.getFile()).willReturn(pomFile);
        given(session.getSystemProperties()).willReturn(systemProperties);
        given(session.getUserProperties()).willReturn(userProperties);
        given(propertiesAppender.append(configuredProperties, systemProperties, userProperties)).willReturn(properties);
        given(replacedConfiguration.getInvocationId()).willReturn(invocationId);

        // When
        final InvocationRequest actual = factory.create(configuration);

        // Then
        then(properties).should().setProperty(INVOCATION_ID, invocationId);
        then(session).should(never()).getGoals();
        assertThat(actual.getOutputHandler(null), is(infoOutputHandler));
        assertThat(actual.getErrorHandler(null), is(errorOutputHandler));
        assertThat(actual.getProfiles(), is(profiles));
        assertThat(actual.getBaseDirectory(), is(baseDir));
        assertThat(actual.getPomFile(), is(pomFile));
        assertThat(actual.getGoals(), is(goals));
        assertThat(actual.getProperties(), is(properties));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Empty_goals_are_ignored() {

        final MavenSession session = mock(MavenSession.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final MultiInvokerConfiguration replacedConfiguration = mock(MultiInvokerConfiguration.class);
        final List<String> goals = mock(List.class);
        final Properties configuredProperties = mock(Properties.class);
        final Properties systemProperties = mock(Properties.class);
        final Properties userProperties = mock(Properties.class);
        final Properties properties = mock(Properties.class);

        // Given
        given(configurationReplacer.resolveSubstitutions(configuration)).willReturn(replacedConfiguration);
        given(replacedConfiguration.getProject()).willReturn(mock(MavenProject.class));
        given(replacedConfiguration.getSession()).willReturn(session);
        given(replacedConfiguration.getGoals()).willReturn(Collections.<String>emptyList());
        given(replacedConfiguration.getProperties()).willReturn(configuredProperties);
        given(session.getGoals()).willReturn(goals);
        given(session.getSystemProperties()).willReturn(systemProperties);
        given(session.getUserProperties()).willReturn(userProperties);
        given(propertiesAppender.append(configuredProperties, systemProperties, userProperties)).willReturn(properties);

        // When
        final InvocationRequest actual = factory.create(configuration);

        // Then
        assertThat(actual.getGoals(), is(goals));
    }
}