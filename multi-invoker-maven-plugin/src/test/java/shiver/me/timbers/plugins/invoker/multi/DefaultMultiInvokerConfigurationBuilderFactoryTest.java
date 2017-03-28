package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.matchers.Matchers.hasField;
import static shiver.me.timbers.matchers.Matchers.hasFieldThat;

public class DefaultMultiInvokerConfigurationBuilderFactoryTest {

    @Test
    public void Instantiation_for_coverage() {
        new DefaultMultiInvokerConfigurationBuilderFactory();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_an_invocation_configuration_builder() {

        final LogFactory logFactory = mock(LogFactory.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final Log log = mock(Log.class);
        final String invocationId = someString();
        final Boolean forEachProfile = someBoolean();
        final List<String> invocations = asList(someString(), someString(), someString());
        final List<String> profiles = asList(someString(), someString(), someString());
        final List<String> goals = asList(someString(), someString(), someString());
        final Properties properties = new Properties();

        // Given
        given(configuration.getLog()).willReturn(log);
        given(configuration.getInvocationId()).willReturn(invocationId);
        given(configuration.isForEachProfile()).willReturn(forEachProfile);
        given(configuration.getInvocations()).willReturn(invocations);
        given(configuration.getProfiles()).willReturn(profiles);
        given(configuration.getGoals()).willReturn(goals);
        given(configuration.getProperties()).willReturn(properties);
        properties.setProperty(someAlphanumericString(3), someAlphanumericString(5));
        properties.setProperty(someAlphanumericString(8), someAlphanumericString(13));
        properties.setProperty(someAlphanumericString(21), someAlphanumericString(34));

        // When
        final MultiInvokerConfigurationBuilder actual = new DefaultMultiInvokerConfigurationBuilderFactory(logFactory)
            .createWith(configuration);

        // Then
        assertThat(actual, hasField("logFactory", logFactory));
        assertThat(actual, hasField("log", log));
        assertThat(actual, hasField("invocationId", invocationId));
        assertThat(actual, hasField("forEachProfile", forEachProfile));
        assertThat(actual, hasField("invocations", invocations));
        assertThat(actual, hasFieldThat("profiles", allOf(not(sameInstance(profiles)), equalTo(profiles))));
        assertThat(actual, hasFieldThat("goals", allOf(not(sameInstance(goals)), equalTo(goals))));
        assertThat(actual, hasFieldThat("properties", allOf(not(sameInstance(properties)), equalTo(properties))));
    }
}