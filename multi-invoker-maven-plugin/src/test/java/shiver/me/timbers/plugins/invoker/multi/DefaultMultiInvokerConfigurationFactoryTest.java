package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.model.Profile;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomEnums.someEnum;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DefaultMultiInvokerConfigurationFactoryTest {

    private MultiInvokerConfigurationBuilderFactory configurationBuilderFactory;
    private DefaultMultiInvokerConfigurationFactory factory;


    @Before
    public void setUp() {
        configurationBuilderFactory = mock(MultiInvokerConfigurationBuilderFactory.class);
        factory = new DefaultMultiInvokerConfigurationFactory(configurationBuilderFactory);
    }

    @Test
    public void Instantiation_for_coverage() {
        new DefaultMultiInvokerConfigurationFactory();
    }

    @Test
    public void Can_copy_an_existing_configuration() {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final Log log = mock(Log.class);
        final Boolean forEachProfile = someBoolean();
        final List<String> items = asList(someString(), someString(), someString());
        final List<String> profiles = asList(someString(), someString(), someString());
        final List<String> goals = asList(someString(), someString(), someString());
        final Properties properties = new Properties();

        // Given
        given(configuration.getLog()).willReturn(log);
        given(configuration.isForEachProfile()).willReturn(forEachProfile);
        given(configuration.getInvocations()).willReturn(items);
        given(configuration.getProfiles()).willReturn(profiles);
        given(configuration.getGoals()).willReturn(goals);
        given(configuration.getProperties()).willReturn(properties);
        properties.setProperty(someAlphanumericString(3), someAlphanumericString(5));
        properties.setProperty(someAlphanumericString(8), someAlphanumericString(13));
        properties.setProperty(someAlphanumericString(21), someAlphanumericString(34));

        // When
        final MultiInvokerConfiguration actual = factory.copy(configuration);

        // Then
        assertThat(actual.getLog(), is(log));
        assertThat(actual.isForEachProfile(), is(forEachProfile));
        assertThat(actual.getInvocations(), allOf(not(sameInstance(items)), equalTo(items)));
        assertThat(actual.getProfiles(), allOf(not(sameInstance(profiles)), equalTo(profiles)));
        assertThat(actual.getGoals(), allOf(not(sameInstance(goals)), equalTo(goals)));
        assertThat(actual.getProperties(), allOf(not(sameInstance(properties)), equalTo(properties)));
    }

    @Test
    public void Can_create_an_invocation_configuration_for_each_profile_defined_in_the_current_pom_file() {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final LogLevel logLevel = someEnum(LogLevel.class);

        final MultiInvokerConfigurationBuilder configurationBuilder = mock(MultiInvokerConfigurationBuilder.class);
        final MultiInvokerConfigurationBuilder configurationBuilderLogLevel =
            mock(MultiInvokerConfigurationBuilder.class);

        final MultiInvokerConfiguration expected = mock(MultiInvokerConfiguration.class);

        // Given
        given(configurationBuilderFactory.createWith(configuration)).willReturn(configurationBuilder);
        given(configurationBuilder.withLogLevel(logLevel)).willReturn(configurationBuilderLogLevel);
        given(configurationBuilderLogLevel.build()).willReturn(expected);

        // When
        final MultiInvokerConfiguration actual = factory.forLogLevel(configuration, logLevel);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_create_an_invocation_configuration_for_a_supplied_profile() {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final Profile profile = mock(Profile.class);

        final String id = someString();
        final MultiInvokerConfigurationBuilder configurationBuilder = mock(MultiInvokerConfigurationBuilder.class);
        final MultiInvokerConfigurationBuilder configurationBuilderInvocationId =
            mock(MultiInvokerConfigurationBuilder.class);
        final MultiInvokerConfigurationBuilder configurationBuilderProfile =
            mock(MultiInvokerConfigurationBuilder.class);

        final MultiInvokerConfiguration expected = mock(MultiInvokerConfiguration.class);

        // Given
        given(configurationBuilderFactory.createWith(configuration)).willReturn(configurationBuilder);
        given(profile.getId()).willReturn(id);
        given(configurationBuilder.withInvocationId(id)).willReturn(configurationBuilderInvocationId);
        given(configurationBuilderInvocationId.withProfile(id)).willReturn(configurationBuilderProfile);
        given(configurationBuilderProfile.build()).willReturn(expected);

        // When
        final MultiInvokerConfiguration actual = factory.forProfile(configuration, profile);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_create_an_invocation_configuration_for_an_invocation_id() {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final String invocation = someString();

        final MultiInvokerConfigurationBuilder configurationBuilder = mock(MultiInvokerConfigurationBuilder.class);
        final MultiInvokerConfigurationBuilder configurationBuilderInvocationId =
            mock(MultiInvokerConfigurationBuilder.class);

        final MultiInvokerConfiguration expected = mock(MultiInvokerConfiguration.class);

        // Given
        given(configurationBuilderFactory.createWith(configuration)).willReturn(configurationBuilder);
        given(configurationBuilder.withInvocationId(invocation)).willReturn(configurationBuilderInvocationId);
        given(configurationBuilderInvocationId.build()).willReturn(expected);

        // When
        final MultiInvokerConfiguration actual = factory.forInvocation(configuration, invocation);

        // Then
        assertThat(actual, is(expected));
    }
}