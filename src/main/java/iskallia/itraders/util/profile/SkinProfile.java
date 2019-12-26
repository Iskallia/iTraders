package iskallia.itraders.util.profile;

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

import javax.annotation.Nullable;

public class SkinProfile {

	public static final ExecutorService SERVICE = Executors.newFixedThreadPool(5);

	private String latestNickname;
	public AtomicReference<GameProfile> gameProfile = new AtomicReference<GameProfile>();
	public AtomicReference<NetworkPlayerInfo> playerInfo = new AtomicReference<NetworkPlayerInfo>();

	@Nullable
	public String getLatestNickname() {
		return this.latestNickname;
	}

	public void updateSkin(String name) {
		if(name.equals(this.latestNickname))return;
		latestNickname = name;
		
		SERVICE.submit(() -> {
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
		SERVICE.submit(() -> {
			GameProfile output = TileEntitySkull.updateGameprofile(input);
			consumer.accept(output);
		});
	}

}
