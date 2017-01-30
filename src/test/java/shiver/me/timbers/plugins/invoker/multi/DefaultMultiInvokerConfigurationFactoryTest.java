package shiver.me.timbers.plugins.invoker.multi;

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
import static shiver.me.timbers.matchers.Matchers.hasField;

public class DefaultMultiInvokerConfigurationFactoryTest {

    private DefaultMultiInvokerConfigurationFactory factory;

    @Before
    public void setUp() {
        factory = new DefaultMultiInvokerConfigurationFactory();
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
    public void Can_create_a_builder_from_an_existing_configuration() {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final Boolean forEachProfile = someBoolean();

        // Given
        given(configuration.isForEachProfile()).willReturn(forEachProfile);

        // When
        final MultiInvokerConfigurationBuilder builder = factory.buildFrom(configuration);

        // Then
        assertThat(builder, hasField("forEachProfile", forEachProfile));
    }
}