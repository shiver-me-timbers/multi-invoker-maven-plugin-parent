package shiver.me.timbers.plugins.invoker.multi;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.matchers.Matchers.hasField;

public class ConfigurationMultiInvokerConfigurationBuilderTest {

    private MultiInvokerConfiguration configuration;

    @Before
    public void setUp() {
        configuration = mock(MultiInvokerConfiguration.class);
    }

    @Test
    public void Can_add_an_invocation_id_to_the_builder() {

        // Given
        final String invocationId = someString();

        // When
        final ConfigurationMultiInvokerConfigurationBuilder builder =
            new ConfigurationMultiInvokerConfigurationBuilder(configuration);
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
        updatedProfiles.add(profile);

        // When
        final ConfigurationMultiInvokerConfigurationBuilder builder =
            new ConfigurationMultiInvokerConfigurationBuilder(configuration);
        builder.withProfile(profile);

        // Then
        assertThat(profiles, equalTo(oldProfiles));
        assertThat(builder, hasField("profiles", updatedProfiles));
    }

    @Test
    public void Can_build_a_configuration() {

        final String invocationId = someString();
        final Boolean forEachProfile = someBoolean();
        final List<String> profiles = asList(someString(), someString(), someString());

        // Given
        given(configuration.getInvocationId()).willReturn(invocationId);
        given(configuration.isForEachProfile()).willReturn(forEachProfile);
        given(configuration.getProfiles()).willReturn(profiles);

        // When
        final MultiInvokerConfiguration actual = new ConfigurationMultiInvokerConfigurationBuilder(configuration)
            .build();

        // Then
        assertThat(actual.getInvocationId(), is(invocationId));
        assertThat(actual.isForEachProfile(), is(forEachProfile));
        assertThat(actual.getProfiles(), is(profiles));
    }
}