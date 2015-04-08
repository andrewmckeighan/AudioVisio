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

    // LOAD

    @Test
    public void testLoad(){
        JSONObject obj = new JSONObject();

        /*
         * I am creating the location object manually because
         * load expects to get a type of double back from the
         * json, but the vector is floats. Since the values
         * don't leave memory, they don't get converted. We
         * may want to look into changing the helper methods
         * to force the conversion from vector3f to jsonobject
         * cast the values to doubles.
         */
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

    @Test(expected = IllegalArgumentException.class)
    public void testNullLoad(){
        panel.load(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLoad(){
        JSONObject obj = new JSONObject();
        panel.load(obj);
    }

    // SAVE

    @Test
    public void testSave(){
        panel.location = location;
        panel.setID(1L);

        JSONObject obj = new JSONObject();
        panel.save(obj);

        assertEquals(1L, obj.get(JSONHelper.KEY_ID));
        assertEquals("panel", obj.get(JSONHelper.KEY_TYPE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNulLSave(){
        panel.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonEmptySave(){
        JSONObject obj = new JSONObject();
        obj.put(JSONHelper.KEY_ID, 1L);
        panel.save(obj);
    }
}