package keepcalm.mods.permissions.internals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import keepcalm.mods.permissions.api.IPermission;
import cpw.mods.fml.common.FMLCommonHandler;

/**
 * Not for modders - strictly internal
 * @author keepcalm
 *
 */
public class PermissionRegistry {
	private static Map<String,IPermission> permissions = new HashMap<String,IPermission>();
	/**
	 * Permissions which let all users use them by default
	 */
	private static Map<String,IPermission> allowAll = new HashMap<String, IPermission>();
	/**
	 * Permissions which let ops use them by default
	 */
	private static Map<String,IPermission> allowOps = new HashMap<String, IPermission>();
	/**
	 * Permissions which let no-body use them by default
	 */
	private static Map<String,IPermission> allowNone = new HashMap<String,IPermission>();
	public static void registerPermission(IPermission permission) {
		String path = permission.getConfigurationPath();
		if (permissions.containsKey(path)) {
			FMLCommonHandler.instance().getFMLLogger().warning("The permission " + path + " conflicts with another permission!");
			return;
		}
		permissions.put(path, permission);
		
		switch (permission.defaultSetting()) {
		case all:
			allowAll.put(path,permission);
			break;
		case nobody:
			allowNone.put(path,permission);
			break;
		case operator:
			allowOps.put(path,permission);
			break;
		}
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
	
	public static Set<String> getOpPerms() {
		return allowOps.keySet();
	}
	/**
	 * @return a list of permissions which are globally usable by default
	 */
	public static Set<String> getAllPerms() {
		return allowAll.keySet();
	}
	
	public static Set<String> getNobodyPerms() {
		return allowNone.keySet();
	}
}
