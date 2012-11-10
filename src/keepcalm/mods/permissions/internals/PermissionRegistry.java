package keepcalm.mods.permissions.internals;

import java.util.Map;
import java.util.HashMap;

import cpw.mods.fml.common.FMLCommonHandler;
import keepcalm.mods.permissions.api.*;

/**
 * Not for modders - strictly internal
 * @author keepcalm
 *
 */
public class PermissionRegistry {
	private static Map<String,IPermission> permissions = new HashMap<String,IPermission>();
	
	public static void registerPermission(IPermission permission) {
		String path = permission.getConfigurationPath();
		if (permissions.containsKey(path)) {
			FMLCommonHandler.instance().getFMLLogger().warning("The permission " + path + " conflicts with another permission!");
			return;
		}
		permissions.put(path, permission);
	}
	
	public static IPermission getPermissionFromPath(String configPath) {
		try {
			return permissions.get(configPath);
		}
		catch (Exception e) {
			FMLCommonHandler.instance().getFMLLogger().warning("The permission " + configPath + " does not exist, but something tried to use it!");
			return null;
		}
	}
	public static void deregisterPermission(String path) {
		permissions.remove(path);
	}
}
