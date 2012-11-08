package keepcalm.mods.permissions.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Permissions {
	private static final Permissions instance = new Permissions();
	
	private static ArrayList<IPermissionsProvider> possibleProviders = new ArrayList<>();
	
	private static List<String> permissions = new ArrayList<String>();

	public static List<String> allPermissions;
	
	public static Permissions getInstance() {
		return instance;
	}
	
	public static void init() {
		// TODO
	}
	
	public static void registerPermission(String name) {
		allPermissions.add(name);
	}
	
	public static void deregisterPermission(String name) {
		allPermissions.remove(name);
	}
	
	public void addPermissionProvider(IPermissionsProvider permProv) {
		possibleProviders.add(permProv);
	}
	
	
}
