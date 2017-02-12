package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Properties;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DefaultMavenStringsTest {

    private DefaultMavenStrings mavenStrings;

    @Before
    public void setUp() {
        mavenStrings = new DefaultMavenStrings();
    }

    @Test
    public void Can_convert_a_configuration_into_a_maven_artifact_id_string() {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final MavenProject project = mock(MavenProject.class);

        final Artifact artifact = mock(Artifact.class);

        final String expected = someString();

        // Given
        given(configuration.getProject()).willReturn(project);
        given(project.getArtifact()).willReturn(artifact);
        given(artifact.toString()).willReturn(expected);

        // When
        final String actual = mavenStrings.toArtifactId(configuration);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_convert_a_request_into_a_maven_goal_string() {

        final InvocationRequest request = mock(InvocationRequest.class);

        final String goal1 = someString();
        final String goal2 = someString();
        final String goal3 = someString();

        // Given
        given(request.getGoals()).willReturn(asList(goal1, goal2, goal3));

        // When
        final String actual = mavenStrings.toGoals(request);

        // Then
        assertThat(actual, equalTo(format("%s %s %s", goal1, goal2, goal3)));
    }

    @Test
    public void Can_handle_empty_goals() {

        final InvocationRequest request = mock(InvocationRequest.class);

        // Given
        given(request.getGoals()).willReturn(Collections.<String>emptyList());

        // When
        final String actual = mavenStrings.toGoals(request);

        // Then
        assertThat(actual, is(emptyString()));
    }

    @Test
    public void Can_convert_a_request_into_a_maven_profile_string() {

        final InvocationRequest request = mock(InvocationRequest.class);

        final String profile1 = someString();
        final String profile2 = someString();
        final String profile3 = someString();

        // Given
        given(request.getProfiles()).willReturn(asList(profile1, profile2, profile3));

        // When
        final String actual = mavenStrings.toProfiles(request);

        // Then
        assertThat(actual, equalTo(format(" -P%s,%s,%s", profile1, profile2, profile3)));
    }

    @Test
    public void Can_handle_empty_profiles() {

        final InvocationRequest request = mock(InvocationRequest.class);

        // Given
        given(request.getProfiles()).willReturn(Collections.<String>emptyList());

        // When
        final String actual = mavenStrings.toProfiles(request);

        // Then
        assertThat(actual, is(emptyString()));
    }

    @Test
    public void Can_convert_a_request_into_a_maven_properties_string() {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request = mock(InvocationRequest.class);

        final MavenSession session = mock(MavenSession.class);
        final Properties systemProperties = new Properties();
        final Properties userProperties = new Properties();
        final Properties expectedProperties = new Properties();
        final Properties requestProperties = new Properties();
        final String systemKey1 = someAlphanumericString(5);
        final String systemKey2 = someAlphanumericString(5);
        final String systemKey3 = someAlphanumericString(5);
        final String systemValue1 = someAlphanumericString(8);
        final String systemValue2 = someAlphanumericString(8);
        final String systemValue3 = someAlphanumericString(8);
        final String userKey1 = someAlphanumericString(8);
        final String userValue1 = someAlphanumericString(13);
        final String key1 = someAlphanumericString(13);
        final String key2 = someAlphanumericString(13);
        final String key3 = someAlphanumericString(13);
        final String value1 = someAlphanumericString(21);
        final String value2 = someAlphanumericString(21);
        final String value3 = someAlphanumericString(21);

        // Given
        given(configuration.getSession()).willReturn(session);
        given(session.getSystemProperties()).willReturn(systemProperties);
        given(session.getUserProperties()).willReturn(userProperties);
        given(request.getProperties()).willReturn(requestProperties);
        systemProperties.setProperty(systemKey1, systemValue1);
        systemProperties.setProperty(systemKey2, systemValue2);
        systemProperties.setProperty(systemKey3, systemValue3);
        systemProperties.setProperty(userKey1, userValue1);
        userProperties.setProperty(userKey1, userValue1);
        expectedProperties.setProperty(key1, value1);
        expectedProperties.setProperty(key2, value2);
        expectedProperties.setProperty(key3, value3);
        requestProperties.putAll(systemProperties);
        requestProperties.putAll(expectedProperties);

        // When
        final String actual = mavenStrings.toProperties(configuration, request);

        // Then
        assertThat(actual, startsWith(" "));
        assertThat(actual, allOf(
            containsString(format("-D%s=%s", key1, value1)),
            containsString(format("-D%s=%s", key2, value2)),
            containsString(format("-D%s=%s", key3, value3)),
            containsString(format("-D%s=%s", userKey1, userValue1))
        ));
        assertThat(actual, allOf(
            not(containsString(format("-D%s=%s", systemKey1, systemValue1))),
            not(containsString(format("-D%s=%s", systemKey2, systemValue2))),
            not(containsString(format("-D%s=%s", systemKey3, systemValue3)))
        ));
    }

    @Test
    public void Can_handle_empty_properties() {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request = mock(InvocationRequest.class);

        final MavenSession session = mock(MavenSession.class);
        final Properties systemProperties = new Properties();
        final Properties userProperties = new Properties();
        final Properties requestProperties = new Properties();

        // Given
        given(configuration.getSession()).willReturn(session);
        given(session.getSystemProperties()).willReturn(systemProperties);
        given(session.getUserProperties()).willReturn(userProperties);
        given(request.getProperties()).willReturn(requestProperties);

        // When
        final String actual = mavenStrings.toProperties(configuration, request);

        // Then
        assertThat(actual, is(emptyString()));
    }
}