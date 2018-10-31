package nowrek.newsfilter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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
import java.util.concurrent.BlockingQueue;

import nowrek.newsfilter.DataStructures.AppConfigData;
import nowrek.newsfilter.DataStructures.Article;
import nowrek.newsfilter.DataStructures.Page;
import nowrek.newsfilter.DataStructures.ChangeConfig;
import nowrek.newsfilter.UI.ScreenSlidePagerAdapter;
import nowrek.newsfilter.Utils.ConfigChangeListener;
import nowrek.newsfilter.WorkerThreads.NFThreadFactory;
import nowrek.newsfilter.WorkerThreads.NFWorkQueue;

public class SlidingActivity extends AppCompatActivity implements ConfigChangeListener {
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private AppConfigData appConfigData;
    private static final String CONFIG_FILE_NAME = "appConfig.json";
    private BlockingQueue<Runnable> workQueue;
    private NFThreadFactory threadPool;
    private Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen_slide);
        fileCheck(CONFIG_FILE_NAME);
        setUpConfigData();
        setUpPager();
        setUpThreadStructure();
    }

    @Override
    public void onConfigChange(Collection<ChangeConfig> changeList) {
        saveJSONFile(CONFIG_FILE_NAME);
        setUpConfigData();
        downloadPages();

    }

    private void downloadPages() {
        pagerAdapter.clearPages();
        pagerAdapter.setPages(appConfigData.getURLListAsString());
        threadPool.executePageDownloadTasks(appConfigData.getURLList());
    }

    private void setUpConfigData(){
        if(appConfigData != null) appConfigData.unregisterChangeListener(this);
        appConfigData = new AppConfigData(getJSONFile(CONFIG_FILE_NAME));
        appConfigData.registerChangeListener(this);
    }

    private void setUpThreadStructure(){
        uiHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(Article.class.isInstance(msg.obj)){
                    Article article = (Article) msg.obj;
                    pagerAdapter.addArticle(article.getArticleOrigin().getUrl(), article.getContent());
                }
            }
        };
        workQueue = new NFWorkQueue();
        threadPool = new NFThreadFactory(uiHandler, workQueue,3);
    }

    private void setUpPager(){
        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    public AppConfigData getConfiguration() {
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
