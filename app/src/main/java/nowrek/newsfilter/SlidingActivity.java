package nowrek.newsfilter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.LinkedList;

import nowrek.newsfilter.DataStructures.AppConfigData;
import nowrek.newsfilter.DataStructures.Page;
import nowrek.newsfilter.DataStructures.ChangeConfig;
import nowrek.newsfilter.DataStructures.PageConfigData;
import nowrek.newsfilter.DataStructures.URLHandle;
import nowrek.newsfilter.UI.ScreenSlidePagerAdapter;
import nowrek.newsfilter.Utils.ConfigChangeListener;
import nowrek.newsfilter.WorkerThreads.PageDownloadTask;

public class SlidingActivity extends AppCompatActivity implements ConfigChangeListener {
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private AppConfigData appConfigData;
    private static final String CONFIG_FILE_NAME = "appConfig.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen_slide);
        fileCheck(CONFIG_FILE_NAME);
        getConfiguration();
        appConfigData.registerChangeListener(this);

        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onConfigChange(Collection<ChangeConfig> changeList) {
        saveJSONFile(CONFIG_FILE_NAME);
        getConfiguration();
        downloadPages();

    }

    private void downloadPages() {
        pagerAdapter.clearPages();

        LinkedList<URLHandle> urlHandles = new LinkedList<>();

        for (PageConfigData page : appConfigData.getPageList()) {
            URLHandle pageUrlHandle = new URLHandle(page.getPageUrl());
            urlHandles.add(pageUrlHandle);
        }

        try {
            Log.v("CONCURRENT", "RUNNING NEW PAGE DOWNLOAD TASK!");
            PageDownloadTask pageDownloadTask = new PageDownloadTask(this);
            pageDownloadTask.execute(urlHandles);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }



    public void displayArticles(LinkedList<Page> pages) {

        for (Page page : pages) {

            pagerAdapter.addPage(page.getPageOrigin().getUrl(), page.getContent());
        }

        if (this.pagerAdapter != null)
            this.pagerAdapter.notifyDataSetChanged();
    }

    public AppConfigData getConfiguration() {
        if (appConfigData == null) {
            appConfigData = new AppConfigData(getJSONFile(CONFIG_FILE_NAME));
        }
        return appConfigData;
    }

    private JSONObject getJSONFile(String fileName) {
        JSONObject jsonFile = new JSONObject();
        try {
            jsonFile = new JSONObject(readFile(fileName));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jsonFile;
    }

    private boolean saveJSONFile(String fileName) {
        try {
            overWriteFile(fileName, appConfigData.toJsonObject().toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void overWriteFile(String fileName, String content) throws IOException {
        deleteFile(fileName);
        //fileCheck(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(openFileOutput(fileName, MODE_PRIVATE)));
        bufferedWriter.write(content);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private String readFile(String fileName) throws IOException {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
        StringBuilder stringBuilder = new StringBuilder();
        String line = fileReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = fileReader.readLine();
        }
        fileReader.close();
        return stringBuilder.toString();
    }

    private boolean fileCheck(String fileName) {
        File file = new File(getFilesDir(), fileName);
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
