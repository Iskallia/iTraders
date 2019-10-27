package iskallia.itraders.subgame;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class GamePlayer {

    private final UUID playerUUID;

    public GamePlayer(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Nullable
    public EntityPlayer getPlayer(World world) {
        return world.getPlayerEntityByUUID(this.playerUUID);
    }

    public boolean isThisPlayer(EntityPlayer player) {
        return playerUUID.equals(player.getUniqueID());
    }

    public boolean hasLeft(World world) {
        return this.getPlayer(world) != null;
    }

    public boolean shouldKickDistance(World world, Function<Vec3d, Float> sqDstFunction) {
        EntityPlayer player = getPlayer(world);
        return player == null || sqDstFunction.apply(player.getPositionVector()) >= 900;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePlayer that = (GamePlayer) o;
        return Objects.equals(playerUUID, that.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUUID);
    }
}
