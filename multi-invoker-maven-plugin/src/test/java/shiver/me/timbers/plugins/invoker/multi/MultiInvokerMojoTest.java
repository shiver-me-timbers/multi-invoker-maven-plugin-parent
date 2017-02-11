package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
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
import java.util.Properties;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomEnums.someEnum;
import static shiver.me.timbers.data.random.RandomIntegers.someIntegerGreaterThan;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.plugins.invoker.multi.MultiInvokerMojo.INVOCATION_ID;

public class MultiInvokerMojoTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Log log;
    private LogLevel logLevel;
    private MavenProject project;
    private MavenSession session;
    private MultiInvokerConfigurationFactory configurationFactory;
    private InvocationRequestsFactory requestsFactory;
    private MavenStrings mavenStrings;
    private Invoker invoker;
    private MultiInvokerMojo mojo;

    @Before
    public void setUp() {
        log = mock(Log.class);
        logLevel = someEnum(LogLevel.class);
        project = mock(MavenProject.class);
        session = mock(MavenSession.class);
        configurationFactory = mock(MultiInvokerConfigurationFactory.class);
        requestsFactory = mock(InvocationRequestsFactory.class);
        mavenStrings = mock(MavenStrings.class);
        invoker = mock(Invoker.class);
        mojo = new MultiInvokerMojo(
            logLevel,
            project,
            session,
            configurationFactory,
            requestsFactory,
            mavenStrings,
            invoker
        );
        mojo.setLog(log);
    }

    @Test
    public void Instantiation_for_coverage() {
        new MultiInvokerMojo();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Will_log_the_invocation()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request = mock(InvocationRequest.class);
        final InvocationResult success = mock(InvocationResult.class);
        final String goals = someString();
        final String profiles = someString();

        // Given
        given(configurationFactory.forLogLevel(mojo, logLevel)).willReturn(configuration);
        given(requestsFactory.create(configuration)).willReturn(singletonList(request));
        given(mavenStrings.toGoals(request)).willReturn(goals);
        given(mavenStrings.toProfiles(request)).willReturn(profiles);
        given(invoker.execute(request)).willReturn(success);
        given(success.getExitCode()).willReturn(0);

        // When
        mojo.execute();

        // Then
        then(log).should().info(format("Invoking: mvn %s %s", goals, profiles));
    }

    @Test
    public void Can_invoke_the_current_project_multiple_times()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request1 = mock(InvocationRequest.class);
        final InvocationRequest request2 = mock(InvocationRequest.class);
        final InvocationRequest request3 = mock(InvocationRequest.class);
        final InvocationResult success = mock(InvocationResult.class);

        // Given
        given(configurationFactory.forLogLevel(mojo, logLevel)).willReturn(configuration);
        given(requestsFactory.create(configuration)).willReturn(asList(request1, request2, request3));
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
    public void Can_invoke_the_current_if_invocation_id_is_empty()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final Properties properties = mock(Properties.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request = mock(InvocationRequest.class);
        final InvocationResult success = mock(InvocationResult.class);


        // Given
        given(session.getUserProperties()).willReturn(properties);
        given(properties.getProperty(INVOCATION_ID)).willReturn("");
        given(configurationFactory.forLogLevel(mojo, logLevel)).willReturn(configuration);
        given(requestsFactory.create(configuration)).willReturn(singletonList(request));
        given(invoker.execute(request)).willReturn(success);
        given(success.getExitCode()).willReturn(0);

        // When
        mojo.execute();

        // Then
        then(success).should().getExitCode();
    }

    @Test
    public void Can_invoke_the_current_module_if_invocation_id_is_null()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final Properties properties = mock(Properties.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request = mock(InvocationRequest.class);
        final InvocationResult success = mock(InvocationResult.class);


        // Given
        given(session.getUserProperties()).willReturn(properties);
        given(properties.getProperty(INVOCATION_ID)).willReturn(null);
        given(configurationFactory.forLogLevel(mojo, logLevel)).willReturn(configuration);
        given(requestsFactory.create(configuration)).willReturn(singletonList(request));
        given(invoker.execute(request)).willReturn(success);
        given(success.getExitCode()).willReturn(0);

        // When
        mojo.execute();

        // Then
        then(success).should().getExitCode();
    }

    @Test
    public void Will_fail_correctly_if_the_invocations_are_incorrectly_configured()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request = mock(InvocationRequest.class);
        final MavenInvocationException exception = new MavenInvocationException(someString());

        // Given
        given(configurationFactory.forLogLevel(mojo, logLevel)).willReturn(configuration);
        given(requestsFactory.create(configuration)).willReturn(singletonList(request));
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
        given(configurationFactory.forLogLevel(mojo, logLevel)).willReturn(configuration);
        given(requestsFactory.create(configuration)).willReturn(asList(request1, request2, request3));
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
    public void Can_get_the_current_maven_project()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        // When
        final MavenProject actual = mojo.getProject();

        // Then
        assertThat(actual, is(project));
    }

    @Test
    public void Can_get_the_current_maven_session()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        // When
        final MavenSession actual = mojo.getSession();

        // Then
        assertThat(actual, is(session));
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
    public void Can_configured_the_profiles_that_will_run_with_each_invocation()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final String profile1 = someAlphanumericString();
        final String profile2 = someAlphanumericString();
        final String profile3 = someAlphanumericString();

        // Given
        mojo.withProfiles(format("%s,%s,%s", profile1, profile2, profile3));

        // When
        final List<String> actual = mojo.getProfiles();

        // Then
        assertThat(actual, contains(profile1, profile2, profile3));
    }

    @Test
    public void Can_run_for_each_configured_invocation()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final String item1 = someAlphanumericString();
        final String item2 = someAlphanumericString();
        final String item3 = someAlphanumericString();

        // Given
        mojo.withInvocations(format("%s,%s,%s", item1, item2, item3));

        // When
        final List<String> actual = mojo.getInvocations();

        // Then
        assertThat(actual, contains(item1, item2, item3));
    }

    @Test
    public void Can_leave_the_invocations_empty()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        // Given
        mojo.withInvocations("");

        // When
        final List<String> actual = mojo.getInvocations();

        // Then
        assertThat(actual, empty());
    }

    @Test
    public void Can_leave_the_invocations_blank()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        // Given
        mojo.withInvocations(null);

        // When
        final List<String> actual = mojo.getInvocations();

        // Then
        assertThat(actual, empty());
    }

    @Test
    public void Can_set_the_goals_to_run_for_each_configured_invocation()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final String goal1 = someAlphanumericString();
        final String goal2 = someAlphanumericString();
        final String goal3 = someAlphanumericString();

        // Given
        mojo.withGoals(format("%s,%s,%s", goal1, goal2, goal3));

        // When
        final List<String> actual = mojo.getGoals();

        // Then
        assertThat(actual, contains(goal1, goal2, goal3));
    }

    @Test
    public void Can_set_extra_properties_for_each_configured_invocation()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        final Properties properties = mock(Properties.class);

        // Given
        mojo.withProperties(properties);

        // When
        final Properties actual = mojo.getProperties();

        // Then
        assertThat(actual, is(properties));
    }

    @Test
    public void Can_get_empty_properties()
        throws MojoFailureException, MojoExecutionException, MavenInvocationException {

        // Given
        mojo.withProperties(null);

        // When
        final Properties actual = mojo.getProperties();

        // Then
        assertThat(actual, equalTo(new Properties()));
    }

    @Test
    public void Will_not_invoke_anything_if_run_from_within_a_multi_invocation()
        throws MojoFailureException, MojoExecutionException {

        final Properties properties = mock(Properties.class);

        // Given
        given(session.getUserProperties()).willReturn(properties);
        given(properties.getProperty(INVOCATION_ID)).willReturn(someString());

        // When
        mojo.execute();

        // Then
        then(session).should().getUserProperties();
        verifyNoMoreInteractions(session);
        verifyZeroInteractions(project, configurationFactory, requestsFactory, invoker);
    }
}