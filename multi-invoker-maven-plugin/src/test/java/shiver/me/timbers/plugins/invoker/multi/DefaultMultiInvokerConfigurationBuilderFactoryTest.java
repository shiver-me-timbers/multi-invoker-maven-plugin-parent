package shiver.me.timbers.plugins.invoker.multi;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.matchers.Matchers.hasField;
import static shiver.me.timbers.matchers.Matchers.hasFieldThat;

public class DefaultMultiInvokerConfigurationBuilderFactoryTest {

    @Test
    public void Can_create_an_invocation_configuration_builder() {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final String invocationId = someString();
        final Boolean forEachProfile = someBoolean();
        final List<String> profiles = asList(someString(), someString(), someString());

        // Given
        given(configuration.getInvocationId()).willReturn(invocationId);
        given(configuration.isForEachProfile()).willReturn(forEachProfile);
        given(configuration.getProfiles()).willReturn(profiles);

        // When
        final MultiInvokerConfigurationBuilder actual = new DefaultMultiInvokerConfigurationBuilderFactory()
            .createWith(configuration);

        // Then
        assertThat(actual, hasField("invocationId", invocationId));
        assertThat(actual, hasField("forEachProfile", forEachProfile));
        assertThat(actual, hasFieldThat("profiles", allOf(not(sameInstance(profiles)), equalTo(profiles))));
    }
}