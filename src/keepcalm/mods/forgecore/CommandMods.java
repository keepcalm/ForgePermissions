package keepcalm.mods.forgecore;

import keepcalm.mods.forgecore.api.permissions.Permissions;
import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class CommandMods extends CommandBase {

	@Override
	public String getCommandName() {
		return "mods";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender ics) {
		return Permissions.doesPlayerHavePermission("forgecore.mods", (EntityPlayer) ics);
	}
	
	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		var1.sendChatToPlayer("Active Mods:");
		for (ModContainer i : Loader.instance().getActiveModList()) {
			var1.sendChatToPlayer(ChatColor.YELLOW + i.getName() + ChatColor.RESET + ", version " + ChatColor.GREEN + i.getDisplayVersion() + ChatColor.RESET);
		}
	}

}
