package kaptainwutax.itraders;

import java.util.Map;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
public class MixinForgeLoader implements IFMLLoadingPlugin {

    public MixinForgeLoader() {
    	System.out.println("STARTING MIXINS!=========================");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.itraders.json");
    }
    
	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
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

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
