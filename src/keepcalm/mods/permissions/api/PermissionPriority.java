package keepcalm.mods.permissions.api;

import java.util.HashMap;

public enum PermissionPriority {


	HIGHEST,
	HIGH,
	NORMAL,
	LOW,
	LOWEST;
	
	public static final int valueOf(PermissionPriority v) {
		switch (v) {
		case HIGHEST:
			return 2;
		case HIGH:
			return 1;
		case NORMAL:
			return 0;
		case LOW:
			return -1;
		case LOWEST: 
			return -2;
		default:
			return -3;
		}
	}
	
	public static boolean isXGreaterThan(PermissionPriority p1, PermissionPriority p2) {
		if (valueOf(p1) > valueOf(p2)) {
			return true;
		}
		return false;
	}
	
}
