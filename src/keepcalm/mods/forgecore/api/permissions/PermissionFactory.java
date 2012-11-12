package keepcalm.mods.forgecore.api.permissions;

import keepcalm.mods.forgecore.permissions.internals.BasicPermission;

public class PermissionFactory {
	public static IPermission getPermissionForArguments(PermissibleSetting set, String name, String desc, String configPath) {
		return new BasicPermission(set,name,desc,configPath);
	}
}
