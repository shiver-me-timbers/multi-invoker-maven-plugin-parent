package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomEnums.someEnum;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.matchers.Matchers.hasField;

public class ConfigurationMultiInvokerConfigurationBuilderTest {

    private MultiInvokerConfiguration configuration;
    private LogFactory logFactory;

    @Before
    public void setUp() {
        configuration = mock(MultiInvokerConfiguration.class);
        logFactory = mock(LogFactory.class);
    }

    @Test
    public void Can_set_the_invocations_log_level() {

        final LogLevel logLevel = someEnum(LogLevel.class);

        final Log log = mock(Log.class);

        // Given
        given(configuration.getProperties()).willReturn(new Properties());
        given(logFactory.create(logLevel)).willReturn(log);

        // When
        final ConfigurationMultiInvokerConfigurationBuilder builder =
            new ConfigurationMultiInvokerConfigurationBuilder(configuration, logFactory);
        builder.withLogLevel(logLevel);

        // Then
        assertThat(builder, hasField("log", log));
    }

    @Test
    public void Cannot_set_the_invocations_log_level_if_null_supplied() {

        final Log log = mock(Log.class);

        // Given
        given(configuration.getProperties()).willReturn(new Properties());
        given(configuration.getLog()).willReturn(log);

        // When
        final ConfigurationMultiInvokerConfigurationBuilder builder =
            new ConfigurationMultiInvokerConfigurationBuilder(configuration, logFactory);
        builder.withLogLevel(null);

        // Then
        assertThat(builder, hasField("log", log));
        verifyZeroInteractions(logFactory);
    }

    @Test
    public void Can_add_an_invocation_id_to_the_builder() {

        final String invocationId = someString();

        // Given
        given(configuration.getProperties()).willReturn(new Properties());

        // When
        final ConfigurationMultiInvokerConfigurationBuilder builder =
            new ConfigurationMultiInvokerConfigurationBuilder(configuration, logFactory);
        builder.withInvocationId(invocationId);

        // Then
        assertThat(builder, hasField("invocationId", invocationId));
    }

    @Test
    public void Can_add_a_profile_to_the_builder() {

        final String profile = someString();

        final List<String> profiles = asList(someString(), someString(), someString());
        final List<String> oldProfiles = new ArrayList<>(profiles);
        final List<String> updatedProfiles = new ArrayList<>(profiles);

        // Given
        given(configuration.getProfiles()).willReturn(profiles);
        given(configuration.getProperties()).willReturn(new Properties());
        updatedProfiles.add(profile);

        // When
        final ConfigurationMultiInvokerConfigurationBuilder builder =
            new ConfigurationMultiInvokerConfigurationBuilder(configuration, logFactory);
        builder.withProfile(profile);

        // Then
        assertThat(profiles, equalTo(oldProfiles));
        assertThat(builder, hasField("profiles", updatedProfiles));
    }

    @Test
    public void Can_build_a_configuration() {

        final MavenProject project = mock(MavenProject.class);
        final MavenSession session = mock(MavenSession.class);
        final Log log = mock(Log.class);
        final String invocationId = someString();
        final Boolean forEachProfile = someBoolean();
        final List<String> items = asList(someString(), someString(), someString());
        final List<String> profiles = asList(someString(), someString(), someString());
        final List<String> goals = asList(someString(), someString(), someString());
        final Properties properties = new Properties();

        // Given
        given(configuration.getProject()).willReturn(project);
        given(configuration.getSession()).willReturn(session);
        given(configuration.getLog()).willReturn(log);
        given(configuration.getInvocationId()).willReturn(invocationId);
        given(configuration.getInvocations()).willReturn(items);
        given(configuration.isForEachProfile()).willReturn(forEachProfile);
        given(configuration.getProfiles()).willReturn(profiles);
        given(configuration.getGoals()).willReturn(goals);
        given(configuration.getProperties()).willReturn(properties);
        properties.setProperty(someAlphanumericString(3), someAlphanumericString(5));
        properties.setProperty(someAlphanumericString(8), someAlphanumericString(13));
        properties.setProperty(someAlphanumericString(21), someAlphanumericString(34));

        // When
        final MultiInvokerConfiguration actual = new ConfigurationMultiInvokerConfigurationBuilder(configuration, logFactory)
            .build();

        // Then
        assertThat(actual.getProject(), is(project));
        assertThat(actual.getSession(), is(session));
        assertThat(actual.getLog(), is(log));
        assertThat(actual.getInvocationId(), is(invocationId));
        assertThat(actual.isForEachProfile(), is(forEachProfile));
        assertThat(actual.getInvocations(), equalTo(items));
        assertThat(actual.getProfiles(), equalTo(profiles));
        assertThat(actual.getGoals(), equalTo(goals));
        assertThat(actual.getProperties(), equalTo(properties));
    }
}