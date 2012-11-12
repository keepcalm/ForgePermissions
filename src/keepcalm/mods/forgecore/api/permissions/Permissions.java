package keepcalm.mods.forgecore.api.permissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import keepcalm.mods.forgecore.permissions.asm.PermissionsContainer;
import keepcalm.mods.forgecore.permissions.internals.PermissionRegistry;
import keepcalm.mods.forgecore.permissions.internals.StupidPermissionProvider;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import cpw.mods.fml.common.FMLCommonHandler;

public class Permissions {
	private static final Permissions instance = new Permissions();

	public static File permissionLocation;
	
	private static ArrayList<IPermissionsProvider> possibleProviders = new ArrayList<IPermissionsProvider>();

	public static IPermissionsProvider defaultPermsProvider = new StupidPermissionProvider();

	private static List<String> permissions = new ArrayList<String>();

	public static List<IPermission> allPermissions = new ArrayList<IPermission>();
	
	private static boolean initialized;
	
	public static Permissions getInstance() {
		return instance;
	}
	/**
	 * Called on ServerStarting. All plugins must register their permissions on PostInit or prior
	 */
	public static void init() {
		// if it's not already set.
		permissionLocation = PermissionsContainer.cfgDir;
		permissionLocation.mkdirs();
		if (defaultPermsProvider instanceof StupidPermissionProvider) {
			try {
				for (IPermissionsProvider j : possibleProviders) {
					if (PermissionPriority.isXGreaterThan(j.getPriority(), defaultPermsProvider.getPriority())) {
						defaultPermsProvider = j;

					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			defaultPermsProvider.initialisePermissions();
		} catch (Exception e) {
			FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "Permissions startup failed... Falling back to stupid permissions provider!", e);
			defaultPermsProvider = new StupidPermissionProvider();
			try {
				defaultPermsProvider.initialisePermissions();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void registerPermission(IPermission perm) {
		PermissionRegistry.registerPermission(perm);
		allPermissions.add(perm);
	}

	public static void deregisterPermission(String configPath) {
		PermissionRegistry.deregisterPermission(configPath);
		allPermissions.remove(configPath);
	}
	
	public static void deregisterPermission(IPermission perm) {
		PermissionRegistry.deregisterPermission(perm.getConfigurationPath());
		allPermissions.remove(perm);
	}

	public static void addPermissionProvider(IPermissionsProvider permProv) {
		possibleProviders.add(permProv);
	}	
	
	public void setDefaultPermissionProvider(IPermissionsProvider newDefault) {
		if (initialized) 
			return;
		this.defaultPermsProvider = newDefault;
	}
	
	public static boolean doesPlayerHavePermission(String permName, EntityPlayer player) {
		return defaultPermsProvider.doesPlayerHavePermission(permName, player);
	}
	
	public static void givePlayerPermission(IPermission perm, EntityPlayer sender) {
		defaultPermsProvider.givePlayerPerm(perm, sender);
		
	}
	
	public static IPermission getPermissionFromPath(String configPath) {
		return PermissionRegistry.getPermissionFromPath(configPath);
	}

}
