package keepcalm.mods.permissions.api;

import cpw.mods.fml.common.ModContainer;

/**
 * you can run if (permObj.equals("string_of_perm_config_path")), it will behave like a String.
 * @author keepcalm
 *
 */
public abstract class IPermission {
	public abstract PermissibleSetting defaultSetting();
	
	/**
	 * @return user-friendly name of permissions
	 */
	public abstract String getName();
	
	
	/**
	 * 
	 * @return description of permission for help, etc
	 */
	public abstract String getDescription();
	
	/**
	 * @return the unique path of the description - something like yourModID.perms.permName
	 */
	public abstract String getConfigurationPath();
	
	@Override
	public boolean equals(Object a) {
		if (a instanceof String) {
			String b = (String) a;
			if (b.equals(this.getConfigurationPath())) 
				return true;
			return false;
		}
		
		if (!(a instanceof IPermission)) {
			return false;
		}
		IPermission b = (IPermission) a;
		
		if (b.getDescription() == this.getDescription() && b.getName() == this.getName() && b.getConfigurationPath() == this.getConfigurationPath())
			return true;
		
		
		
		return false;
	}
}
