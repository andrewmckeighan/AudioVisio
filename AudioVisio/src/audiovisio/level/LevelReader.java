package audiovisio.level;

import audiovisio.level.upgrader.LevelVersions;
import audiovisio.utils.LogHelper;
import audiovisio.utils.VersionString;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LevelReader {

    /**
     * Read the given filename into a Level
     *
     * @param filename The file to read
     *
     * @return The level to read
     */
    public static Level read( String filename ){
        return LevelReader.read(new File(filename));
    }

    /**
     * Read the given file into a Level
     *
     * @param file The file to read
     *
     * @return The level to read
     */
    public static Level read( File file ){
        return new Level(LevelReader.loadJsonFile(file), file.getName());
    }

    /**
     * Loads a level from the specified JSON file
     *
     * @param file The file to load
     *
     * @return The json object from the file
     */
    public static JSONObject loadJsonFile( File file ){
        JSONParser parser = new JSONParser();

        FileReader fileReader;
        JSONObject obj = null;

        try{
            fileReader = new FileReader(file);

            obj = (JSONObject) parser.parse(fileReader);
            fileReader.close();

            LevelVersions.init();

            if (obj.get(Level.KEY_FORMAT) == null) {
                // Assume the default is ver 0.2
                obj.put(Level.KEY_FORMAT, "0.2");
            }

            VersionString formatVersion = new VersionString((String) obj.get(Level.KEY_FORMAT));
            if (Level.CURRENT_LEVEL_FORMAT.compareTo(formatVersion) > 0){
                LevelVersions.upgrade(formatVersion.getVersion(), obj);
                LevelWriter.writeJson(obj, file);
            }
        } catch (FileNotFoundException ex){
            LogHelper.warn("Bad JSON file", ex);
        } catch (IOException ex){
            LogHelper.warn("Bad JSON file", ex);
        } catch (ParseException ex){
            LogHelper.warn("Bad JSON file Parser", ex);
        }

        return obj;
    }
}