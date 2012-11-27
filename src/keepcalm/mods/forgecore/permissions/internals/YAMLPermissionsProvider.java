package keepcalm.mods.forgecore.permissions.internals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLCommonHandler;

import keepcalm.mods.forgecore.api.permissions.IPermission;
import keepcalm.mods.forgecore.api.permissions.IPermissionsProvider;
import keepcalm.mods.forgecore.api.permissions.PermissionPriority;
import keepcalm.mods.forgecore.api.permissions.Permissions;
import keepcalm.mods.forgecore.permissions.configurationHelpers.ConfigurationSection;
import keepcalm.mods.forgecore.permissions.configurationHelpers.YamlConfiguration;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;

public class YAMLPermissionsProvider implements IPermissionsProvider {

	private HashMap<String, List<IPermission>> userPerms = new HashMap<String, List<IPermission>>();
	/**
	 * A map of username -> list of groups it inherits from
	 */
	private HashMap<String, List<String>> userDependencyMap = new HashMap<String, List<String>>();
	private HashMap<String, List<IPermission>> userExclusions = new HashMap<String, List<IPermission>>();
	
	private HashMap<String, List<IPermission>> groupPerms = new HashMap<String, List<IPermission>>();
	/**
	 * a map of group name -> groups it inherits from
	 */
	private HashMap<String, List<String>> groupDependencyMap = new HashMap<String, List<String>>();
	private HashMap<String, List<String>> groupExclusions = new HashMap<String, List<String>>();
	private File userFile;
	private File configFile;
	private File groupsFile;
	private YamlConfiguration configuration;
	private YamlConfiguration usersCfg;
	private YamlConfiguration groupCfg;

	@Override
	public List<IPermission> getPermissionsForPlayer(String name) {
		try {
			return userPerms.get(name.toLowerCase());
		}
		catch (Exception e) {
			return Arrays.asList(new IPermission[0]);
		}
	}

	@Override
	public List<IPermission> getPermissionsForPlayer(EntityPlayer ep) {
		try {
			return userPerms.get(ep.username.toLowerCase());
		}
		catch (Exception e) {
			return Arrays.asList(new IPermission[0]);
		}
	}

	@Override
	public boolean doesPlayerHavePermission(String path, EntityPlayer player) {
		try {
			return userPerms.get(player.username.toLowerCase()).contains(path);
		}
		catch (Exception e) {
			if (!MinecraftServer.getServer().isSinglePlayer())
				return false;
			return true;
		}
	}

	@Override
	public void givePlayerPerm(IPermission path, EntityPlayer guy) {
		if (!userPerms.get(guy.username.toLowerCase()).contains(path)) {
			userPerms.get(guy.username.toLowerCase()).add(path);
		}
	}

	@Override
	public void removePlayerPerm(IPermission name, EntityPlayer guy) {
		String key = guy.username.toLowerCase();
		if (userPerms.get(key).contains(name)) {
			userPerms.get(key).remove(name);
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
					userExclusions.put(i, ignoredPerms);
					
				} // end of permissions processing
				List<String> dependencies = new ArrayList<String>();
				if (userConfig.isList("groups")) {
					for (String j : userConfig.getStringList("groups")) {
						dependencies.add(j);
					}
				}
				userDependencyMap.put(i, dependencies);
			}
			else {
				FMLCommonHandler.instance().getFMLLogger().warning("The user entry for " + i + " is INVALID! Ignoring...");
			}
		}
		recalculateUserDependencies();
	}

	private void recalculateUserDependencies() {
		
		for (String key : userDependencyMap.keySet()) {
			for (String group : userDependencyMap.get(key)) {
				userPerms.get(key).addAll(groupPerms.get(group));
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
		try {
			defaults.load(defaultGroups);
			groupCfg.setDefaults(defaults);
		}
		catch (Exception e) {
			FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "Failed to read defaults file - perhaps your are in a development environment?", e);
		}
		
		loadGroupInformation(groupCfg);


		// users - __MUST__ be last - otherwise the group-loading functionality breaks
		this.userFile = new File(Permissions.permissionLocation, "/users.yml");
		userFile.createNewFile();
		InputStream defaultUsers = this.getClass().getClassLoader().getResourceAsStream("/keepcalm/mods/permissions/defaults/defaultUsers.yml");
		usersCfg = new YamlConfiguration();
		usersCfg.load(userFile);
		defaults = new YamlConfiguration(); // reset
		try {
			defaults.load(defaultUsers);
			usersCfg.setDefaults(defaults);
		}
		catch (Exception e) {
			FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "Failed to read defaults file - perhaps your are in a development environment?", e);
		}
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
				if (groupCfg.isList("inherit")) {
					for (String group : groupCfg.getStringList("inherit")) {
						dependencies.add(group);
					}
				}
				this.groupExclusions.put(key, dependencies);
				
				
				

			}
			else {
				FMLCommonHandler.instance().getFMLLogger().warning("Invalid group configuration: " + key + " - ignoring...");
			}
			updateGroupDependencyInfo();
		}
	}
	
	private void updateGroupDependencyInfo() {
		
		for (String i : this.groupDependencyMap.keySet()) {
			List<IPermission> perms = new ArrayList<IPermission>();
			for (String j : this.groupDependencyMap.get(i)) {
				perms.addAll(groupPerms.get(j));
				
			}
			perms.removeAll(groupExclusions.get(i));
			groupPerms.get(i).addAll(perms);
		}
		
	}

	private void initialiseGroupInfo() {
		loadGroupInformation(groupCfg);
	}

	@Override
	public void savePermissions() {
		try {
			usersCfg.save(userFile);
			groupCfg.save(groupsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public PermissionPriority getPriority() {
		return PermissionPriority.NORMAL;
	}

	@Override
	public void givePlayerPerm(IPermission perm, String name) {
		if (!userPerms.get(name.toLowerCase()).contains(perm)) {
			userPerms.get(name.toLowerCase()).add(perm);
		}
	}


	@Override
	public void removePlayerPerm(IPermission perm, String name) {
		if (!userPerms.get(name.toLowerCase()).contains(perm)) {
			userPerms.get(name.toLowerCase()).add(perm);
		}
	}

	@Override
	public void removePlayerPerm(String path, String username) {
		if (!userPerms.get(username.toLowerCase()).contains(path)) {
			userPerms.get(username.toLowerCase()).remove(path);
		}
	}

	@Override
	public void createSectionForNewPlayer(String name) {
		if (!userPerms.containsKey(name)) 
		userPerms.put(name, new ArrayList<IPermission>());
	}

}
