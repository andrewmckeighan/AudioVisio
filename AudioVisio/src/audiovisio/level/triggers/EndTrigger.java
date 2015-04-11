package audiovisio.level.triggers;

import audiovisio.AudioVisio;
import audiovisio.entities.Player;
import audiovisio.level.Trigger;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.utils.LogHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

/**
 * @author Matt Gerst
 */
public class EndTrigger extends Trigger {

    public EndTrigger(){
        super();
    }

    public EndTrigger( Vector3f location ){
        super(location);
    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);

        codeObj.put("subtype", "end");
    }

    @Override
    public LevelNode getLevelNode(){
        LevelNode parent = super.getLevelNode();
        parent.setUserObject(String.format("#%d end @ %s", this.ID, this.location));

        LevelNode subType = new LevelNode("Sub-Type", "end", true);
        parent.insert(subType, 1);

        return parent;
    }

    @Override
    public void collide( Player player, AudioVisio av ){
        av.stopGame();
    }
}
