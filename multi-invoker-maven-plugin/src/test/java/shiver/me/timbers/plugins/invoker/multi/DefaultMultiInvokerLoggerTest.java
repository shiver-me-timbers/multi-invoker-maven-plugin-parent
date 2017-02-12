package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.junit.Test;

import static java.lang.String.format;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DefaultMultiInvokerLoggerTest {

    @Test
    public void Instantiation_for_coverage() {
        new DefaultMultiInvokerLogger();
    }

    @Test
    public void Can_log_an_iinvokation() {

        final MavenStrings mavenStrings = mock(MavenStrings.class);
        final MultiInvokerConfiguration configuration = mock(MultiInvokerConfiguration.class);
        final InvocationRequest request = mock(InvocationRequest.class);

        final Log log = mock(Log.class);
        final String goals = someString();
        final String profiles = someString();
        final String properties = someString();

        // Given
        given(configuration.getLog()).willReturn(log);
        given(mavenStrings.toGoals(request)).willReturn(goals);
        given(mavenStrings.toProfiles(request)).willReturn(profiles);
        given(mavenStrings.toProperties(configuration, request)).willReturn(properties);

        // When
        new DefaultMultiInvokerLogger(mavenStrings).log(configuration, request);

        // Then
        then(log).should().info(format("Invoking: mvn %s%s%s", goals, profiles, properties));
    }
}