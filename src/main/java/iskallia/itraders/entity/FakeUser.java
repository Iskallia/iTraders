package iskallia.itraders.entity;

import com.mojang.authlib.GameProfile;

import iskallia.itraders.net.handler.FakeServerHandler;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class FakeUser extends FakePlayer {

	public FakeUser(WorldServer world, GameProfile name) {
		super(world, name);
		this.connection = new FakeServerHandler(this);
	}

}
