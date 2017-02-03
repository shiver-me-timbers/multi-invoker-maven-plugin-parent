package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DefaultMultiInvokerConfigurationReplacerTest {

    @Test
    public void Instantiation_for_coverage() {
        new DefaultMultiInvokerConfigurationReplacer();
    }

    @Test
    public void Can_resolve_the_invocation_id_in_the_profiles_list() {

        final MultiInvokerConfigurationFactory configurationFactory = mock(MultiInvokerConfigurationFactory.class);
        final StringReplacer stringReplacer = mock(StringReplacer.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final MultiInvokerConfiguration configurationCopy = mock(MultiInvokerConfiguration.class);

        final Log log = mock(Log.class);
        final String invocationid = someString();
        @SuppressWarnings("unchecked")
        final List<String> items = mock(List.class);
        final String profile1 = someString();
        final String profile2 = someString();
        final String profile3 = someString();
        final String replacedProfile1 = someString();
        final String replacedProfile2 = someString();
        final String replacedProfile3 = someString();

        // Given
        given(configurationFactory.copy(configuration)).willReturn(configurationCopy);
        given(configurationCopy.getLog()).willReturn(log);
        given(configurationCopy.getInvocationId()).willReturn(invocationid);
        given(configurationCopy.getInvocations()).willReturn(items);
        given(configurationCopy.getProfiles()).willReturn(asList(profile1, profile2, profile3));
        given(stringReplacer.replace(profile1, "@invocation.id@", invocationid)).willReturn(replacedProfile1);
        given(stringReplacer.replace(profile2, "@invocation.id@", invocationid)).willReturn(replacedProfile2);
        given(stringReplacer.replace(profile3, "@invocation.id@", invocationid)).willReturn(replacedProfile3);

        // When
        final MultiInvokerConfiguration actual = new DefaultMultiInvokerConfigurationReplacer(
            configurationFactory,
            stringReplacer
        ).resolveSubstitutions(configuration);

        // Then
        assertThat(actual.getLog(), is(log));
        assertThat(actual.getInvocationId(), is(invocationid));
        assertThat(actual.getInvocations(), is(items));
        assertThat(actual.getProfiles(), contains(replacedProfile1, replacedProfile2, replacedProfile3));
    }
}