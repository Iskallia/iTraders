package iskallia.itraders.net.packet;

import io.netty.buffer.ByteBuf;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorCore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SReactorToggle implements IMessage {

    private int x, y, z;

    public C2SReactorToggle() {}

    public C2SReactorToggle(TileEntityReactorCore reactorCore) {
        BlockPos pos = reactorCore.getPos();

        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    /* ------------------------ */

    public static class C2SReactorToggleHandler implements IMessageHandler<C2SReactorToggle, IMessage> {

        @Override
        public IMessage onMessage(C2SReactorToggle message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            WorldServer world = player.getServerWorld();

            TileEntity tileEntity = world.getTileEntity(new BlockPos(message.x, message.y, message.z));

            if(tileEntity instanceof TileEntityReactorCore) {
                TileEntityReactorCore reactorCore = (TileEntityReactorCore) tileEntity;
                reactorCore.toggle();
            }

            return null;
        }

    }

}
