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
            childLayoutVertical();
            //childLayoutCenter();
            // <panel>
            panel(new PanelBuilder("panel_top") {{
               childLayoutCenter(); // panel properties, add more...
               alignCenter();
               backgroundColor("#FFFFFF");
               height("25%");
               width("100%");

 
                // GUI elements
                control(new ButtonBuilder("host", "Host Game"){{
                    alignCenter();
                    valignCenter();
                    height("50%");
                    width("50%");
                }});
                
                //.. add more GUI elements here              
 
            }});
            
            panel(new PanelBuilder("panel_top_mid") {{
                childLayoutCenter(); // panel properties, add more...
                alignCenter();
                backgroundColor("#FFFFFF");
                height("25%");
                width("100%");

  
                 // GUI elements
                 control(new ButtonBuilder("join", "Join Game"){{
                     alignCenter();
                     valignCenter();
                     height("50%");
                     width("50%");
                 }});
                 
                 
             }});
            panel(new PanelBuilder("panel_bottom_mid") {{
                childLayoutCenter(); // panel properties, add more...
                backgroundColor("#FFFFFF");
                alignCenter();
                height("25%");
                width("100%");
                 // GUI elements
                 control(new ButtonBuilder("sett", "Settings"){{
                     alignCenter();
                     valignCenter();
                     height("50%");
                     width("50%");
                 }});
                 
                               
  
             }});
            
            panel(new PanelBuilder("panel_bottom") {{
                childLayoutCenter(); // panel properties, add more...
                backgroundColor("#FFFFFF");
                alignCenter();
                height("25%");
                width("100%");
                 // GUI elements
                 control(new ButtonBuilder("Leave", "Leave Game"){{
                     alignCenter();
                     valignCenter();
                     height("50%");
                     width("50%");
                 }});
                 
                               
  
             }});
            
            
            // </panel>
          }});
        // </layer>
      }}.build(nifty));
    // </screen>
 
    nifty.gotoScreen("Screen_ID"); // start the screen
    }
}