package nowrek.newsfilter.DataStructures;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import nowrek.newsfilter.Utils.ConfigObservableImpl;

public class AppConfigData extends ConfigObservableImpl {

    private HashMap<Class, LinkedList<? extends ConfigData>> dataMap = new HashMap<>();

    public LinkedList<PageConfigData> getPageList() {
        return (LinkedList<PageConfigData>) dataMap.get(PageConfigData.class);
    }

    public LinkedList<String> getURLListAsString() {
        LinkedList<String> result = new LinkedList<>();
        for (PageConfigData page : getPageList()) {
            String pageUrlHandle = page.getPageUrl();
            result.add(pageUrlHandle);
        }
        return result;
    }

    public LinkedList<URLHandle> getURLList() {
        LinkedList<URLHandle> result = new LinkedList<>();
        for (PageConfigData page : getPageList()) {
            URLHandle pageUrlHandle = new URLHandle(page.getPageUrl());
            result.add(pageUrlHandle);
        }
        return result;
    }

    public void setPageList(final LinkedList<PageConfigData> pageList) {
        dataMap.put(PageConfigData.class, pageList);
        Collections.sort(dataMap.get(PageConfigData.class));
    }

    public void addData(final PageConfigData data) {
        if (!dataMap.keySet().contains(PageConfigData.class)) {
            dataMap.put(PageConfigData.class, new LinkedList<PageConfigData>());
        }
        ((LinkedList<PageConfigData>) dataMap.get(PageConfigData.class)).add(data);
        Collections.sort(dataMap.get(PageConfigData.class));
    }

    public void setData(final PageConfigData data) {
        PageConfigData storedData = (PageConfigData) findDataByTypeAndKey(PageConfigData.class, data.getKey());
        if (storedData != null) {
            storedData.setName(data.getName());
            storedData.setPageUrl(data.getPageUrl());
            storedData.setTags(data.getTags());
            Collections.sort(dataMap.get(PageConfigData.class));
        }
    }

    public void removeData(final PageConfigData data) {
        removeAnyData(data);
    }

    private void removeAnyData(final ConfigData data) {
        dataMap.get(data.getClass()).remove(findDataByTypeAndKey(data.getClass(), data.Key));
        Collections.sort(dataMap.get(PageConfigData.class));
    }

    public void processChanges(final Collection<ChangeConfig> changeList) {
        for (ChangeConfig changeConfig : changeList) {
            try {
                getAppropriateMethod(this, changeConfig).invoke(this, changeConfig.getChangeData());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        notifyListeners(changeList);
    }

    public static Method getAppropriateMethod(final Object object, final ChangeConfig changeConfig) throws NoSuchMethodException {
        return object.getClass().getMethod(getProperMethodName(changeConfig.getChangeType()), changeConfig.getDataType());
    }

    private static String getProperMethodName(ChangeConfig.ChangeType type) {
        switch (type) {
            case Added:
                return "addData";
            case Modified:
                return "setData";
            case Removed:
                return "removeData";
            default:
                return null;
        }
    }

    private ConfigData findDataByTypeAndKey(Class dataType, String key) {
        for(ConfigData storedData : dataMap.get(dataType)){
            if (storedData.getKey().equals(key)) {
                return storedData;
            }
        }
        return null;
    }

    public AppConfigData(JSONObject jsonObject) {
        Iterator<String> jsonIterator = jsonObject.keys();
        try {
            while (jsonIterator.hasNext()) {
                String typeName = jsonIterator.next();
                if (typeName.equals(PageConfigData.class.toString())) {
                    LinkedList<PageConfigData> pageConfigList = getPageConfigList(jsonObject.getJSONArray(typeName));
                    if (!pageConfigList.isEmpty()) {
                        dataMap.put(PageConfigData.class, pageConfigList);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "EXCEPTION WHILE DESERIALIZING FROM JSON", e);
        }
    }

    private LinkedList<PageConfigData> getPageConfigList(JSONArray jsonArray) {
        LinkedList<PageConfigData> pageList = new LinkedList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                pageList.add(new PageConfigData(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "EXCEPTION WHILE GETTING PAGE CONFIG LIST FROM JSON", e);
        }
        return pageList;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        Iterator<Class> typesIterator = dataMap.keySet().iterator();
        try {
            while (typesIterator.hasNext()) {
                Class type = typesIterator.next();
                JSONArray configData = new JSONArray();
                Iterator<ConfigData> dataIterator = (Iterator<ConfigData>) dataMap.get(type).listIterator();
                while (dataIterator.hasNext()) {
                    ConfigData data = dataIterator.next();
                    configData.put(data.toJsonObject());
                }
                jsonObject.put(type.toString(), configData);
            }
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "EXCEPTION WHILE CONVERTING TO JSON", e);
        }
        return jsonObject;
    }
}
