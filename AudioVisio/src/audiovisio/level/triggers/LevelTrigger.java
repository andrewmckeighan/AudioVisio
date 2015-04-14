package audiovisio.level.triggers;

import audiovisio.AudioVisio;
import audiovisio.entities.Player;
import audiovisio.level.Trigger;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.rsle.editor.RSLESetter;
import audiovisio.utils.LogHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

/**
 * A trigger that loads another level when activated.
 * This is intended to be used for a series of levels
 * that are intended to be completed together.
 *
 * @author Matt Gerst
 */
public class LevelTrigger extends Trigger {
    public static final String KEY_LEVEL_FILE = "levelfile";

    private String levelFile;

    public LevelTrigger(){}

    public LevelTrigger( Vector3f loc ){
        this.location = loc;
    }

    @Override
    public void load( JSONObject loadObj ){
        super.load(loadObj);

        this.levelFile = (String) loadObj.get(KEY_LEVEL_FILE);
    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);

        codeObj.put("subtype", "level");
        codeObj.put(KEY_LEVEL_FILE, this.levelFile);
    }

    @Override
    public LevelNode getLevelNode(){
        LevelNode parent = super.getLevelNode();
        parent.setUserObject(String.format("#%d level @ %s", this.ID, this.location));

        LevelNode subType = new LevelNode("Sub-Type", "level", true);
        parent.insert(subType, 1);

        LevelNode levelFile = new LevelNode("Level File", this.levelFile, false);
        parent.insert(levelFile, 3);

        return parent;
    }

    @Override
    public void collide( Player player, AudioVisio av ){
        LogHelper.info("At some point this will load " + this.levelFile + " instead of logging");
    }

    @RSLESetter("Level File")
    public void setLevelFile( String levelFile ){
        this.levelFile = levelFile;
    }
}
