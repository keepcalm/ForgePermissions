package keepcalm.mods.permissions.asm;

import java.io.File;
import java.util.Arrays;

import keepcalm.mods.permissions.api.IPermission;
import keepcalm.mods.permissions.api.PermissibleSetting;
import keepcalm.mods.permissions.api.PermissionFactory;
import keepcalm.mods.permissions.api.Permissions;
import keepcalm.mods.permissions.internals.YAMLPermissionsProvider;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class PermissionsContainer extends DummyModContainer {
	public static File cfgDir;
	
	
	public PermissionsContainer() {
		super(new ModMetadata());
		ModMetadata meta = super.getMetadata();
		meta.authorList = Arrays.asList(new String[] {"keepcalm"});
		meta.modId = "Permissions";
		meta.name = "Permissions";
		meta.description = "A simple permissions API for Minecraft";
		meta.version = "1.4.2-0";
		
		IPermission permCmd = PermissionFactory.getPermissionForArguments(PermissibleSetting.operator, "PermissionCommand", "Allow Permissions to be run", "forgePermissions.commands.permissioncontrol");
		Permissions.registerPermission(permCmd);
		Permissions.defaultPermsProvider = new YAMLPermissionsProvider();
		
		
		
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
	public void serverStart(FMLServerStartingEvent ev) {
		Permissions.init();
	}

}
