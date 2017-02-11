package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.util.List;
import java.util.Properties;

/**
 * @author Karl Bennett
 */
interface MultiInvokerConfiguration {

    MavenProject getProject();

    MavenSession getSession();

    Log getLog();

    String getInvocationId();

    boolean isForEachProfile();

    List<String> getInvocations();

    List<String> getProfiles();

    List<String> getGoals();

    Properties getProperties();
}
