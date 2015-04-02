package audiovisio.level.triggers;

import audiovisio.level.Trigger;
import audiovisio.rsle.editor.LevelNode;
import org.json.simple.JSONObject;

/**
 * @author Matt Gerst
 */
public class TextTrigger extends Trigger {
    private String text;

    @Override
    public void load( JSONObject loadObj ){
        super.load(loadObj);

        text = (String) loadObj.get("text");
    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);

        codeObj.put("subtype", "text");
        codeObj.put("text", this.text);
    }

    @Override
    public LevelNode getLevelNode(){
        LevelNode parent = super.getLevelNode();
        parent.setUserObject(String.format("#%d text @ %s", this.ID, this.location));

        LevelNode subType = new LevelNode("Sub-Type", "text", true);
        parent.insert(subType, 1);

        LevelNode textNode = new LevelNode("Text", this.text, false);
        parent.insert(textNode, 3);

        return parent;
    }
}
