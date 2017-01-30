package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.plugins.invoker.multi.MultiInvokerMojo.INVOCATION_ID;

public class DefaultInvocationRequestFactoryTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_an_invocation_request() {

        final MavenProject project = mock(MavenProject.class);
        final MavenSession session = mock(MavenSession.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final List<String> profiles = mock(List.class);
        final File baseDir = mock(File.class);
        final File pomFile = mock(File.class);
        final List<String> goals = mock(List.class);
        final Properties systemProperties = new Properties();
        final Properties userProperties = new Properties();
        final String propertyKey1 = someString();
        final String propertyKey2 = someString();
        final String propertyKey3 = someString();
        final String propertyValue1 = someString();
        final String propertyValue2 = someString();
        final String propertyValue3 = someString();
        final String invocationId = someString();

        // Given
        given(configuration.getProfiles()).willReturn(profiles);
        given(project.getBasedir()).willReturn(baseDir);
        given(project.getFile()).willReturn(pomFile);
        given(session.getGoals()).willReturn(goals);
        given(session.getSystemProperties()).willReturn(systemProperties);
        systemProperties.setProperty(propertyKey1, propertyValue1);
        given(session.getUserProperties()).willReturn(userProperties);
        userProperties.setProperty(propertyKey2, propertyValue2);
        userProperties.setProperty(propertyKey3, propertyValue3);
        given(configuration.getInvocationId()).willReturn(invocationId);

        // When
        final InvocationRequest actual = new DefaultInvocationRequestFactory().create(project, session, configuration);

        // Then
        assertThat(actual.getProfiles(), equalTo(profiles));
        assertThat(actual.getBaseDirectory(), equalTo(baseDir));
        assertThat(actual.getPomFile(), equalTo(pomFile));
        assertThat(actual.getGoals(), equalTo(goals));
        assertThat(actual.getProperties(), containsProperties(systemProperties));
        assertThat(actual.getProperties(), containsProperties(userProperties));
        assertThat(actual.getProperties(), containsProperties(userProperties));
        assertThat(actual.getProperties(), containsProperty(INVOCATION_ID, invocationId));
    }

    private static Matcher<Properties> containsProperties(final Properties expected) {

        return new TypeSafeMatcher<Properties>() {

            @Override
            protected boolean matchesSafely(Properties actual) {
                for (Map.Entry<Object, Object> entry : expected.entrySet()) {
                    if (!containsProperty(entry.getKey(), entry.getValue()).matches(actual)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(expected);
            }
        };
    }

    private static Matcher<Properties> containsProperty(final Object key, final Object value) {

        return new TypeSafeMatcher<Properties>() {

            @Override
            protected boolean matchesSafely(Properties actual) {
                final Object actualValue = actual.get(key);
                return actualValue != null && actualValue.toString().equals(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("key: ").appendValue(key).appendText(" value: ").appendValue(value);
            }
        };
    }
}