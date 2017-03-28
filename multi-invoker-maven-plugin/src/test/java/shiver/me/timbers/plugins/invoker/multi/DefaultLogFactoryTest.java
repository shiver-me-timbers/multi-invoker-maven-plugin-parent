package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomEnums.someEnum;
import static shiver.me.timbers.matchers.Matchers.hasProperty;

public class DefaultLogFactoryTest {

    @Test
    public void Can_create_a_default_log() {

        // Given
        final LogLevel logLevel = someEnum(LogLevel.class);

        // When
        final Log actual = new DefaultLogFactory().create(logLevel);

        // Then
        assertThat(actual, hasProperty("logger.threshold", logLevel.getThreshold()));
    }
}