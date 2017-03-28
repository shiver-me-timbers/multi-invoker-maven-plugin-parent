package shiver.me.timbers.plugins.invoker.multi;

import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomEnums.someEnum;

public class LogLevelTest {

    @Test
    public void Can_get_the_log_prefix() {

        // Given
        final LogLevel logLevel = someEnum(LogLevel.class);

        // When
        final String actual = logLevel.prefix();

        // Then
        assertThat(actual, equalTo(format("[%s]", logLevel.longName())));
    }
}