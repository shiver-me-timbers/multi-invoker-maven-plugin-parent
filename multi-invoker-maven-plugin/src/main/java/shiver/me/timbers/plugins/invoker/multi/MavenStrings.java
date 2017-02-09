package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.shared.invoker.InvocationRequest;

/**
 * @author Karl Bennett
 */
interface MavenStrings {

    String toGoals(InvocationRequest request);

    String toProfiles(InvocationRequest request);
}
