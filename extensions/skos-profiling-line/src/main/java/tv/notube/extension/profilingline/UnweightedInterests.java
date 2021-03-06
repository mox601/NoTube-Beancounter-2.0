package tv.notube.extension.profilingline;

import tv.notube.commons.model.activity.Activity;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UnweightedInterests {

    private String user;

    private Map<URI, Set<Activity>> interests;

    public UnweightedInterests(String user, Map<URI,Set<Activity>> interests) {
        this.user = user;
        this.interests = interests;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Map<URI, Set<Activity>> getInterests() {
        return interests;
    }

    public void setInterests(Map<URI, Set<Activity>> interests) {
        this.interests = interests;
    }
}
