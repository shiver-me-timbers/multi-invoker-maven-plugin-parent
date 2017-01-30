package shiver.me.timbers.plugins.invoker.multi;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DefaultPropertiesAppenderTest {

    @Test
    public void Can_append_properties() {

        final Properties properties1 = new Properties();
        final Properties properties2 = new Properties();
        final Properties properties3 = new Properties();

        // Given
        properties1.setProperty(someString(), someString());
        properties2.setProperty(someString(), someString());
        properties2.setProperty(someString(), someString());
        properties3.setProperty(someString(), someString());
        properties3.setProperty(someString(), someString());
        properties3.setProperty(someString(), someString());

        // When
        final Properties actual = new DefaultPropertiesAppender().append(properties1, properties2, properties3);

        // Then
        assertThat(actual, containsProperties(properties1));
        assertThat(actual, containsProperties(properties2));
        assertThat(actual, containsProperties(properties3));
    }

    private static Matcher<Properties> containsProperties(final Properties expected) {

        return new TypeSafeMatcher<Properties>() {

            @Override
            protected boolean matchesSafely(Properties actual) {
                for (Map.Entry<Object, Object> entry : expected.entrySet()) {
                    if (!containsProperty(entry.getKey(), entry.getValue()).matches(actual)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(expected);
            }
        };
    }

    private static Matcher<Properties> containsProperty(final Object key, final Object value) {

        return new TypeSafeMatcher<Properties>() {

            @Override
            protected boolean matchesSafely(Properties actual) {
                final Object actualValue = actual.get(key);
                return actualValue != null && actualValue.toString().equals(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("key: ").appendValue(key).appendText(" value: ").appendValue(value);
            }
        };
    }
}