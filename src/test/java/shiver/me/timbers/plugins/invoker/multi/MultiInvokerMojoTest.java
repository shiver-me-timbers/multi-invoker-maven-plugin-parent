package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomIntegers.someIntegerGreaterThan;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class MultiInvokerMojoTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private MavenProject project;
    private MavenSession session;
    private MultiInvokerConfigurationFactory configurationFactory;
    private InvocationRequestsFactory requestsFactory;
    private Invoker invoker;
    private MultiInvokerMojo mojo;

    @Before
    public void setUp() {
        project = mock(MavenProject.class);
        session = mock(MavenSession.class);
        configurationFactory = mock(MultiInvokerConfigurationFactory.class);
        requestsFactory = mock(InvocationRequestsFactory.class);
        invoker = mock(Invoker.class);
        mojo = new MultiInvokerMojo(project, session, configurationFactory, requestsFactory, invoker);
    }

    @Test
    public void Instantiation_for_coverage() {
        new MultiInvokerMojo();
    }

    @Test
    public void Can_invoke_the_current_project_for_multiple_times()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request1 = mock(InvocationRequest.class);
        final InvocationRequest request2 = mock(InvocationRequest.class);
        final InvocationRequest request3 = mock(InvocationRequest.class);
        final InvocationResult success = mock(InvocationResult.class);

        // Given
        given(configurationFactory.copy(mojo)).willReturn(configuration);
        given(requestsFactory.create(project, session, configuration)).willReturn(asList(request1, request2, request3));
        given(invoker.execute(request1)).willReturn(success);
        given(invoker.execute(request2)).willReturn(success);
        given(invoker.execute(request3)).willReturn(success);
        given(success.getExitCode()).willReturn(0);

        // When
        mojo.execute();

        // Then
        then(success).should(times(3)).getExitCode();
    }

    @Test
    public void Will_fail_correctly_if_the_invocations_are_incorrectly_configured()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request = mock(InvocationRequest.class);
        final MavenInvocationException exception = new MavenInvocationException(someString());

        // Given
        given(configurationFactory.copy(mojo)).willReturn(configuration);
        given(requestsFactory.create(project, session, configuration)).willReturn(singletonList(request));
        given(invoker.execute(request)).willThrow(exception);
        expectedException.expect(MojoExecutionException.class);
        expectedException.expectMessage("Multi invocation is invalid.");
        expectedException.expectCause(is(exception));

        // When
        mojo.execute();
    }

    @Test
    public void Can_fail_one_of_the_multiple_invocations()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request1 = mock(InvocationRequest.class);
        final InvocationRequest request2 = mock(InvocationRequest.class);
        final InvocationRequest request3 = mock(InvocationRequest.class);
        final InvocationResult success = mock(InvocationResult.class);
        final InvocationResult failure = mock(InvocationResult.class);
        final CommandLineException exception = mock(CommandLineException.class);

        // Given
        given(configurationFactory.copy(mojo)).willReturn(configuration);
        given(requestsFactory.create(project, session, configuration)).willReturn(asList(request1, request2, request3));
        given(invoker.execute(request1)).willReturn(success);
        given(invoker.execute(request2)).willReturn(failure);
        given(success.getExitCode()).willReturn(0);
        given(failure.getExitCode()).willReturn(someIntegerGreaterThan(0));
        given(failure.getExecutionException()).willReturn(exception);
        expectedException.expect(MojoExecutionException.class);
        expectedException.expectMessage("Multi invocation has failed.");
        expectedException.expectCause(is(exception));

        // When
        mojo.execute();
    }

    @Test
    public void Can_get_the_invocation_id()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        // When
        final String actual = mojo.getInvocationId();

        // Then
        assertThat(actual, is(mojo.getClass().getSimpleName()));
    }

    @Test
    public void Can_set_the_invocations_to_run_for_each_profile_defined_in_the_current_pom()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final Boolean expected = someBoolean();

        // Given
        mojo.forEachProfile(expected);

        // When
        final boolean actual = mojo.isForEachProfile();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_get_configured_profiles()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        // When
        final List<String> actual = mojo.getProfiles();

        // Then
        assertThat(actual, is(empty()));
    }
}