package keepcalm.mods.forgecore.api.permissions;

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
	
	
	
	public void givePlayerPerm(IPermission perm, EntityPlayer guy);
	
	/**
	 * Used if a player is offline. Should ALWAYS work.
	 * @param perm - permission to give
	 * @param name - name of player
	 */
	public void givePlayerPerm(IPermission perm, String name);

	public void removePlayerPerm(IPermission perm, EntityPlayer guy);
	
	/**
	 * Used if a player is offline. Should ALWAYS work.
	 * @param perm - permission to remove
	 * @param name - name of player
	 */
	public void removePlayerPerm(IPermission perm, String name);
	
	/**
	 * Called on server starting - so you CAN access the server.
	 * @throws Exception
	 */
	public void initialisePermissions() throws Exception;
	
	public void createSectionForNewPlayer(String name);
	
	/**
	 * Called on server shutdown, and whenever command input is given.
	 */
	public void savePermissions();
	
	public PermissionPriority getPriority();
	
	public void removePlayerPerm(String name, String username);
	
	public boolean doesPlayerHavePermission(String path, EntityPlayer player);
}
