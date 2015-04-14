package audiovisio.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {
    public static final String json = "json";
    public static String dataDir;
    public static String levelDir;
    public static String metaDir;

    public static JSONObject loadJSONFile( File file ){
        JSONParser parser = new JSONParser();

        FileReader fileReader;
        JSONObject obj = null;

        try{
            fileReader = new FileReader(file);

            obj = (JSONObject) parser.parse(fileReader);
            fileReader.close();
        } catch (FileNotFoundException ex){
            LogHelper.warn("Bad JSON file", ex);
        } catch (IOException ex){
            LogHelper.warn("Bad JSON file", ex);
        } catch (ParseException ex){
            LogHelper.warn("Error parsing JSON file", ex);
        }

        return obj;
    }

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

    public static File getDataDirectory(){
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

        return new File(directory);
    }

    public static File getLevelDirectory(){
        return new File(FileUtils.levelDir);
    }

    public static File getMetaDirectory(){
        return new File(FileUtils.metaDir);
    }

    public static File getMetaFile( String file ){
        return new File(FileUtils.metaDir + "/" + file);
    }

    public static File getLevelFile( String file ){
        return new File(FileUtils.levelDir + "/" + file);
    }

    public static boolean dataDirectorySanityCheck(){
        File dataDir = getDataDirectory();
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

                if (!meta.exists() && !meta.isDirectory()){
                    if (!meta.mkdir()){
                        LogHelper.warn("Could not create meta directory");
                        return false;
                    }

                    populateDefaultMeta(meta);
                }

            } else {
                LogHelper.warn("There was an error creating the data directory");
                return false;
            }
        }

        return true;
    }

    private static void populateDefaultLevels(File levelDir){
        File demo_level = new File(FileUtils.class.getResource("/Default/Levels/demo_level.json").getPath());
        File dest = new File(levelDir.toString() + "/demo_level.json");

        try{
            Files.copy(demo_level.toPath(), dest.toPath());
        } catch (IOException e){
            LogHelper.warn("There was an error populating the default levels", e);
        }
    }

    private static void populateDefaultMeta(File metaDir){
        File defaultConfig = new File(FileUtils.class.getResource("/Default/Configuration/config.json").getPath());
        File dest = new File(metaDir.toString() + "/config.json");

        try{
            Files.copy(defaultConfig.toPath(), dest.toPath());
        } catch (IOException e){
            LogHelper.warn("There was an error populating the default metadata", e);
        }
    }
}
