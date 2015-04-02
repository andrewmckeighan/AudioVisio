package audiovisio.level.triggers;

import audiovisio.level.Trigger;
import audiovisio.rsle.editor.LevelNode;
import org.json.simple.JSONObject;

/**
 * @author Matt Gerst
 */
public class EndTrigger extends Trigger {
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
}
