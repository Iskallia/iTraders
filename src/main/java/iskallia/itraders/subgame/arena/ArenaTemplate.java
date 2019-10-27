package iskallia.itraders.subgame.arena;

import hellfirepvp.astralsorcery.common.structure.array.BlockArray;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class ArenaTemplate extends BlockArray {

    public BlockArray rotateYCCW() {
        BlockArray out = new BlockArray();
        for (BlockPos pos : pattern.keySet()) {
            BlockInformation info = pattern.get(pos);
            out.addBlock(new BlockPos(pos.getZ(), pos.getY(), -pos.getX()), info.state.withRotation(Rotation.COUNTERCLOCKWISE_90));
        }
        return out;
    }

}
