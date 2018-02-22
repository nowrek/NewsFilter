package nowrek.newsfilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.LinkedList;

import nowrek.newsfilter.DataStructures.URLHandle;
import nowrek.newsfilter.Utils.CppLibrariesLoader;
import nowrek.newsfilter.WorkerThreads.PageDownloadTask;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    private CppLibrariesLoader cppLibrariesLoader = new CppLibrariesLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        cppLibrariesLoader.loadCppLibaries();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent1 = new Intent(this, SlidingActivity.class);
        startActivity(intent1);

        /*
        Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        */

        testUrlDownload();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    /**
     *  Temporary function to test URL downloads
     */
    private void testUrlDownload() {
        URLHandle testUrl = new URLHandle("http://www.google.com");
        LinkedList<URLHandle> testUrlList= new LinkedList<>();
        testUrlList.add(testUrl);

        new Thread(new PageDownloadTask(testUrlList)).start();

    }
}
