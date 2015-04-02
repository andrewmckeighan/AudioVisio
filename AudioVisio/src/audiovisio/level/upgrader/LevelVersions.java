package audiovisio.level.upgrader;

import audiovisio.utils.LogHelper;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * @author Matt Gerst
 */
public class LevelVersions {
    private static HashMap<String, ILevelUpgrader> upgraders = new HashMap<String, ILevelUpgrader>();

    /**
     * Perform the upgrade on the file.
     *
     * @param oldVersion The version string we are upgrading from
     * @param object     The JSONObject to upgrade
     */
    public static void upgrade( String oldVersion, JSONObject object ){
        String currentVersion = oldVersion;

        while (LevelVersions.upgraders.containsKey(currentVersion)){
            ILevelUpgrader upgrader = LevelVersions.upgraders.get(oldVersion);
            LogHelper.info(String.format("Upgrading %s -> %s", oldVersion, upgrader.newVersion()));
            upgrader.upgrade(object);

            currentVersion = upgrader.newVersion().toString();
        }
    }

    /**
     * Initialize the upgrader list
     */
    public static void init(){
        LevelVersions.addVersion("0.2", new Level03Upgrader());
        LevelVersions.addVersion("0.3", new Level04Upgrader());
        LevelVersions.addVersion("0.4", new Level05Upgrader());
    }

    /**
     * Add a version to the list of upgradable versions.
     *
     * @param version  The version string of the OLD version (the version we are upgrading from)
     * @param upgrader The upgrader that will perform the upgrade.
     */
    public static void addVersion( String version, ILevelUpgrader upgrader ){
        LevelVersions.upgraders.put(version, upgrader);
    }
}
