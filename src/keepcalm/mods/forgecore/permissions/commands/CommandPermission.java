package keepcalm.mods.forgecore.permissions.commands;

import java.util.Arrays;
import java.util.List;

import keepcalm.mods.forgecore.ChatColor;
import keepcalm.mods.forgecore.api.permissions.Permissions;


import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.WrongUsageException;

public class CommandPermission extends CommandBase {
	private static final List<String> validActions = Arrays.asList(new String[] {"add", "remove", "list"});
	
	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "permissions";
	}
	
	@Override
	public String getCommandUsage(ICommandSender s ) {
		return "/permissions <add|remove|list> player [permission]";
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1) {
		if (!(par1 instanceof EntityPlayer)) {
			return true;
		}
		return Permissions.doesPlayerHavePermission("forgePermissions.commands.permissionscontrol", (EntityPlayer) par1);
	}
	
	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		boolean isConsole = !(var1 instanceof EntityPlayer);
		String action;
		String who;
		try {
			action = var2[0].toLowerCase();
			who = var2[1];
			
		}
		catch (ArrayIndexOutOfBoundsException e) {
			throw new WrongUsageException("/permissions <add|remove|list> player [permission]");
		}
		if (!validActions.contains(action.toLowerCase())) {
			throw new WrongUsageException("/permissions <add|remove|list> player [permission]");
		}
		
		String what = "";
		if (action == "add"  || action == "remove" ) {
			try{
				what = var2[2];
			}
			catch (Exception e) {
				throw new WrongUsageException("/permissions <add|remove|list> player [permission]");
			}
		}
		
		if (action == "add") {
			Permissions.givePlayerPermission(Permissions.getPermissionFromPath(what), MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(who));
			var1.sendChatToPlayer(ChatColor.YELLOW + "Success!" + ChatColor.RESET);
		}
		else if (action == "remove") {
			Permissions.removePlayerPermission(Permissions.getPermissionFromPath(what), MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(who));
			var1.sendChatToPlayer(ChatColor.YELLOW + "Success!" + ChatColor.RESET);
		}
		
		
		
		
	}

}
