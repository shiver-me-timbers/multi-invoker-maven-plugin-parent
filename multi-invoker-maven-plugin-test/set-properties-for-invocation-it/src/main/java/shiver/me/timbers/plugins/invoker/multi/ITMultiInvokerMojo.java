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
    public void Can_invoke_a_maven_project_once_for_each_configured_invocation() throws Exception {

        // Given
        final MavenXpp3Reader reader = new MavenXpp3Reader();
        final Model model = reader.read(new FileReader(new File("pom.xml")));

        // When
        final String item = model.getProperties().getProperty("test-invocation");

        // Then
        final File profileDir = new File(new File("target"), item + "-one-two");
        assertThat(profileDir, anExistingDirectory());
        assertThat(new File(profileDir, "touch-file" + item), anExistingFile());
    }
}
