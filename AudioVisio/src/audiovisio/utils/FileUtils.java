package audiovisio.utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {
    public static final String json = "json";
    public static String dataDir;
    public static String levelDir;
    public static String metaDir;

    /**
     * Get the extension of the file.
     * Taken from the oracle docs
     * http://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
     */
    public static String getExtension( File f ){
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1){
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static class LevelFileFilter extends FileFilter {

        @Override
        public boolean accept( File f ){
            if (f.isDirectory()){
                return true;
            }

            String extension = FileUtils.getExtension(f);
            if (extension != null){
                return extension.equals(FileUtils.json);
            }

            return false;
        }

        @Override
        public String getDescription(){
            return "JSON Files";
        }
    }

    public static String getDataDirectory(){
        String directory;
        if(System.getProperty("os.name").toUpperCase().contains("WIN")){
            directory = System.getenv("AppData");
            directory += "/AudioVisio";
        }
        else
        {
            directory = System.getProperty("user.home");
            directory += "/.audiovisio";
        }

        return directory;
    }

    public static boolean dataDirectorySanityCheck(){
        File dataDir = new File(getDataDirectory());
        FileUtils.dataDir = dataDir.toString();
        FileUtils.levelDir = dataDir.toString() + "/levels";
        FileUtils.metaDir = dataDir.toString() + "/data";

        if (dataDir.exists() && dataDir.isDirectory()){
            LogHelper.fine("Data directory exists");
            return true;
        } else if (dataDir.exists() && !dataDir.isDirectory()){
            LogHelper.warn("Data directory is not a directory");
            return false;
        } else if (!dataDir.exists()){
            if(dataDir.mkdir()){
                File lvl = new File(FileUtils.levelDir);

                if (!lvl.exists() && !lvl.isDirectory()){
                    if (!lvl.mkdir()){
                        LogHelper.warn("Could not create level directory");
                        return false;
                    }

                    populateDefaultLevels(lvl);
                }

                File meta = new File(FileUtils.metaDir);

                if (!meta.mkdir()){
                    LogHelper.warn("Could not create meta directory");
                    return false;
                }

            } else {
                LogHelper.warn("There was an error creating the data directory");
                return false;
            }
        }

        return true;
    }

    public static void populateDefaultLevels(File levelDir){
        File demo_level = new File(FileUtils.class.getResource("/DefaultLevels/demo_level.json").getPath());
        File dest = new File(levelDir.toString() + "/demo_level.json");

        try{
            Files.copy(demo_level.toPath(), dest.toPath());
        } catch (IOException e){
            LogHelper.warn("There was an error populating the default levels", e);
        }
    }
}
