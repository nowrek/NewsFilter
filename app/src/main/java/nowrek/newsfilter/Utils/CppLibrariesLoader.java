package nowrek.newsfilter.Utils;

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
