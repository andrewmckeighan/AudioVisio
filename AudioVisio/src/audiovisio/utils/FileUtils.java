package audiovisio.utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileUtils {
    public final static String json = "json";

    /**
     * Get the extension of the file.
     * Taken from the oracle docs
     * http://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    public static class LevelFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = FileUtils.getExtension(f);
            if (extension != null) {
                if (extension.equals(FileUtils.json)) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        @Override
        public String getDescription() {
            return "JSON Files";
        }
    }
}
