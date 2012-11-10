package keepcalm.mods.permissions.api;

import keepcalm.mods.permissions.internals.BasicPermission;

public class PermissionFactory {
	public static IPermission getPermissionForArguments(PermissibleSetting set, String name, String desc, String configPath) {
		return new BasicPermission(set,name,desc,configPath);
	}
}
