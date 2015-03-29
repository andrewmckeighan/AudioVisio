package audiovisio.level.upgrader;

import audiovisio.level.Level;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.VersionString;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

/**
 * Upgrade to format version 0.3
 *
 * Added fields
 * Level: format, spawn
 * Spawn: p1, p2 (each are location objects)
 *
 * @author Matt Gerst
 */
public class Level03Upgrader implements ILevelUpgrader {

    @Override
    public void upgrade( JSONObject obj ){
        // Add level format
        obj.put(Level.KEY_FORMAT, "0.3");

        // Add spawn
        JSONObject spawn = new JSONObject();
        spawn.put(Level.KEY_AUDIO_SPAWN, JSONHelper.saveVector3f(Vector3f.ZERO));
        spawn.put(Level.KEY_VISUAL_SPAWN, JSONHelper.saveVector3f(Vector3f.ZERO));

        obj.put(Level.KEY_SPAWN, spawn);
    }

    @Override
    public VersionString newVersion(){
        return new VersionString("0.3");
    }
}
