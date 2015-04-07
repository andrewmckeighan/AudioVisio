package audiovisio.level;

import audiovisio.level.triggers.EndTrigger;
import audiovisio.level.triggers.TextTrigger;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class LevelRegistryTest {
    @Before
    public void setUp(){
        LevelRegistry.registerItem("panel", Panel.class);
        LevelRegistry.registerItem("trigger", Trigger.class);
        LevelRegistry.registerItemSubType("trigger", "end", EndTrigger.class);
    }

    @Test
    public void testItemClass(){
        assertEquals(Panel.class, LevelRegistry.getItemClassForType("panel"));
    }

    @Test
    public void testItemInstance(){
        ILevelItem panel = LevelRegistry.getItemForType("panel");

        assertTrue(panel instanceof Panel);
    }

    @Test
    public void testItemSubType(){
        ILevelItem endTrigger = LevelRegistry.getItemForSubType("trigger", "end");

        assertTrue(endTrigger instanceof EndTrigger);
    }

    @Test
    public void testItemSubtypes(){
        Set<String> subTypes = LevelRegistry.getSubTypesForType("trigger");

        assertEquals(1, subTypes.size());
        assertTrue(subTypes.contains("end"));
    }

    @Test
    public void testTypeHasSubtypes(){
        assertTrue(LevelRegistry.typeHasSubTypes("trigger"));
        assertFalse(LevelRegistry.typeHasSubTypes("panel"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetItemForInvalidType(){
        LevelRegistry.getItemForType("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSubTypeForInvalidType(){
        LevelRegistry.getItemForSubType("invalid", "end");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSubTypeForInvalidSubType(){
        LevelRegistry.getItemForSubType("trigger", "invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeHasSubtypes(){
        LevelRegistry.typeHasSubTypes("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemNullClass(){
        LevelRegistry.registerItem("test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemSubtypeNullClass(){
        LevelRegistry.registerItemSubType("trigger", "test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemSubtypeInvalidType(){
        LevelRegistry.registerItemSubType("test", "test", TextTrigger.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemSubtypesForInvalidType(){
        LevelRegistry.getSubTypesForType("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterExistingSubType(){
        LevelRegistry.registerItemSubType("trigger", "end", TextTrigger.class);
    }
}