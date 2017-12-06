package nowrek.newsfilter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import nowrek.newsfilter.Utils.CppLibrariesLoader;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    private CppLibrariesLoader cppLibrariesLoader = new CppLibrariesLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        cppLibrariesLoader.loadCppLibaries();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
