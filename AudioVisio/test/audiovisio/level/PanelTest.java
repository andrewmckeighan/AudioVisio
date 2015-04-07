package audiovisio.level;

import audiovisio.utils.JSONHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PanelTest {
    Panel panel;
    Vector3f location = new Vector3f(1F, 2F, 3F);

    @Before
    public void setUp(){
        panel = new Panel();
    }

    @Test
    public void testSetupWithPosition(){
        panel = new Panel(location);

        assertEquals(location, panel.location);
    }

    @Test
    public void testLoad(){
        JSONObject obj = new JSONObject();
        JSONObject loc = new JSONObject();
        loc.put(JSONHelper.KEY_LOCATION_X, 1D);
        loc.put(JSONHelper.KEY_LOCATION_Y, 2D);
        loc.put(JSONHelper.KEY_LOCATION_Z, 3D);

        obj.put(JSONHelper.KEY_ID, 1L);
        obj.put(JSONHelper.KEY_LOCATION, loc);

        panel.load(obj);

        assertEquals(1L, panel.getID());
        assertEquals(location, panel.location);
    }
}