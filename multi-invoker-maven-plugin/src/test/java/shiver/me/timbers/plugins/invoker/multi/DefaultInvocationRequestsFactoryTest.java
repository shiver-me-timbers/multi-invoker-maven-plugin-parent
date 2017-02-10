package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DefaultInvocationRequestsFactoryTest {

    private InvocationRequestFactory requestFactory;
    private MultiInvokerConfigurationFactory configurationFactory;
    private DefaultInvocationRequestsFactory factory;

    @Before
    public void setUp() {
        requestFactory = mock(InvocationRequestFactory.class);
        configurationFactory = mock(MultiInvokerConfigurationFactory.class);
        factory = new DefaultInvocationRequestsFactory(configurationFactory, requestFactory);
    }

    @Test
    public void Instantiation_for_coverage() {
        new DefaultInvocationRequestsFactory();
    }

    @Test
    public void Can_create_an_invocation_request_for_each_profile_defined_in_the_current_pom_file() {

        final MavenProject project = mock(MavenProject.class);
        final MavenSession session = mock(MavenSession.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final Model model = mock(Model.class);
        final Profile profile1 = mock(Profile.class);
        final Profile profile2 = mock(Profile.class);
        final Profile profile3 = mock(Profile.class);
        final MultiInvokerConfiguration configuration1 = mock(MultiInvokerConfiguration.class);
        final MultiInvokerConfiguration configuration2 = mock(MultiInvokerConfiguration.class);
        final MultiInvokerConfiguration configuration3 = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request1 = mock(InvocationRequest.class);
        final InvocationRequest request2 = mock(InvocationRequest.class);
        final InvocationRequest request3 = mock(InvocationRequest.class);

        // Given
        given(configuration.isForEachProfile()).willReturn(true);
        given(project.getModel()).willReturn(model);
        given(model.getProfiles()).willReturn(asList(profile1, profile2, profile3));
        given(configurationFactory.forProfile(configuration, profile1)).willReturn(configuration1);
        given(configurationFactory.forProfile(configuration, profile2)).willReturn(configuration2);
        given(configurationFactory.forProfile(configuration, profile3)).willReturn(configuration3);
        given(requestFactory.create(project, session, configuration1)).willReturn(request1);
        given(requestFactory.create(project, session, configuration2)).willReturn(request2);
        given(requestFactory.create(project, session, configuration3)).willReturn(request3);

        // When
        final List<InvocationRequest> actual = factory.create(project, session, configuration);

        // Then
        assertThat(actual, contains(request1, request2, request3));
    }

    @Test
    public void Can_create_an_invocation_request_for_each_configured_invocation() {

        final MavenProject project = mock(MavenProject.class);
        final MavenSession session = mock(MavenSession.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);

        final String item1 = someString();
        final String item2 = someString();
        final String item3 = someString();
        final MultiInvokerConfiguration configuration1 = mock(MultiInvokerConfiguration.class);
        final MultiInvokerConfiguration configuration2 = mock(MultiInvokerConfiguration.class);
        final MultiInvokerConfiguration configuration3 = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request1 = mock(InvocationRequest.class);
        final InvocationRequest request2 = mock(InvocationRequest.class);
        final InvocationRequest request3 = mock(InvocationRequest.class);

        // Given
        given(configuration.isForEachProfile()).willReturn(false);
        given(configuration.getInvocations()).willReturn(asList(item1, item2, item3));
        given(configurationFactory.forInvocation(configuration, item1)).willReturn(configuration1);
        given(configurationFactory.forInvocation(configuration, item2)).willReturn(configuration2);
        given(configurationFactory.forInvocation(configuration, item3)).willReturn(configuration3);
        given(requestFactory.create(project, session, configuration1)).willReturn(request1);
        given(requestFactory.create(project, session, configuration2)).willReturn(request2);
        given(requestFactory.create(project, session, configuration3)).willReturn(request3);

        // When
        final List<InvocationRequest> actual = factory.create(project, session, configuration);

        // Then
        assertThat(actual, contains(request1, request2, request3));
    }
}