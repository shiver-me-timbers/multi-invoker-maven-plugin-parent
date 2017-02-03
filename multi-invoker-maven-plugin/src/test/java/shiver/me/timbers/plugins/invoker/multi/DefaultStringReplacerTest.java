package shiver.me.timbers.plugins.invoker.multi;

import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;

public class DefaultStringReplacerTest {

    @Test
    public void Can_replace_a_value() {

        // Given
        final String prefix = someAlphanumericString();
        final String substitution = format("@%s@", someAlphanumericString());
        final String suffix = someAlphanumericString();
        final String value = someAlphanumericString();

        // When
        final String actual = new DefaultStringReplacer()
            .replace(format("%s%s%s", prefix, substitution, suffix), substitution, value);

        // Then
        assertThat(actual, equalTo(format("%s%s%s", prefix, value, suffix)));
    }
}