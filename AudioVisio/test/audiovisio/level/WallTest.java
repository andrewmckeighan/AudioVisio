package audiovisio.level;

import audiovisio.utils.JSONHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Matt Gerst
 */
public class WallTest {
    Wall wall;
    Vector3f location = new Vector3f(1F, 2F, 3F);

    @Before
    public void setUp(){
        wall = new Wall(location, ILevelItem.Direction.NORTH);
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
        obj.put(Wall.KEY_EDGE, ILevelItem.Direction.EAST.toString());

        wall.load(obj);

        assertEquals(1L, wall.getID());
        assertEquals(location, wall.location);
        assertEquals(ILevelItem.Direction.EAST, wall.getDirection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLoad(){
        wall.load(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLoad(){
        JSONObject obj = new JSONObject();

        wall.load(obj);
    }

    // SAVE

    @Test
    public void testSave(){
        wall.setID(1L);

        JSONObject obj = new JSONObject();
        wall.save(obj);

        assertEquals(1L, obj.get(JSONHelper.KEY_ID));
        assertTrue(obj.containsKey(JSONHelper.KEY_LOCATION));
        assertEquals("wall", obj.get(JSONHelper.KEY_TYPE));
        assertEquals(ILevelItem.Direction.NORTH.toString(), obj.get(Wall.KEY_EDGE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSave(){
        wall.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonEmptySave(){
        JSONObject obj = new JSONObject();
        obj.put("test", "invalid");

        wall.save(obj);
    }
}