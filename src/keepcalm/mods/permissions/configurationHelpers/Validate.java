package keepcalm.mods.permissions.configurationHelpers;

public class Validate {
	public static void notNull(Object o, String error) {
		if (o == null) {
			throw new RuntimeException(error);
		}
	}

	public static void notEmpty(String path, String string) {
		if (path.isEmpty()) {
			throw new RuntimeException(string);
		}
	}

	public static void isTrue(boolean b, String string) {
		if (!b)
			throw new RuntimeException(string);
	}
}
