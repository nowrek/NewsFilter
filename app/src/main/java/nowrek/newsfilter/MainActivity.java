package nowrek.newsfilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import nowrek.newsfilter.DataStructures.AppConfigData;
import nowrek.newsfilter.DataStructures.URLHandle;
import nowrek.newsfilter.Utils.CppLibrariesLoader;
import nowrek.newsfilter.WorkerThreads.PageDownloadTask;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    private CppLibrariesLoader cppLibrariesLoader = new CppLibrariesLoader();


    public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        cppLibrariesLoader.loadCppLibaries();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent1 = new Intent(this, SlidingActivity.class);
        startActivity(intent1);

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            enableSSLSocket();
        } catch (Exception exception ){
            exception.printStackTrace();
            finish();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    /**
     * Temporary function to test URL downloads
     */
    /*
    private void testUrlDownload() {
        URLHandle testUrl = new URLHandle("http://www.google.com");
        LinkedList<URLHandle> testUrlList = new LinkedList<>();
        testUrlList.add(testUrl);

        new Thread(new PageDownloadTask(testUrlList)).start();

    }
   */
}
