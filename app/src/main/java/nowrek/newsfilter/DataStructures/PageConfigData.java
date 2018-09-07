package nowrek.newsfilter.DataStructures;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PageConfigData extends ConfigData {
    private String PageUrl;
    private ArrayList<String> Tags;

    public String getPageUrl() {
        return PageUrl;
    }

    public void setPageUrl(String pageUrl) {
        PageUrl = pageUrl;
    }

    public ArrayList<String> getTags() {
        return Tags;
    }

    public void setTags(ArrayList<String> tags) {
        Tags = tags;
    }

    public String getJoinedTags() {
        return joinString(",", getTags());
    }

    public PageConfigData(String key, String name, String pageUrl, ArrayList<String> tags) {
        super(key, name);
        setPageUrl(pageUrl);
        setTags(tags);
    }

    PageConfigData(JSONObject pageConfigJson) {
        try {
            setKey(pageConfigJson.getString("key"));
            setName(pageConfigJson.getString("name"));
            setPageUrl(pageConfigJson.getString("url"));
            setTags(new ArrayList<>(Arrays.asList(pageConfigJson.getString("tags").split(","))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", getKey());
            jsonObject.put("name", getName());
            jsonObject.put("url", getPageUrl());
            jsonObject.put("tags", getJoinedTags());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private String joinString(String delimiter, Iterable<String> elements) {
        Iterator<String> iterator = elements.iterator();
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(delimiter);
            }
        }
        return builder.toString();
    }

    @Override
    public int compareTo(@NonNull ConfigData o) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.getName(), o.getName());
    }
}
