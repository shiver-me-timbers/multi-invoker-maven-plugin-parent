package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.plugins.invoker.multi.LogLevel.DEBUG;
import static shiver.me.timbers.plugins.invoker.multi.LogLevel.ERROR;
import static shiver.me.timbers.plugins.invoker.multi.LogLevel.FATAL;
import static shiver.me.timbers.plugins.invoker.multi.LogLevel.INFO;
import static shiver.me.timbers.plugins.invoker.multi.LogLevel.WARN;

public class DefaultOutputHandlerFactoryTest {

    @Test
    public void Can_log_debug_with_the_info_logging_output_handler() {

        // Given
        final Log log = mock(Log.class);
        final String line = DEBUG.prefix() + someString();

        // When
        new DefaultOutputHandlerFactory().createInfoFrom(log).consumeLine(line);

        // Then
        verify(log).debug(line);
        verifyNoMoreInteractions(log);
    }

    @Test
    public void Can_log_info_with_the_info_logging_output_handler() {

        // Given
        final Log log = mock(Log.class);
        final String line = INFO.prefix() + someString();

        // When
        new DefaultOutputHandlerFactory().createInfoFrom(log).consumeLine(line);

        // Then
        verify(log).info(line);
        verifyNoMoreInteractions(log);
    }

    @Test
    public void Can_log_warn_with_the_info_logging_output_handler() {

        // Given
        final Log log = mock(Log.class);
        final String line = WARN.prefix() + someString();

        // When
        new DefaultOutputHandlerFactory().createInfoFrom(log).consumeLine(line);

        // Then
        verify(log).warn(line);
        verifyNoMoreInteractions(log);
    }

    @Test
    public void Can_log_error_with_the_info_logging_output_handler() {

        // Given
        final Log log = mock(Log.class);
        final String line = ERROR.prefix() + someString();

        // When
        new DefaultOutputHandlerFactory().createInfoFrom(log).consumeLine(line);

        // Then
        verify(log).error(line);
        verifyNoMoreInteractions(log);
    }

    @Test
    public void Can_log_fatal_with_the_info_logging_output_handler() {

        // Given
        final Log log = mock(Log.class);
        final String line = FATAL.prefix() + someString();

        // When
        new DefaultOutputHandlerFactory().createInfoFrom(log).consumeLine(line);

        // Then
        verify(log).error(line);
        verifyNoMoreInteractions(log);
    }

    @Test
    public void Can_create_an_error_logging_output_handler() {

        // Given
        final Log log = mock(Log.class);
        final String line = someString();

        // When
        new DefaultOutputHandlerFactory().createErrorFrom(log).consumeLine(line);

        // Then
        verify(log).error(line);
    }
}