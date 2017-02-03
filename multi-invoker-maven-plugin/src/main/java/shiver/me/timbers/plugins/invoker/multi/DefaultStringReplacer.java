package shiver.me.timbers.plugins.invoker.multi;

import org.codehaus.plexus.component.annotations.Component;

/**
 * @author Karl Bennett
 */
@Component(role = StringReplacer.class, hint = "default")
class DefaultStringReplacer implements StringReplacer {

    @Override
    public String replace(String string, String substitution, String value) {
        return string.replaceAll(substitution, value);
    }
}
