package keepcalm.mods.forgecore.permissions.internals;

import keepcalm.mods.forgecore.api.permissions.IPermission;
import keepcalm.mods.forgecore.api.permissions.PermissibleSetting;
import cpw.mods.fml.common.ModContainer;


/**
 * 
 * A basic, non-dynamic version of a permission
 * @author keepcalm
 *
 */
public class BasicPermission extends IPermission {

	private PermissibleSetting setting;
	private String name, desc, path;
	
	public BasicPermission(PermissibleSetting s, String name, String desc, String path) {
		this.setting = s;
		this.name = name;
		this.desc = desc;
		this.path = path;
	}
	
	@Override
	public PermissibleSetting defaultSetting() {
		return setting;
	}

	@Override
	public String getName() {
		return name;
	}



	@Override
	public String getDescription() {
		return desc;
	}

	@Override
	public String getConfigurationPath() {
		return path;
	}

}
