package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;

import static org.hamcrest.io.FileMatchers.anExistingDirectory;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;

public class ITMultiInvokerMojo {

    @Test
    public void Can_invoke_a_maven_project_with_a_configured_phase_and_goal() throws Exception {

        // Given
        final MavenXpp3Reader reader = new MavenXpp3Reader();
        final Model model = reader.read(new FileReader(new File("pom.xml")));

        // When
        final String invocation = model.getProperties().getProperty("test-invocation");

        // Then
        final File profileDir = new File(new File("target"), invocation);
        assertThat(profileDir, anExistingDirectory());
        assertThat(new File(profileDir, "touch-file" + invocation), anExistingFile());
    }
}
