package audiovisio.level.triggers;

import audiovisio.level.Trigger;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.rsle.editor.RSLESetter;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

/**
 * @author Matt Gerst
 */
public class TextTrigger extends Trigger {
    private String text;

    public TextTrigger(){
        super();
    }

    public TextTrigger( Vector3f loc ){
        super(loc);
    }

    @Override
    public void load( JSONObject loadObj ){
        super.load(loadObj);

        this.text = (String) loadObj.get("text");
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

    @RSLESetter("Text")
    public void setText( String text ){
        this.text = text;
    }
}
