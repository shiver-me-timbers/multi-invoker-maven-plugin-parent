package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.io.FileMatchers.anExistingDirectory;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;

public class ITMultiInvokerMojo {

    @Test
    public void Can_invoke_a_maven_child_project_once_for_each_profile() throws Exception {

        // Given
        final MavenXpp3Reader reader = new MavenXpp3Reader();
        final Model model = reader.read(new FileReader(new File("pom.xml")));

        // When
        final List<Profile> profiles = model.getProfiles();

        // Then
        assertThat(profiles, not(empty()));
        for (Profile profile : profiles) {
            final String id = profile.getId();
            final File profileDir = new File(new File("target"), id);
            assertThat(profileDir, anExistingDirectory());
            assertThat(new File(profileDir, "touch-file" + id), anExistingFile());
        }
    }
}
