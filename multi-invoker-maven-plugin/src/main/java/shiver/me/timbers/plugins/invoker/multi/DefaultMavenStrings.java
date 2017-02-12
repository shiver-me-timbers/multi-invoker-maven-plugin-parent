package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.codehaus.plexus.component.annotations.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static java.lang.String.format;
import static java.util.Map.Entry;

/**
 * @author Karl Bennett
 */
@Component(role = MavenStrings.class, hint = "default")
class DefaultMavenStrings implements MavenStrings {

    @Override
    public String toGoals(InvocationRequest request) {
        return concat(request.getGoals(), " ");
    }

    @Override
    public String toProfiles(InvocationRequest request) {
        final String profiles = concat(request.getProfiles(), ",");
        if (profiles.isEmpty()) {
            return "";
        }
        return " -P" + profiles;
    }

    @Override
    public String toProperties(MultiInvokerConfiguration configuration, InvocationRequest request) {
        final String properties = concat(
            cleanupRequestProperties(configuration, request).entrySet(),
            new Concatinate<Entry<Object, Object>>() {
                @Override
                public String toString(Entry<Object, Object> element) {
                    return format("-D%s=%s", element.getKey(), element.getValue());
                }

                @Override
                public void concat(StringBuilder builder, Entry<Object, Object> element) {
                    builder.append(" ").append(toString(element));
                }
            }
        );
        if (properties.isEmpty()) {
            return "";
        }
        return " " + properties;
    }

    private static String concat(Collection<String> strings, final String delimiter) {
        return concat(strings, new Concatinate<String>() {
            @Override
            public String toString(String element) {
                return element;
            }

            @Override
            public void concat(StringBuilder builder, String element) {
                builder.append(delimiter).append(element);
            }
        });
    }

    private static <T> String concat(Collection<T> strings, Concatinate<T> concatinate) {
        final List<T> stringsCopy = new ArrayList<>(strings);
        if (stringsCopy.isEmpty()) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(concatinate.toString(stringsCopy.remove(0)));
        for (T element : stringsCopy) {
            concatinate.concat(builder, element);
        }
        return builder.toString();
    }

    private static Properties cleanupRequestProperties(MultiInvokerConfiguration configuration, InvocationRequest request) {
        final MavenSession session = configuration.getSession();
        final Properties systemProperties = copy(session.getSystemProperties());
        final Properties requestProperties = copy(request.getProperties());
        remove(session.getUserProperties(), systemProperties);
        remove(systemProperties, requestProperties);
        return requestProperties;
    }

    private static Properties copy(Properties properties) {
        final Properties copiedProperties = new Properties();
        copiedProperties.putAll(properties);
        return copiedProperties;
    }

    private static void remove(Properties properties, Properties origin) {
        for (Entry<Object, Object> entry : properties.entrySet()) {
            origin.remove(entry.getKey());
        }
    }

    private interface Concatinate<T> {

        String toString(T element);

        void concat(StringBuilder builder, T element);
    }
}
