package audiovisio.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import audiovisio.utils.LogHelper;

public class LevelReader {

    public static Level read(String fileName){
    	return new Level(loadJsonFile(fileName));
    }

    /**
     * Loads a level from the specified JSON file
     * 
     * @param fileName The file to load
     * @return The json object from the file
     */
    private static JSONObject loadJsonFile(String fileName) {
    	JSONParser parser = new JSONParser();
    	
    	FileReader fileReader = null;
    	JSONObject obj = null;
    	
    	try {
    		fileReader = new FileReader(new File(fileName));
    	} catch (FileNotFoundException ex) {
    		LogHelper.warn("Bad JSON file");
    	}
    	
    	try {
    		obj = (JSONObject) parser.parse(fileReader);
    		fileReader.close();
    	} catch (IOException ex) {
    		LogHelper.warn("Bad JSON file");
    	} catch (ParseException ex) {
    		LogHelper.warn("Bad JSON file Parser");
    	}
    	
    	return obj;
    }
}