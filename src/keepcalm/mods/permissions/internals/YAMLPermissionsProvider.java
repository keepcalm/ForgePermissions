package keepcalm.mods.permissions.internals;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;

import keepcalm.mods.permissions.api.IPermission;
import keepcalm.mods.permissions.api.IPermissionsProvider;
import keepcalm.mods.permissions.api.PermissionPriority;
import keepcalm.mods.permissions.api.Permissions;
import keepcalm.mods.permissions.configurationHelpers.ConfigurationSection;
import keepcalm.mods.permissions.configurationHelpers.YamlConfiguration;
import net.minecraft.src.EntityPlayer;

public class YAMLPermissionsProvider implements IPermissionsProvider {
	
	private HashMap<String, List<IPermission>> perms;
	/**
	 * A map of username -> list of groups it inherits from
	 */
	private HashMap<String, List<String>> userDependencyMap;
	private HashMap<String, List<IPermission>> groups;
	/**
	 * a map of group name -> groups it inherits from
	 */
	private HashMap<String, List<String>> groupDependencyMap;
 	private File userFile;
	private File configFile;
	private File groupsFile;
	private YamlConfiguration configuration;
	private YamlConfiguration usersCfg;
	private YamlConfiguration groupCfg;
	
	@Override
	public List<IPermission> getPermissionsForPlayer(String name) {
		return perms.get(name.toLowerCase());
	}

	@Override
	public List<IPermission> getPermissionsForPlayer(EntityPlayer ep) {
		return perms.get(ep.username.toLowerCase());
	}

	@Override
	public boolean doesPlayerHavePermission(String path, EntityPlayer player) {
		return perms.get(player.username.toLowerCase()).contains(path);
	}

	@Override
	public void givePlayerPerm(IPermission path, EntityPlayer guy) {
		if (!perms.get(guy.username.toLowerCase()).contains(path)) {
			perms.get(guy.username.toLowerCase()).add(path);
		}
	}

	@Override
	public void removePlayerPerm(String name, EntityPlayer guy) {
		String key = guy.username.toLowerCase();
		if (perms.get(key).contains(name)) {
			perms.get(key).remove(name);
		}
	}
	
	private void recalculateUserInfo() {
		loadUserInformationFromYaml(usersCfg);
	}
	
	private void loadUserInformationFromYaml(YamlConfiguration users) {
		for (String i : users.getKeys(false)) {
			
			if (users.isConfigurationSection(i)) {
				
				ConfigurationSection userConfig = users.getConfigurationSection(i);
				// contains computed permissions for this user
				List<IPermission> userPerms = new ArrayList<IPermission>();
				List<IPermission> ignoredPerms = new ArrayList<IPermission>();
				if (userConfig.isList("permissions")) {
					// add permissions
					
					for (String perm : userConfig.getStringList("permissions")) {
						boolean cancel = false;
						if (perm.startsWith("!")) {
							cancel = true;
							// remove the exclamation mark
							perm = perm.replace('!', ' ').trim();
						}
						IPermission iperm = Permissions.getPermissionFromPath(perm);
						if (iperm == null) {
							FMLCommonHandler.instance().getFMLLogger().warning("The permission " + perm + " has not been registered - but is given to " + i);
							continue;
						}
						if (cancel) {
							ignoredPerms.add(iperm);
						}
						else {
							userPerms.add(iperm);
						}
					}
				} // end of permissions processing
			}
			else {
				FMLCommonHandler.instance().getFMLLogger().warning("The user entry for " + i + " is INVALID! Ignoring...");
			}
		}
	}
	
	@Override
	public void initialisePermissions() throws Exception {
		// groups
		this.groupsFile = new File(Permissions.permissionLocation, "/groups.yml");
		groupsFile.createNewFile();
		InputStream defaultGroups = this.getClass().getClassLoader().getResourceAsStream("/keepcalm/mods/permissions/defaults/defaultGroups.yml");
		groupCfg = new YamlConfiguration();
		groupCfg.load(groupsFile);
		YamlConfiguration defaults = new YamlConfiguration();
		defaults.load(defaultGroups);
		groupCfg.setDefaults(defaults);
		loadGroupInformation(groupCfg);
		
		
		// users - __MUST__ be last - otherwise the group-loading functionality breaks
		this.userFile = new File(Permissions.permissionLocation, "/users.yml");
		userFile.createNewFile();
		InputStream defaultUsers = this.getClass().getClassLoader().getResourceAsStream("/keepcalm/mods/permissions/defaults/defaultUsers.yml");
		usersCfg = new YamlConfiguration();
		usersCfg.load(userFile);
		defaults = new YamlConfiguration(); // reset
		defaults.load(defaultUsers);
		usersCfg.setDefaults(defaults);
		loadUserInformationFromYaml(usersCfg);
		
		
	}

	private void loadGroupInformation(YamlConfiguration groupCfg) {
		for (String key : groupCfg.getKeys(false)) {
			if (groupCfg.isConfigurationSection(key)) {
				ConfigurationSection cfg = groupCfg.getConfigurationSection(key);
				
				// so we are going to run excludedPerms on just the groups, not calculatedPerms itself.
				List<IPermission> calculatedPerms = new ArrayList<IPermission>();
				List<IPermission> excludedPerms = new ArrayList<IPermission>();
				if (groupCfg.isList("permissions")) {
					List<String> groupPerms = groupCfg.getStringList("permissions");
					for (String perm : groupPerms) {
						boolean cancel = false;
						if (perm.startsWith("!")) {
							cancel = true;
							perm = perm.replace('!', ' ').trim();
						}
						IPermission p = Permissions.getPermissionFromPath(perm);
						if (p == null) {
							FMLCommonHandler.instance().getFMLLogger().warning("Invalid permisison: " + perm);
							continue;
						}
						else {
							if (cancel) {
							
								excludedPerms.add(p);
							}
							else {
								
								calculatedPerms.add(p);
							}
						}
					}
				}
				List<String> dependencies = new ArrayList<String>();
				List<String> exclusions = new ArrayList<String>();
				if (groupCfg.isList("inherit")) {
					for (String group : groupCfg.getStringList("inherit")) {
						boolean cancel = false;
						if (group.startsWith("!")) {
							cancel = true;
							group = group.replace('!', ' ').trim();
						}
						if (cancel) {
							exclusions.add(group);
						}
						else {
							dependencies.add(group);
						}
					}
				}
				dependencies.removeAll(exclusions);
				
			}
		}
	}
	
	private void initialiseGroupInfo() {
		loadGroupInformation(groupCfg);
	}

	@Override
	public void savePermissions() {
		
	}

	@Override
	public PermissionPriority getPriority() {
		return PermissionPriority.NORMAL;
	}

	@Override
	public void givePlayerPerm(IPermission perm, String name) {
		if (!perms.get(name.toLowerCase()).contains(perm)) {
			perms.get(name.toLowerCase()).add(perm);
		}
	}

	
	@Override
	public void removePlayerPerm(IPermission perm, String name) {
		if (!perms.get(name.toLowerCase()).contains(perm)) {
			perms.get(name.toLowerCase()).add(perm);
		}
	}
	
	@Override
	public void removePlayerPerm(String path, String username) {
		if (!perms.get(username.toLowerCase()).contains(path)) {
			perms.get(username.toLowerCase()).remove(path);
		}
	}

}
