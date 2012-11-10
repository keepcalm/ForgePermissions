package keepcalm.mods.permissions.commands;

import java.util.Arrays;
import java.util.List;

import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;

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
		
		
		
		
		
		
	}

}
