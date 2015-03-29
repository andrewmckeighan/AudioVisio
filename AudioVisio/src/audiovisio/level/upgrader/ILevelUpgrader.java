package audiovisio.level.upgrader;

import audiovisio.utils.VersionString;
import org.json.simple.JSONObject;

/**
 * This defines an interface for level upgrades. Level upgraders
 * are used to upgrade from older version of level files to newer
 * files. They should apply sane defaults for everything added.
 *
 * @author Matt Gerst
 */
public interface ILevelUpgrader {
    /**
     * Upgrade the given level's JSONObject. Add objects and
     * attributes to objects as needed with sane defaults.
     *
     * @param obj The object to upgrade.
     */
    void upgrade( JSONObject obj );

    /**
     * Get what version the file will be after upgrading.
     *
     * @return The new version.
     */
    VersionString newVersion();
}
