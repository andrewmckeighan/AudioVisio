package audiovisio.level;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import audiovisio.utils.LogHelper;

public class LevelWriter {

    public static void write(Level level){
    	try {
    		File levelFile = new File(level.getFileName());
    		levelFile.setReadable(true);
    		levelFile.setWritable(true);
    		
    		FileWriter saveFile = new FileWriter(levelFile);
    		level.levelData.writeJSONString(saveFile);
    		saveFile.flush();
    		saveFile.close();
    	} catch (IOException e) {
    		LogHelper.severe("Could not save level file", e);
    	}
    }

}