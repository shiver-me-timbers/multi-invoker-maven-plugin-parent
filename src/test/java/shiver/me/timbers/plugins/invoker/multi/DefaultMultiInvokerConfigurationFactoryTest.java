package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.model.Profile;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
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

        final Boolean forEachProfile = someBoolean();
        final List<String> profiles = asList(someString(), someString(), someString());

        // Given
        given(configuration.isForEachProfile()).willReturn(forEachProfile);
        given(configuration.getProfiles()).willReturn(profiles);

        // When
        final MultiInvokerConfiguration actual = factory.copy(configuration);

        // Then
        assertThat(actual.isForEachProfile(), is(forEachProfile));
        assertThat(actual.getProfiles(), allOf(not(sameInstance(profiles)), equalTo(profiles)));
    }

    @Test
    public void Can_create_an_invocation_request_for_each_profile_defined_in_the_current_pom_file() {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final Profile profile = mock(Profile.class);

        final String id = someString();
        final MultiInvokerConfigurationBuilder configurationBuilder = mock(MultiInvokerConfigurationBuilder.class);
        final MultiInvokerConfigurationBuilder configurationBuilderInvocationId = mock(MultiInvokerConfigurationBuilder.class);
        final MultiInvokerConfigurationBuilder configurationBuilderProfile = mock(MultiInvokerConfigurationBuilder.class);

        final MultiInvokerConfiguration expected = mock(MultiInvokerConfiguration.class);

        // Given
        given(configurationBuilderFactory.createWith(configuration)).willReturn(configurationBuilder);
        given(configuration.isForEachProfile()).willReturn(true);
        given(profile.getId()).willReturn(id);
        given(configurationBuilder.withInvocationId(id)).willReturn(configurationBuilderInvocationId);
        given(configurationBuilderInvocationId.withProfile(id)).willReturn(configurationBuilderProfile);
        given(configurationBuilderProfile.build()).willReturn(expected);

        // When
        final MultiInvokerConfiguration actual = factory.forProfile(configuration, profile);

        // Then
        assertThat(actual, is(expected));
    }
}