package audiovisio.rsle.editor.extensions;

import audiovisio.rsle.RSLE;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Matt Gerst
 */
public interface IRSLEExtension extends ActionListener {
    void init(RSLE rsle);

    void registerMenuItems();

    void onCompletion(String action, String type, String subtype);
}
