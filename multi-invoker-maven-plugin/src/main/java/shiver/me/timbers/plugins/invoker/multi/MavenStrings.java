package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.shared.invoker.InvocationRequest;

/**
 * @author Karl Bennett
 */
interface MavenStrings {

    String toArtifactId(MultiInvokerConfiguration configuration);

    String toGoals(InvocationRequest request);

    String toProfiles(InvocationRequest request);

    String toProperties(MultiInvokerConfiguration configuration, InvocationRequest request);
}
