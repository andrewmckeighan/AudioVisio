package audiovisio.level.upgrader;

import audiovisio.entities.InteractableEntity;
import audiovisio.level.ILevelItem;
import audiovisio.level.Level;
import audiovisio.level.LevelRegistry;
import audiovisio.utils.VersionString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Matt Gerst
 */
public class Level04Upgrader implements ILevelUpgrader {
    @Override
    public void upgrade( JSONObject obj ){
        obj.put(Level.KEY_FORMAT, "0.4");

        JSONArray level = (JSONArray) obj.get("level");

        for (Object oItem : level) {
            JSONObject item = (JSONObject) oItem;

            // This feels a little hacky...
            String type = (String) item.get("type");

            ILevelItem iT = LevelRegistry.getItemForType(type);
            if (iT instanceof InteractableEntity) {
                item.put(InteractableEntity.KEY_LINKED, new JSONArray());
            }
        }
    }

    @Override
    public VersionString newVersion(){
        return new VersionString("0.4");
    }
}
