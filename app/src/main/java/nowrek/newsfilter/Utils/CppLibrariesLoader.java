package nowrek.newsfilter.Utils;

/**
 * Created by nowrek on 23.11.2017.
 */

public class CppLibrariesLoader {

    public CppLibrariesLoader() {
    }

    public void loadCppLibaries() {
        try {
            System.loadLibrary("native-lib");
        } catch (Exception exception) {
            System.out.println("Unable to load libraries.\n" + exception.getCause());
        }
    }

}
