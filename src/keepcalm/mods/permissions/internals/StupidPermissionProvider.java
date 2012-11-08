package keepcalm.mods.permissions.internals;

import java.util.List;

import net.minecraft.src.EntityPlayer;
import keepcalm.mods.permissions.api.IPermissionsProvider;
import keepcalm.mods.permissions.api.PermissionPriority;
import keepcalm.mods.permissions.api.Permissions;

public class StupidPermissionProvider implements IPermissionsProvider {

	@Override
	public List<String> getPermissionsForPlayer(String name) {
		return Permissions.allPermissions;
	}

	@Override
	public List<String> getPermissionsForPlayer(EntityPlayer ep) {
		return Permissions.allPermissions;
	}

	@Override
	public boolean doesPlayerHavePermission(String name, EntityPlayer player) {
		return true;
	}

	@Override
	public void givePlayerPerm(String name, EntityPlayer guy) {

	}

	@Override
	public void removePlayerPerm(String name, EntityPlayer guy) {
		
	}

	@Override
	public PermissionPriority getPriority() {
		return PermissionPriority.LOWEST;
	}

}
