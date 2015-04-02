package audiovisio;

import audiovisio.entities.Button;
import audiovisio.entities.Door;
import audiovisio.entities.Lever;
import audiovisio.level.*;
import audiovisio.level.triggers.EndTrigger;

/**
 * @author Matt Gerst
 */
public class Items {
    /**
     * Add our level items to the Level Registry.
     */
    public static void init(){
        LevelRegistry.registerItem("panel", Panel.class);
        LevelRegistry.registerItem("stair", Stair.class);
        LevelRegistry.registerItem("door", Door.class);
        LevelRegistry.registerItem("button", Button.class);
        LevelRegistry.registerItem("lever", Lever.class);
        LevelRegistry.registerItem("wall", Wall.class);

        LevelRegistry.registerItem("trigger", Trigger.class);
        LevelRegistry.registerItemSubType("trigger", "end", EndTrigger.class);
    }
}
