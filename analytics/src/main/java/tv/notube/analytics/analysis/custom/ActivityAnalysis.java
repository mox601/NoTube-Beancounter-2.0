package tv.notube.analytics.analysis.custom;

import org.joda.time.DateTime;
import tv.notube.analytics.analysis.*;
import tv.notube.commons.configuration.analytics.AnalysisDescription;
import tv.notube.commons.configuration.analytics.MethodDescription;
import tv.notube.commons.storage.model.Activity;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.ActivityLogException;
import tv.notube.commons.storage.model.fields.Field;
import tv.notube.commons.storage.model.fields.StringField;
import tv.notube.commons.storage.model.fields.URLField;

import java.util.HashSet;
import java.util.Set;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ActivityAnalysis extends StorageAnalysis {

    public ActivityAnalysis(
            ActivityLog alog,
            String name,
            String description
    ) {
        super(alog, name, description);
    }

    public AnalysisResult run(String owner) throws AnalysisException {
        Activity[] activities;
        try {
            activities = alog.filter(new DateTime(), owner, query);
        } catch (ActivityLogException e) {
            throw new AnalysisException("Error while filtering from alog", e);
        }
        ActivityAnalysisResult result = new ActivityAnalysisResult(
                new DateTime()
        );
        for (Activity activity : activities) {
            Field fields[];
            try {
                fields = alog.getFields(activity.getId());
            } catch (ActivityLogException e) {
                throw new AnalysisException(
                        "Error while getting field for '" + activity.getId() + "'",
                        e
                );
            }
            for(Field field : fields) {
                if(field instanceof StringField) {
                    StringField sf = (StringField) field;
                    if(sf.getName().equals("verb")) {
                        result.add(sf.getValue());
                    }
                }
                if(field instanceof URLField) {
                    URLField sf = (URLField) field;
                    if(sf.getName().equals("service")) {
                        result.add(sf.getValue());
                    }
                }
            }
        }
        return result;
    }

    @Override
    public AnalysisDescription getAnalysisDescription() {
        MethodDescription getAmount;
        getAmount = new MethodDescription(
                "getNumberOfActivitiesByVerb",
                "it returns the total number of activities of the verb specified as parameter",
                new String[] { "java.lang.String" }
        );

        MethodDescription getActivities;
        getActivities = new MethodDescription(
                "getActivities",
                "it returns the activity types the user performed",
                new String[] {}
        );

        MethodDescription getTotalActivities;
        getTotalActivities = new MethodDescription(
                "getTotalActivities",
                "it returns the total number of activities of the user",
                new String[] {}
        );

        MethodDescription getServiceAmount;
        getServiceAmount = new MethodDescription(
                "getNumberOfActivitiesByService",
                "it returns the total number of activities done on a " +
                        "specified service (must be a valid URL)",
                new String[] { "java.net.URL" }
        );

        Set<MethodDescription> aadMds = new HashSet<MethodDescription>();
        aadMds.add(getAmount);
        aadMds.add(getActivities);
        aadMds.add(getTotalActivities);
        aadMds.add(getServiceAmount);

        return new AnalysisDescription(
                getName(),
                getDescription(),
                getQuery(),
                ActivityAnalysis.class.getCanonicalName(),
                ActivityAnalysisResult.class.getCanonicalName(),
                aadMds
        );
    }
}
