package keepcalm.mods.forgecore;

import static keepcalm.mods.forgecore.permissions.asm.PermissionsContainer.VERSION;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;


/**
 * Dummy mod which acts as a parent for other mods in this package
 * @author keepcalm
 *
 */
@Mod(modid="ForgeCore_Dummy",version=VERSION,name="ForgeCore")
public class ForgeCoreModContainer {
	
	@Metadata("ForgeCore_Dummy")
	public static ModMetadata myMeta = new ModMetadata();
	
	private static List<ModContainer> mods = new ArrayList<ModContainer>();
	
	private static FMLModContainer myMC;
	/**
	 * Add a mod to this mod's child mods list.
	 * To get the mod container for a \@Mod then you want to
	 * use Loader.instance().getIndexedModList().get(modID);
	 * @param mc
	 */
	public static void registerModDependency(ModContainer mc) {
		mods.add(mc);
	}
	
	public ForgeCoreModContainer() {
		myMC =  (FMLModContainer) Loader.instance().getIndexedModList().get("ForgeCore_Dummy");
		myMeta = myMC.getMetadata();
		System.out.println(myMeta);
		myMeta.modId = "ForgeCore_Dummy";
		myMeta.version = VERSION;
		myMeta.name = "ForgeCore";
		myMeta.description = "A common base for all the mods which take common Bukkit functionality onto MinecraftForge.";
		
	}
	
	@Init
	public void postInit(FMLInitializationEvent ev) {
		myMeta.childMods = mods;
		myMeta = Loader.instance().getIndexedModList().get("ForgeCore_Dummy").getMetadata();
		
		for (Field i : myMeta.getClass().getFields()) {
			
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
				
		}
 	}

}