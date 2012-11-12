package keepcalm.mods.forgecore.permissions.internals;

import java.util.List;

import net.minecraft.src.EntityPlayer;
import keepcalm.mods.forgecore.api.permissions.IPermission;
import keepcalm.mods.forgecore.api.permissions.IPermissionsProvider;
import keepcalm.mods.forgecore.api.permissions.PermissionPriority;
import keepcalm.mods.forgecore.api.permissions.Permissions;

/**
 * A simple permissions provider which allows everyone to do everything.
 * @author keepcalm
 *
 */
public class StupidPermissionProvider implements IPermissionsProvider {

	@Override
	public List<IPermission> getPermissionsForPlayer(String name) {
		return Permissions.allPermissions;
	}

	@Override
	public List<IPermission> getPermissionsForPlayer(EntityPlayer ep) {
		return Permissions.allPermissions;
	}

	@Override
	public boolean doesPlayerHavePermission(String name, EntityPlayer player) {
		return true;
	}

	@Override
	public void givePlayerPerm(IPermission perm, EntityPlayer guy) {}

	@Override
	public void removePlayerPerm(String name, EntityPlayer guy) {}

	@Override
	public PermissionPriority getPriority() {
		return PermissionPriority.LOWEST;
	}

	@Override
	public void initialisePermissions() {}

	@Override
	public void savePermissions() {}

	@Override
	public void givePlayerPerm(IPermission perm, String name) {}

	@Override
	public void removePlayerPerm(IPermission perm, String name) {}

	@Override
	public void removePlayerPerm(String name, String username) {}

	@Override
	public void createSectionForNewPlayer(String name) {}



}
