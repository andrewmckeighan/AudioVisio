package audiovisio.level;

import audiovisio.utils.LogHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Keeps track of ILevelItem objects used by the level system.
 *
 * The registry is used to help reduce the amount of code used
 * when loading a level. Rather than having a check for each
 * object type, Level gets the corresponding class for the type
 * from the registry and instantiates it. Registering a new type
 * can be done by implementing the {@link audiovisio.level.ILevelItem}
 * interface and by registering the class to the name of the type
 * that will be saved in the level. For example, to register the
 * Panel class, do the following:
 *
 * <pre>
 *     LevelRegistry.registerItem("panel", Panel.class);
 * </pre>
 *
 * ALL ILevelItem's should have a default constructor, otherwise
 * the level system WILL break.
 *
 * @author Matt Gerst
 */
public class LevelRegistry {
    private static HashMap<String, ItemData> items = new HashMap<String, ItemData>();

    /**
     * Register an item type with the system.
     *
     * Remember that the type name will be the name that the Level
     * looks for when loading level files, therefore the save function
     * of the item should set the appropriate value in the type field.
     * The item will not load correctly otherwise.
     *
     * @param type  The name of the type being added.
     * @param clazz A class that implements the ILevelItem interface.
     *
     * @throws java.lang.IllegalArgumentException If the given class is null
     */
    public static void registerItem( String type, Class<? extends ILevelItem> clazz ){
        if (clazz == null){
            throw new IllegalArgumentException("The registered class for type '" + type + "' cannot be null");
        }
        ItemData id = new ItemData(type, clazz);
        LevelRegistry.items.put(type, id);
        LogHelper.info("Registered item " + clazz.getName() + " for type " + type);
    }

    /**
     * Register a sub-type for a given type with the system.
     *
     * The sub-type will appear as the "subtype" key in the level. Level
     * only checks that there are subtypes registered for a type when
     * loading them.
     *
     * @param type    The name of the type to add to.
     * @param subtype The name of the subtype being added.
     * @param clazz   A class that implements the ILevelItem interface.
     *
     * @throws IllegalArgumentException If the given class is null or the registry doesn't know
     *                                  about the given type.
     */
    public static void registerItemSubType( String type, String subtype, Class<? extends ILevelItem> clazz ){
        if (clazz == null){
            throw new IllegalArgumentException("Subtype " + subtype + " of type " + type + " cannot be null");
        }
        if (!items.containsKey(type)){
            throw new IllegalArgumentException("Type " + type + " is not already registered!");
        }

        ItemData data = items.get(type);
        data.addSubType(subtype, clazz);
        LogHelper.info("Registered subtype " + subtype + " for Item " + type + ": " + clazz.getName());
    }

    /**
     * Get a set of all sub types for a given type.
     *
     * @param type The type to get subtypes for
     *
     * @return A set of all registered subtypes
     */
    public static Set<String> getSubTypesForType( String type ){
        ItemData data = LevelRegistry.items.get(type);
        if (data == null){
            throw new IllegalArgumentException("Invalid type " + type);
        }
        return data.subTypes.keySet();
    }

    /**
     * Check whether a type has any registered subtypes or not.
     *
     * This is one of two criteria Level uses when determining whether
     * an item is a subtype or not and determining how to load them.
     *
     * @param type The type to check
     *
     * @return true if has subtypes, false otherwise
     */
    public static boolean typeHasSubTypes( String type ){
        ItemData data = LevelRegistry.items.get(type);
        if (data == null){ throw new IllegalArgumentException("Invalid type " + type); }
        return data.hasSubTypes();
    }

    /**
     * Get a new instance of the specified type.
     *
     * @param type The type of item wanted.
     *
     * @return A instance of the class registered for the item type created with
     * its default constructor.
     *
     * @throws java.lang.IllegalArgumentException Thrown when a type that is not registered
     *                                            with the system has been requested
     * @throws java.lang.RuntimeException         Thrown when an error occurs creating the object
     */
    public static ILevelItem getItemForType( String type ){
        ItemData data = LevelRegistry.items.get(type);
        if (data == null){ throw new IllegalArgumentException("Invalid type " + type); }
        Class<? extends ILevelItem> clazz = data.itemClass;
        if (clazz == null){ throw new IllegalArgumentException("Invalid type " + type); }

        ILevelItem item;
        try{
            item = clazz.newInstance();
        } catch (InstantiationException e){
            throw new RuntimeException("Could not create class for type: " + type, e);
        } catch (IllegalAccessException e){
            throw new RuntimeException("Could not create class for type: " + type, e);
        }

        return item;
    }

    /**
     * Get a new instance of the specified subtype.
     *
     * @param type    The type of item.
     * @param subtype The subtype of the item wanted.
     *
     * @return An instance of the class registered for the subtype created with
     * its default constructor.
     *
     * @throws IllegalArgumentException Thrown when a type that is not registered
     *                                  with the system has been requested
     * @throws RuntimeException         Thrown when an error occurs creating the object
     */
    public static ILevelItem getItemForSubType( String type, String subtype ){
        ItemData data = LevelRegistry.items.get(type);
        if (data == null){ throw new IllegalArgumentException("Invalid type " + type); }
        Class<? extends ILevelItem> clazz = data.getSubType(subtype);
        if (clazz == null){ throw new IllegalArgumentException("Invalid subtype " + subtype + " for type " + type); }

        ILevelItem item;
        try{
            item = clazz.newInstance();
        } catch (InstantiationException e){
            throw new RuntimeException("Could not create class for type: " + type + ":" + subtype, e);
        } catch (IllegalAccessException e){
            throw new RuntimeException("Could not create class for type: " + type + ":" + subtype, e);
        }

        return item;
    }

    /**
     * Get the class registered for the specified type.
     *
     * @param type The type of the wanted class
     *
     * @return The registered class if the type exists, null otherwise
     */
    public static Class<? extends ILevelItem> getItemClassForType( String type ){
        return LevelRegistry.items.get(type) != null ? LevelRegistry.items.get(type).itemClass : null;
    }

    /**
     * An internal data structure to used to keep track of type->class associations.
     *
     * This is used mainly in case we want to expand the LevelRegistry to keep track
     * of more things about types.
     */
    private static class ItemData {
        String                      type;
        Class<? extends ILevelItem> itemClass;
        Map<String, Class<? extends ILevelItem>> subTypes = new HashMap<String, Class<? extends ILevelItem>>();

        public ItemData( String type, Class<? extends ILevelItem> clazz ){
            this.type = type;
            this.itemClass = clazz;
        }

        public void addSubType( String name, Class<? extends ILevelItem> clazz ){
            if (subTypes.containsKey(name)){
                throw new IllegalArgumentException("Cannot add duplicate subtypes to " + type);
            }

            subTypes.put(name, clazz);
        }

        public Class<? extends ILevelItem> getSubType( String name ){
            return subTypes.get(name);
        }

        public boolean hasSubTypes(){
            return !subTypes.isEmpty();
        }
    }
}
