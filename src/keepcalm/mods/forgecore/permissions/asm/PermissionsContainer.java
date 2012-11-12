package keepcalm.mods.forgecore.permissions.asm;

import java.io.File;
import java.util.Arrays;

import net.minecraft.src.ServerCommandManager;

import keepcalm.mods.forgecore.ForgeCoreModContainer;
import keepcalm.mods.forgecore.api.permissions.IPermission;
import keepcalm.mods.forgecore.api.permissions.PermissibleSetting;
import keepcalm.mods.forgecore.api.permissions.PermissionFactory;
import keepcalm.mods.forgecore.api.permissions.Permissions;
import keepcalm.mods.forgecore.permissions.commands.CommandPermission;
import keepcalm.mods.forgecore.permissions.internals.YAMLPermissionsProvider;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class PermissionsContainer extends DummyModContainer {
	public static File cfgDir;
	public static final String VERSION = "1.4.2-0";
	
	public PermissionsContainer() {
		super(new ModMetadata());
		ModMetadata meta = super.getMetadata();
		meta.authorList = Arrays.asList(new String[] {"keepcalm"});
		meta.modId = "ForgePermissions";
		meta.name = "ForgePermissions";
		meta.description = "A simple permissions API for Minecraft";
		meta.version = VERSION;
		
		IPermission permCmd = PermissionFactory.getPermissionForArguments(PermissibleSetting.operator, "PermissionCommand", "Allow Permissions to be run", "forgePermissions.commands.permissioncontrol");
		Permissions.registerPermission(permCmd);
		Permissions.defaultPermsProvider = new YAMLPermissionsProvider();
		
		ForgeCoreModContainer.registerModDependency(this);
		
		
	}
	
	public boolean registerBus(EventBus b, LoadController c) {
		b.register(this);
		return true;
	}
	
	@Subscribe
	public void preInit(FMLPreInitializationEvent ev) {
		this.cfgDir = new File(ev.getModConfigurationDirectory(), "/permissions");
	}
	
	@Subscribe
	public void postInit(FMLPostInitializationEvent ev) {
		super.getMetadata().parentMod = Loader.instance().getIndexedModList().get("ForgeCore_Dummy");
	}
	
	@Subscribe
	public void serverStart(FMLServerStartingEvent ev) {
		ServerCommandManager scm = (ServerCommandManager) ev.getServer().getCommandManager();
		scm.registerCommand(new CommandPermission());
		Permissions.init();
	}

}
