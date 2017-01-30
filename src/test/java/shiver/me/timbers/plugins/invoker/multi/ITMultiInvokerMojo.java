package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Profile;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.project.MavenProject;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.codehaus.plexus.logging.Logger.LEVEL_ERROR;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.io.FileMatchers.anExistingDirectory;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;

public class ITMultiInvokerMojo {

    @Rule
    public MojoRule mojoRule = new MojoRule();

    @Test
    public void Can_invoke_a_maven_project_once_for_each_profile() throws Exception {

        final MavenProject project = readProject("once-for-each-profile");
        final MavenSession session = mojoRule.newMavenSession(project);
        final MavenExecutionRequest request = session.getRequest();
        final List<Profile> profiles = project.getModel().getProfiles();
        final File target = getTarget(project);

        // Given
        request.setLoggingLevel(LEVEL_ERROR);
        request.setGoals(singletonList("package"));

        // When
        mojoRule.executeMojo(session, project, "invoker");

        // Then
        assertThat(profiles, not(empty()));
        for (Profile profile : profiles) {
            final String id = profile.getId();
            final File profileDir = new File(target, id);
            assertThat(profileDir, anExistingDirectory());
            assertThat(new File(profileDir, id), anExistingFile());
        }
    }

    private MavenProject readProject(String resourcePath) throws Exception {
        return mojoRule.readMavenProject(createDir(resourcePath));
    }

    private static File createDir(String resourcePath) {
        return new File(Thread.currentThread().getContextClassLoader().getResource(resourcePath).getPath());
    }

    private static File getTarget(MavenProject project) {
        return new File(project.getBasedir(), "target");
    }
}
