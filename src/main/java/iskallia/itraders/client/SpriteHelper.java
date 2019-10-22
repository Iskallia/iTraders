package iskallia.itraders.client;

import java.util.HashMap;
import java.util.Map;

import hellfirepvp.astralsorcery.client.util.resource.BindableResource;
import hellfirepvp.astralsorcery.client.util.resource.SpriteSheetResource;
import iskallia.itraders.Traders;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class: SpriteHelper
 * Created by HellFirePvP
 * Date: 18.10.2019 / 06:24
 */
@SideOnly(Side.CLIENT)
public class SpriteHelper {

    private static Map<ResourceLocation, BindableResource> loadedUnmanagedTextures = new HashMap<>();

    public static SpriteSheetResource loadEffectSheet(String name, int sheetRows, int sheetColumns) {
        ResourceLocation key = Traders.getResource(String.format("textures/effect/%s.png", name));
        BindableResource res = loadedUnmanagedTextures.get(key);
        if (res == null) {
            res = new BindableResource(key.toString());
            loadedUnmanagedTextures.put(key, res);
        }
        return res.asSpriteSheet(sheetRows, sheetColumns);
    }
}