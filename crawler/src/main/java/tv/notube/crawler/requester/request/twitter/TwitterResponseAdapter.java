package tv.notube.crawler.requester.request.twitter;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TwitterResponseAdapter implements JsonDeserializer<TwitterResponse> {

    private DateTimeFormatter dateTimeFormatter;

    public TwitterResponseAdapter() {
        dateTimeFormatter = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss Z YYYY");
    }

    public TwitterResponse deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        JsonArray tweetsArray = jsonElement.getAsJsonArray();
        List<TwitterTweet> twitterTweets = new ArrayList<TwitterTweet>();
        for(JsonElement tweetElement : tweetsArray) {
            TwitterTweet tweet = new TwitterTweet();

            DateTime createdAt = dateTimeFormatter.parseDateTime(
                    tweetElement
                    .getAsJsonObject()
                    .get("created_at")
                    .getAsString()
            );
            tweet.setCreatedAt(createdAt);

            String text = tweetElement
                    .getAsJsonObject()
                    .get("text")
                    .getAsString();
            tweet.setText(text);

            String username = tweetElement.getAsJsonObject().get("user")
                    .getAsJsonObject().get("screen_name").getAsString();
            tweet.setUsername(username);

            URL tweetUrl;
            try {
                tweetUrl = new URL(
                        "http://twitter.com/" + username + "/status/" +
                                tweetElement.getAsJsonObject()
                                        .get("id_str")
                                        .getAsString()
                );
                tweet.setUrl(tweetUrl);
            } catch (MalformedURLException e) {
                // leave it null
            }
            // libby
            JsonArray urlsArray = tweetElement
                    .getAsJsonObject()
                    .get("entities")
                    .getAsJsonObject()
                    .get("urls")
                    .getAsJsonArray();
            for(JsonElement jsonUrl : urlsArray) {
                try {
                    JsonElement expandedUrlObj = jsonUrl.getAsJsonObject()
                            .get("expanded_url");
                    if(expandedUrlObj instanceof JsonNull) {
                        continue;
                        // sometimes Twitter returns null in these fields
                        // so just skip
                    }
                    URL url = new URL(expandedUrlObj.getAsString());
                    tweet.addUrl(url);
                } catch (MalformedURLException e) {
                    // could happen that a url is not well formed
                    // just skip
                }
            }
            JsonElement entitiesJson = tweetElement.getAsJsonObject().get("entities");
            JsonArray hashTagsJson = entitiesJson.getAsJsonObject().get("hashtags").getAsJsonArray();
            for(JsonElement jsonHt : hashTagsJson) {
                    tweet.addHashTag(
                            jsonHt.getAsJsonObject()
                                    .get("text")
                                    .getAsString()
                    );
            }
            twitterTweets.add(tweet);
        }
        return new TwitterResponse(twitterTweets);
    }
}
