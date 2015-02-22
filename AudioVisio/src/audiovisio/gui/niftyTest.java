package audiovisio.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;

public class niftyTest extends SimpleApplication {

    @Override
    public void simpleInitApp() {
    NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
            assetManager, inputManager, audioRenderer, guiViewPort);
    Nifty nifty = niftyDisplay.getNifty();
    guiViewPort.addProcessor(niftyDisplay);
    flyCam.setDragToRotate(true);
 
    nifty.loadStyleFile("nifty-default-styles.xml");
    nifty.loadControlFile("nifty-default-controls.xml");
 
    // <screen>
    nifty.addScreen("Screen_ID", new ScreenBuilder("Hello Nifty Screen"){{
        controller(new DefaultScreenController()); // Screen properties       
 
        // <layer>
        layer(new LayerBuilder("Layer_ID") {{
            childLayoutVertical(); // layer properties, add more...
 
            // <panel>
            panel(new PanelBuilder("Panel_ID") {{
               childLayoutCenter(); // panel properties, add more...               
 
                // GUI elements
                control(new ButtonBuilder("Button_ID", "Hello Nifty"){{
                    alignCenter();
                    valignCenter();
                    height("5%");
                    width("15%");
                }});
                control(new ButtonBuilder("Button_ID", "Exit"){{
                    alignLeft();
                    valignCenter();
                    height("5%");
                    width("15%");
                }});
                //.. add more GUI elements here              
 
            }});
            // </panel>
          }});
        // </layer>
      }}.build(nifty));
    // </screen>
 
    nifty.gotoScreen("Screen_ID"); // start the screen
    }
}