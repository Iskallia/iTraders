package kaptainwutax.itraders.subgame;

import hellfirepvp.astralsorcery.common.structure.array.BlockArray;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

/**
 * Class: ArenaTemplate
 * Created by HellFirePvP
 * Date: 19.10.2019 / 09:23
 */
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
