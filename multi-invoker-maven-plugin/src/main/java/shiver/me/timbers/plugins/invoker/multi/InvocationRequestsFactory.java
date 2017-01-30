package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.InvocationRequest;

import java.util.List;

/**
 * @author Karl Bennett
 */
interface InvocationRequestsFactory {

    List<InvocationRequest> create(MavenProject project, MavenSession session, MultiInvokerConfiguration configuration);
}
