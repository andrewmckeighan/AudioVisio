package audiovisio.level;

import audiovisio.utils.JSONHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Matt Gerst
 */
public class TriggerTest {
    Trigger trigger;
    Vector3f location = new Vector3f(1F, 2F, 3F);

    @Before
    public void setUp() throws Exception{
        trigger = new Trigger(location);
    }

    // LOAD

    @Test
    public void testLoad() throws Exception{
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

        obj.put(JSONHelper.KEY_ID, 3L);
        obj.put(JSONHelper.KEY_LOCATION, loc);

        trigger.load(obj);

        assertEquals(3L, trigger.getID());
        assertEquals(location, trigger.location);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLoad(){
        trigger.load(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLoad(){
        JSONObject obj = new JSONObject();
        trigger.load(obj);
    }

    // SAVE

    @Test
    public void testSave(){
        trigger.setID(3L);

        JSONObject obj = new JSONObject();
        trigger.save(obj);

        assertEquals(3L, obj.get(JSONHelper.KEY_ID));
        assertEquals("trigger", obj.get(JSONHelper.KEY_TYPE));
        assertTrue(obj.containsKey(JSONHelper.KEY_LOCATION));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSave(){
        trigger.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonEmptySave(){
        JSONObject obj = new JSONObject();
        obj.put("test", "invalid");

        trigger.save(obj);
    }
}