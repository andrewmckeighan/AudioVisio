package audiovisio.level;

import audiovisio.level.upgrader.LevelVersions;
import audiovisio.utils.FileUtils;
import audiovisio.utils.LogHelper;
import audiovisio.utils.VersionString;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Responsible for loading/saving level files. Also manages the internal
 * list of level files.
 *
 * @author Matt Gerst
 */
public class LevelLoader {
    private static Map<String, Level> levels = new HashMap<String, Level>();

    /**
     * Read the given filename into a level
     *
     * @param filename The file to read
     *
     * @return The level to read
     */
    public static Level read( String filename ){
        return LevelLoader.read(new File(filename));
    }

    /**
     * Read the given file into a Level
     *
     * @param file The file to read
     *
     * @return The level to read
     */
    public static Level read( File file ){
        JSONObject obj = FileUtils.loadJSONFile(file);

        LevelVersions.init();

        if (obj.get(Level.KEY_FORMAT) == null){
            // Assume an unversion file is v0.2
            obj.put(Level.KEY_FORMAT, "0.2");
        }

        VersionString formatVersion = new VersionString((String) obj.get(Level.KEY_FORMAT));
        if (Level.CURRENT_LEVEL_FORMAT.compareTo(formatVersion) > 0){
            LevelVersions.upgrade(formatVersion.getVersion(), obj);
            LevelLoader.writeJson(obj, file);
        }

        return new Level(obj, file.getName());
    }

    /**
     * Write the given level to the file specified by the level.
     *
     * @param level The level to write
     */
    public static void write( Level level ){
        File levelFile = new File(level.getFileName());
        levelFile.setReadable(true);
        levelFile.setWritable(true);
        level.saveLevel();
        LevelLoader.writeJson(level.levelData, levelFile);
    }

    /**
     * Write a JSONObject to a file.
     *
     * @param obj  The JSONObject to write
     * @param file The file to write to
     */
    public static void writeJson( JSONObject obj, File file ){
        try{
            FileWriter saveFile = new FileWriter(file);
            obj.writeJSONString(saveFile);
            saveFile.flush();
            saveFile.close();
        } catch (IOException e){
            LogHelper.severe("Could not save level file", e);
        }
    }

    public static void initLevelList(){
        File levelDir = FileUtils.getLevelDirectory();

        File levels[] = levelDir.listFiles();
        if (levels == null) {
            LogHelper.warn("No Level Files Found");
        }
        for (File level : levels){
            Level lvl = read(level);

            LevelLoader.levels.put(lvl.getName(), lvl);
        }
    }

    public static Map<String, Level> getLevelList(){
        return LevelLoader.levels;
    }

    public static Set<String> getLevelNames(){
        return LevelLoader.levels.keySet();
    }

    public static void addLevel( Level level ){
        LevelLoader.levels.put(level.getName(), level);
    }

    public static Level getLevel( String name ){
        return LevelLoader.levels.get(name);
    }
}
