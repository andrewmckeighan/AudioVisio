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
            childLayoutHorizontal(); // layer properties, add more...
            //childLayoutVertical();
            //childLayoutCenter();
            // <panel>
            panel(new PanelBuilder("panel_bottom_left") {{
               childLayoutCenter(); // panel properties, add more...
               valignCenter();
               backgroundColor("#FFFFFF");
               height("33%");
               width("33%");

 
                // GUI elements
                control(new ButtonBuilder("Button_ID", "Does Your"){{
                    alignCenter();
                    valignCenter();
                    height("50%");
                    width("50%");
                }});
                
                //.. add more GUI elements here              
 
            }});
            
            panel(new PanelBuilder("panel_bottom_mid") {{
                childLayoutCenter(); // panel properties, add more...
                valignCenter();
                backgroundColor("#FFF555");
                height("33%");
                width("33%");

  
                 // GUI elements
                 control(new ButtonBuilder("Button_ID", "Chain Hang"){{
                     alignCenter();
                     valignCenter();
                     height("50%");
                     width("50%");
                 }});
                 
                 //.. add more GUI elements here              
  
             }});
            
            panel(new PanelBuilder("panel_bottom_right") {{
                childLayoutCenter(); // panel properties, add more...
                backgroundColor("#88f8");
                alignRight();
                valignCenter();
                height("33%");
                width("33%");
                 // GUI elements
                 control(new ButtonBuilder("Button_ID222", "Low?"){{
                     alignCenter();
                     valignCenter();
                     height("50%");
                     width("50%");
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