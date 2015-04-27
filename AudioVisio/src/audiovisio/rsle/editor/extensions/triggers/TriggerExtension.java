package audiovisio.rsle.editor.extensions.triggers;

import audiovisio.level.triggers.EndTrigger;
import audiovisio.level.triggers.LevelTrigger;
import audiovisio.level.triggers.TextTrigger;
import audiovisio.rsle.RSLE;
import audiovisio.rsle.editor.components.LocationField;
import audiovisio.rsle.editor.dialogs.DefaultDialog;
import audiovisio.rsle.editor.extensions.IRSLEExtension;
import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Matt Gerst
 */
public class TriggerExtension implements IRSLEExtension {
    private RSLE rsle;
    private JMenuItem addEndTrigger;
    private JMenuItem addLevelTrigger;
    private JMenuItem addTextTrigger;

    @Override
    public void init(RSLE rsle) {
        this.rsle = rsle;
    }

    @Override
    public void registerMenuItems() {
        this.addEndTrigger = new JMenuItem("End Trigger");
        this.addEndTrigger.addActionListener(this);
        this.rsle.registerAddType(RSLE.ItemBucket.TRIGGERS, this.addEndTrigger);

        this.addLevelTrigger = new JMenuItem("Level Trigger");
        this.addLevelTrigger.addActionListener(this);
        this.rsle.registerAddType(RSLE.ItemBucket.TRIGGERS, this.addLevelTrigger);

        this.addTextTrigger = new JMenuItem("Text Trigger");
        this.addTextTrigger.addActionListener(this);
        this.rsle.registerAddType(RSLE.ItemBucket.TRIGGERS, this.addTextTrigger);
    }

    @Override
    public void onCompletion(String action, String type, String subtype) {
        if ("add_trigger".equals(action)){
            Vector3f location = this.location.getLocationVector();

            if ("end".equals(subtype)){
                EndTrigger trigger = new EndTrigger(location);
                this.rsle.addItem(trigger, RSLE.ItemBucket.TRIGGERS);
            } else if ("text".equals(subtype)){
                String text = this.text.getText();

                TextTrigger trigger = new TextTrigger(location);
                trigger.setText(text);
                this.rsle.addItem(trigger, RSLE.ItemBucket.TRIGGERS);
            } else if ("level".equals(subtype)){
                String levelField = this.levelFile.getText();

                LevelTrigger trigger = new LevelTrigger(location);
                trigger.setLevelFile(levelField);
                this.rsle.addItem(trigger, RSLE.ItemBucket.TRIGGERS);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addEndTrigger){
            DefaultDialog dialog = this.rsle.getDialog("add_trigger", "trigger", "end");
            dialog.setTitle("New End Trigger");

            this.location = new LocationField();
            dialog.addRow("Location:", this.location);

            dialog.showDialog();
        } else if (e.getSource() == this.addTextTrigger){
            DefaultDialog dialog = this.rsle.getDialog("add_trigger", "trigger", "text");
            dialog.setTitle("New Text Trigger");

            this.text = new JTextField();
            dialog.addRow("Text:", this.text);

            this.location = new LocationField();
            dialog.addRow("Location:", this.location);

            dialog.showDialog();
        } else if (e.getSource() == this.addLevelTrigger){
            DefaultDialog dialog = this.rsle.getDialog("add_trigger", "trigger", "level");
            dialog.setTitle("New Level Trigger");

            this.levelFile = new JTextField();
            dialog.addRow("Level File:", this.levelFile);

            this.location = new LocationField();
            dialog.addRow("Location:", this.location);

            dialog.showDialog();
        }
    }

    private LocationField location  = new LocationField();
    private JTextField    text      = new JTextField();
    private JTextField    levelFile = new JTextField();
}
