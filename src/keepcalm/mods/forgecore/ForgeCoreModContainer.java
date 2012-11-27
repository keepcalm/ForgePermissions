package keepcalm.mods.forgecore;

import static keepcalm.mods.forgecore.permissions.asm.PermissionsContainer.VERSION;

import java.util.ArrayList;
import java.util.List;

import keepcalm.mods.forgecore.api.permissions.PermissibleSetting;
import keepcalm.mods.forgecore.api.permissions.PermissionFactory;
import keepcalm.mods.forgecore.api.permissions.Permissions;
import net.minecraft.src.ICommand;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.ServerCommandManager;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;


/**
 * Dummy mod which acts as a parent for other mods in this package
 * @author keepcalm
 *
 */
@Mod(modid="ForgeCore_Dummy",version=VERSION,name="ForgeCore")
public class ForgeCoreModContainer {
	
	
	private static List<ModContainer> mods = new ArrayList<ModContainer>();
	
	private static List<ICommand> commands = new ArrayList<ICommand>();
	
	
	public static void registerCommand(ICommand ic) {
		commands.add(ic);
	}
	
	public ForgeCoreModContainer() {
		
		Permissions.registerPermission(PermissionFactory.getPermissionForArguments(PermissibleSetting.all, "/mod", "Get mods installed", "forgecore.mods"));
		registerCommand(new CommandMods());
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent ev) throws MinecraftException {
		// check that we were installed in the right place
		try {
			getClass().getClassLoader().loadClass("org.yaml.snakeyaml.Yaml");
		}
		catch (ClassNotFoundException e) {
			throw new MinecraftException("ForgeCore is a coremod!");
		}
		/*for (Field i : myMeta.getClass().getFields()) {
			
				try {
					String val = (String) i.get(myMeta);
					String name = i.getName();
					System.out.println(name + " => " + val);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				catch (Exception e) {}
				
		}*/
 	}
	
	@ServerStarting
	public void serverStart(FMLServerStartingEvent ev) {
		ServerCommandManager scm = (ServerCommandManager) ev.getServer().getCommandManager();
		for (ICommand i : commands) {
			System.out.println("[ForgeCore] Registering command: " + i.getCommandName());
			scm.registerCommand(i);
		}
	}

}
