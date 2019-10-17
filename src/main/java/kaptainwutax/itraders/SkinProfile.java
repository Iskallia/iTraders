package kaptainwutax.itraders;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkinProfile {

	public static final ExecutorService service = Executors.newFixedThreadPool(5);

	public AtomicReference<GameProfile> gameProfile = new AtomicReference<GameProfile>();
	public AtomicReference<NetworkPlayerInfo> playerInfo = new AtomicReference<NetworkPlayerInfo>();

	public void updateSkin(String name) {
		service.submit(() -> {
			gameProfile.set(new GameProfile(null, name));
			gameProfile.set(TileEntitySkull.updateGameprofile(gameProfile.get()));
			playerInfo.set(new NetworkPlayerInfo(gameProfile.get()));
		});
	}

	@SideOnly(value = Side.CLIENT)
	public ResourceLocation getLocationSkin() {
		if (this.playerInfo == null || this.playerInfo.get() == null) {
			return DefaultPlayerSkin.getDefaultSkinLegacy();
		}

		try {
			return this.playerInfo.get().getLocationSkin();
		} catch (Exception e) {
			System.err.println("stupid! how did you even do this?");
			e.printStackTrace();
		}

		return DefaultPlayerSkin.getDefaultSkinLegacy();
	}

	public static void updateGameProfile(GameProfile input, Consumer<GameProfile> consumer) {
		service.submit(() -> {
			GameProfile output = TileEntitySkull.updateGameprofile(input);
			consumer.accept(output);
		});
	}

}
