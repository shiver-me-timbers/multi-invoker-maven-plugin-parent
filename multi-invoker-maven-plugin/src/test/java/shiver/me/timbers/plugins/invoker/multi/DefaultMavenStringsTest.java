package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.shared.invoker.InvocationRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DefaultMavenStringsTest {

    private DefaultMavenStrings mvns;

    @Before
    public void setUp() {
        mvns = new DefaultMavenStrings();
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
        final String actual = mvns.toGoals(request);

        // Then
        assertThat(actual, equalTo(format("%s %s %s", goal1, goal2, goal3)));
    }

    @Test
    public void Can_handle_empty_goals() {

        final InvocationRequest request = mock(InvocationRequest.class);

        // Given
        given(request.getGoals()).willReturn(Collections.<String>emptyList());

        // When
        final String actual = mvns.toGoals(request);

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
        final String actual = mvns.toProfiles(request);

        // Then
        assertThat(actual, equalTo(format("-P%s,%s,%s", profile1, profile2, profile3)));
    }

    @Test
    public void Can_handle_empty_profiles() {

        final InvocationRequest request = mock(InvocationRequest.class);

        // Given
        given(request.getProfiles()).willReturn(Collections.<String>emptyList());

        // When
        final String actual = mvns.toProfiles(request);

        // Then
        assertThat(actual, is(emptyString()));
    }
}