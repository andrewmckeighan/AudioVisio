package audiovisio;

import audiovisio.level.LevelRegistry;
import audiovisio.rsle.editor.extensions.entities.BoxExtension;
import audiovisio.rsle.editor.extensions.entities.ButtonExtension;
import audiovisio.rsle.editor.extensions.entities.DoorExtension;
import audiovisio.rsle.editor.extensions.entities.LeverExtension;
import audiovisio.rsle.editor.extensions.panels.PanelExtension;
import audiovisio.rsle.editor.extensions.panels.StairExtension;
import audiovisio.rsle.editor.extensions.panels.WallExtension;
import audiovisio.rsle.editor.extensions.triggers.TriggerExtension;

/**
 * @author Matt Gerst
 */
public class RSLEExtensions {
    public static void init(){
        LevelRegistry.registerItemRSLEExtension("panel", PanelExtension.class);
        LevelRegistry.registerItemRSLEExtension("stair", StairExtension.class);
        LevelRegistry.registerItemRSLEExtension("wall", WallExtension.class);

        LevelRegistry.registerItemRSLEExtension("trigger", TriggerExtension.class);

        LevelRegistry.registerItemRSLEExtension("box", BoxExtension.class);
        LevelRegistry.registerItemRSLEExtension("button", ButtonExtension.class);
        LevelRegistry.registerItemRSLEExtension("door", DoorExtension.class);
        LevelRegistry.registerItemRSLEExtension("lever", LeverExtension.class);
    }
}
