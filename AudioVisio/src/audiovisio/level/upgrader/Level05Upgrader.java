package audiovisio.level.upgrader;

import audiovisio.level.Level;
import audiovisio.utils.VersionString;
import org.json.simple.JSONObject;

/**
 * @author Matt Gerst
 */
public class Level05Upgrader implements ILevelUpgrader {
    VersionString version = new VersionString("0.5");

    @Override
    public void upgrade( JSONObject obj ){
        obj.put(Level.KEY_FORMAT, "0.5");

        JSONObject spawn = (JSONObject) obj.get(Level.KEY_SPAWN);

        JSONObject visual = (JSONObject) spawn.get(Level.KEY_VISUAL_SPAWN);
        double locX = (Double) visual.remove("x");
        double locY = (Double) visual.remove("y");
        double locZ = (Double) visual.remove("z");

        JSONObject location = new JSONObject();
        location.put("x", locX);
        location.put("y", locY);
        location.put("z", locZ);
        visual.put(Level.KEY_SPAWN_LOCATION, location);
        visual.put(Level.KEY_SPAWN_ROTATION, 0F);

        JSONObject audio = (JSONObject) spawn.get(Level.KEY_AUDIO_SPAWN);
        locX = (Double) audio.remove("x");
        locY = (Double) audio.remove("y");
        locZ = (Double) audio.remove("z");

        location = new JSONObject();
        location.put("x", locX);
        location.put("y", locY);
        location.put("z", locZ);
        audio.put(Level.KEY_SPAWN_LOCATION, location);
        audio.put(Level.KEY_SPAWN_ROTATION, 0F);
    }

    @Override
    public VersionString newVersion(){
        return this.version;
    }
}
