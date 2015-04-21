package audiovisio.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.nio.file.Files;

public class FileUtils {
    public static final String json = "json";
    /**
     * The location of the data directory. This directory
     * contains files related to an specific instance of
     * AudioVisio. This includes levels, user data, and
     * similar things.
     */
    public static String dataDir;

    /**
     * The location of the level directory. This directory
     * contains all the level files that AudioVisio knows
     * about.
     */
    public static String levelDir;

    /**
     * The location of the metadata directory. This directory
     * contains mainly configuration files as well as files
     * containing user data.
     */
    public static String metaDir;

    private static final String[] DEFAULT_LEVELS = {"demo_level.json", "differentLocationPuzzle.json",
            "ItemJugglePuzzleEasy.json", "ItemJugglePuzzleHard.json", "relativeLocationPuzzle.json"};
    private static final String[] META_FILES = {"config.json"};

    /**
     * Load a json file from disk. This takes in a
     * reference to a file and returns the JSONObject
     * representing its contents.
     *
     * @param file The file to load
     *
     * @return The jsonobject found, null otherwise
     */
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
     * Save a json file to disk. This takes in a
     * reference to a file and the JSON object to
     * save.
     *
     * @param file The file to save to
     * @param object The object to save
     */
    public static void saveJSONFile( File file, JSONObject object ){
        try{
            FileWriter saveFile = new FileWriter(file);
            object.writeJSONString(saveFile);
            saveFile.flush();
            saveFile.close();
        } catch (IOException e){
            LogHelper.severe("There was an error saving the file", e);
        }
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

    /**
     * A file filter for the level file types.
     */
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

    /**
     * Get a File object that points to the data directory
     *
     * @return The data directory object
     */
    public static File getDataDirectory(){
        String directory;
        if (System.getProperty("os.name").toUpperCase().contains("WIN")){
            directory = System.getenv("AppData");
            directory += "/AudioVisio";
        } else {
            directory = System.getProperty("user.home");
            directory += "/.audiovisio";
        }

        return new File(directory);
    }

    /**
     * Get the File object that points to the location of
     * the level directory.
     *
     * @return The level directory object
     */
    public static File getLevelDirectory(){
        return new File(FileUtils.levelDir);
    }

    /**
     * Get the File object that points to the location of
     * the metadata directory.
     *
     * @return The meta directory object
     */
    public static File getMetaDirectory(){
        return new File(FileUtils.metaDir);
    }

    /**
     * Get a metadata file
     *
     * @param file The file to get a reference to
     *
     * @return The requested file
     */
    public static File getMetaFile( String file ){
        return new File(FileUtils.metaDir + "/" + file);
    }

    /**
     * Get a level file
     *
     * @param file The file to get a reference t
     *
     * @return The requested level file
     */
    public static File getLevelFile( String file ){
        return new File(FileUtils.levelDir + "/" + file);
    }

    /**
     * Perform a sanity check on the data directory. This will
     * attempt to create the data directory structure if it
     * does not already exist.
     *
     * @return true if successful, false otherwise
     */
    public static boolean dataDirectorySanityCheck(){
        File dataDir = FileUtils.getDataDirectory();
        FileUtils.dataDir = dataDir.toString();
        FileUtils.levelDir = dataDir + "/levels";
        FileUtils.metaDir = dataDir + "/data";

        if (dataDir.exists() && !dataDir.isDirectory()){
            LogHelper.warn("Data directory is not a directory");
            return false;
        } else if (!dataDir.exists()){
            if (!dataDir.mkdir()){
                LogHelper.warn("There was an error creating the data directory");
                return false;
            }
        }

        File lvl = new File(FileUtils.levelDir);

        if (!lvl.exists() && !lvl.isDirectory()){
            if (!lvl.mkdir()){
                LogHelper.warn("Could not create level directory");
                return false;
            }
        }
        FileUtils.populateDefaultLevels(lvl);

        File meta = new File(FileUtils.metaDir);

        if (!meta.exists() && !meta.isDirectory()){
            if (!meta.mkdir()){
                LogHelper.warn("Could not create meta directory");
                return false;
            }
        }
        FileUtils.populateDefaultMeta(meta);

        return true;
    }

    /**
     * Populate the level directory with the default levels
     *
     * @param levelDir A reference to the level directory object
     */
    private static void populateDefaultLevels( File levelDir ){
        File lvlFile, dest;
        for (String level : FileUtils.DEFAULT_LEVELS){
            lvlFile = new File(FileUtils.class.getResource("/Default/Levels/" + level).getPath());
            dest = new File(levelDir + "/" + level);

            FileUtils.createFileIfNotExists(lvlFile, dest);
        }
    }

    /**
     * Populate the metadata directory with the default levels
     *
     * @param metaDir A reference to the metadata directory object
     */
    private static void populateDefaultMeta( File metaDir ){
        File metaFile, dest;
        for (String file : FileUtils.META_FILES){
            metaFile = new File(FileUtils.class.getResource("/Default/Configuration/" + file).getPath());
            dest = new File(metaDir + "/" + file);

            FileUtils.createFileIfNotExists(metaFile, dest);
        }
    }

    private static void createFileIfNotExists( File toCheck, File toCreate ){
        if (!toCreate.exists()){
            try{
                LogHelper.info("Creating file " + toCreate);
                Files.copy(toCheck.toPath(), toCreate.toPath());
            } catch (IOException e){
                LogHelper.warn("There was an error populating the default metadata", e);
            }
        }
    }
}
