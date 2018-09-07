package nowrek.newsfilter.DataStructures;

import org.json.JSONObject;

public abstract class ConfigData implements Comparable<ConfigData> {

    protected String Key;
    protected String Name;

    ConfigData() {
    }

    abstract public JSONObject toJsonObject();

    ConfigData(String key, String name) {
        setKey(key);
        setName(name);
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
