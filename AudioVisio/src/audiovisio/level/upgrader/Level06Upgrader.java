package audiovisio.level.upgrader;

import audiovisio.level.Level;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.VersionString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Matt Gerst
 */
public class Level06Upgrader implements ILevelUpgrader {
    VersionString version = new VersionString("0.6");

    @Override
    public void upgrade( JSONObject obj ){
        obj.put(Level.KEY_FORMAT, "0.6");

        JSONArray levelData = (JSONArray) obj.get(Level.KEY_LEVEL_DATA);

        for (Object item : levelData){
            JSONObject itemObj = (JSONObject) item;

            itemObj.put(JSONHelper.KEY_COLOR, "lightGrey");
        }
    }

    @Override
    public VersionString newVersion(){
        return this.version;
    }
}
