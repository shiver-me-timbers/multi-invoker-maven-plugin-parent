package shiver.me.timbers.plugins.invoker.multi;

import org.codehaus.plexus.component.annotations.Component;

import java.util.Map;
import java.util.Properties;

/**
 * @author Karl Bennett
 */
@Component(role = PropertiesAppender.class, hint = "default")
class DefaultPropertiesAppender implements PropertiesAppender {

    @Override
    public Properties append(Properties... properties) {
        final Properties appendedProperties = new Properties();
        for (Properties props : properties) {
            addProperties(appendedProperties, props);
        }
        return appendedProperties;
    }

    private static void addProperties(Properties origin, Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            origin.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
    }
}
