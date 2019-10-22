package iskallia.itraders.block.entity;

import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityCryoChamber extends TileEntity implements ITickable {

    private SkinProfile skin = new SkinProfile();
    private int cryoTicks; // TODO: Rename this to a better name (?) D:

    public void updateSkin(String username) {
        skin.updateSkin(username);
    }

    public SkinProfile getSkin() {
        return skin;
    }

    @Override
    public void update() {
        // TODO
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        // TODO
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // TODO
        return super.writeToNBT(compound);
    }

}
