package keepcalm.mods.permissions.api;

import java.io.FileNotFoundException;
import java.util.List;

import net.minecraft.src.EntityPlayer;

/**
 * Interface for classes wishing to register Permissions providers.
 * Must specify an empty constructor.
 * @author keepcalm
 *
 */
public interface IPermissionsProvider {
	public List<IPermission> getPermissionsForPlayer(String name);
	public List<IPermission> getPermissionsForPlayer(EntityPlayer ep);
	
	
	
	public boolean doesPlayerHavePermission(String name, EntityPlayer player);
	
	public void givePlayerPerm(IPermission perm, EntityPlayer guy);
	
	/**
	 * Used if a player is offline. Should ALWAYS work.
	 * @param perm - permission to give
	 * @param name - name of player
	 */
	public void givePlayerPerm(IPermission perm, String name);
	
	public void removePlayerPerm(String name, EntityPlayer guy);
	
	/**
	 * Used if a player is offline. Should ALWAYS work.
	 * @param perm - permission to remove
	 * @param name - name of player
	 */
	public void removePlayerPerm(IPermission perm, String name);
	
	public void initialisePermissions() throws Exception;
	
	public void savePermissions();
	
	public PermissionPriority getPriority();
	
	public void removePlayerPerm(String name, String username);
}
