package keepcalm.mods.permissions.api;

import java.util.List;

import net.minecraft.src.EntityPlayer;

public interface IPermissionsProvider {
	public List<String> getPermissionsForPlayer(String name);
	public List<String> getPermissionsForPlayer(EntityPlayer ep);
	
	public boolean doesPlayerHavePermission(String name, EntityPlayer player);
	
	public void givePlayerPerm(String name, EntityPlayer guy);
	
	public void removePlayerPerm(String name, EntityPlayer guy);
	
	public PermissionPriority getPriority();
}
