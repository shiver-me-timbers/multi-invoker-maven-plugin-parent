package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DefaultOutputHandlerFactoryTest {

    @Test
    public void Can_create_a_logging_output_handler() {

        // Given
        final Log log = mock(Log.class);
        final String line = someString();

        // When
        new DefaultOutputHandlerFactory().createFrom(log).consumeLine(line);

        // Then
        verify(log).info(line);
    }
}