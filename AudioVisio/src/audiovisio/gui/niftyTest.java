package audiovisio.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class niftyTest extends SimpleApplication implements ScreenController {

    @Override
    public void simpleInitApp() {
    	initMain();
    }
    
    public void initMain(){
    	NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        flyCam.setDragToRotate(true);
     
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
     
        // <screen>
        nifty.addScreen("main_menu", new ScreenBuilder("Hello Nifty Screen"){{
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
                         visibleToMouse(true);
                         interactOnClick(System.out.println("Clicked Leave game"));
                     }});
                     
                                   
      
                 }});
                
                
                // </panel>
              }});
            // </layer>
          }}.build(nifty));
        // </screen>
     
        nifty.gotoScreen("main_menu"); // start the screen
    }

	@Override
	public void bind(Nifty arg0, Screen arg1) {
		throw new UnsupportedOperationException("Not supported yet."); 
		
	}

	@Override
	public void onEndScreen() {
		throw new UnsupportedOperationException("Not supported yet."); 
		
	}

	@Override
	public void onStartScreen() {
		throw new UnsupportedOperationException("Not supported yet."); 
		
	}
    
}