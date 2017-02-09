package shiver.me.timbers.plugins.invoker.multi;

import org.apache.maven.shared.invoker.InvocationRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karl Bennett
 */
public class DefaultMavenStrings implements MavenStrings {

    @Override
    public String toGoals(InvocationRequest request) {
        final List<String> goals = new ArrayList<>(request.getGoals());
        if (goals.isEmpty()) {
            return "";
        }
        final StringBuilder profilesString = new StringBuilder(goals.remove(0));
        for (String goal : goals) {
            profilesString.append(' ').append(goal);
        }
        return profilesString.toString();
    }

    @Override
    public String toProfiles(InvocationRequest request) {
        final List<String> profiles = new ArrayList<>(request.getProfiles());
        if (profiles.isEmpty()) {
            return "";
        }
        final StringBuilder profilesString = new StringBuilder("-P");
        profilesString.append(profiles.remove(0));
        for (String profile : profiles) {
            profilesString.append(',').append(profile);
        }
        return profilesString.toString();
    }
}
