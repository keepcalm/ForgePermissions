package keepcalm.mods.permissions.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class PermisisonsLoadingPlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getLibraryRequestClass() {
		return new String[] {"keepcalm.mods.permissions.asm.SnakeYAMLDownloader"};
	}

	@Override
	public String[] getASMTransformerClass() {
		return null;
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

}
