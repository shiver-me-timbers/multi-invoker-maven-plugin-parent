package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.io.FileMatchers.anExistingDirectory;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;

public class ITMultiInvokerMojo {

    @Test
    public void Can_set_the_profiles_to_user_for_each_invocation() throws Exception {

        // Given
        final MavenXpp3Reader reader = new MavenXpp3Reader();
        final Model model = reader.read(new FileReader(new File("pom.xml")));

        // When
        final String[] items = model.getProperties().getProperty("test-items").split(",");
        final String[] profiles = model.getProperties().getProperty("test-profiles").split(",");

        // Then
        assertThat(items, not(emptyArray()));
        for (String item : items) {
            final File profileDir = new File(new File("target"), item);
            assertThat(profileDir, anExistingDirectory());
            assertThat(new File(profileDir, "touch-file" + item), anExistingFile());
            for (String profile : profiles) {
                assertThat(new File(profileDir, "touch-file" + profile), anExistingFile());
            }
        }
    }
}
