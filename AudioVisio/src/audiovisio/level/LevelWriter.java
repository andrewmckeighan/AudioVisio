package audiovisio.level;

import audiovisio.utils.LogHelper;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes the level out to a JSON file.
 */
public class LevelWriter {

    public static void write( Level level ){
        File levelFile = new File(level.getFileName());
        levelFile.setReadable(true);
        levelFile.setWritable(true);
        level.saveLevel();
        LevelWriter.writeJson(level.levelData, levelFile);
    }

    protected static void writeJson( JSONObject obj, File file ){
        try{
            FileWriter saveFile = new FileWriter(file);
            obj.writeJSONString(saveFile);
            saveFile.flush();
            saveFile.close();
        } catch (IOException e){
            LogHelper.severe("Could not save level file", e);
        }
    }

}