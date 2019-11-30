package iskallia.itraders.block.render;

import iskallia.itraders.block.BlockPowerCube;
import iskallia.itraders.item.mesh.ItemMesh;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PowerCubeMesh extends ItemMesh {

    public PowerCubeMesh(ItemBlock item) {
        super(item);

        this.addMesh(this.createMesh("power_cube_common"));
        this.addMesh(this.createMesh("power_cube_rare"));
        this.addMesh(this.createMesh("power_cube_epic"));
        this.addMesh(this.createMesh("power_cube_mega"));
        this.bakeMeshes();
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack cubeStack) {
        NBTTagCompound cubeNBT = cubeStack.getTagCompound();

        if (cubeNBT == null)
            return meshes[0];

        BlockPowerCube.CubeRarity rarity = BlockPowerCube.CubeRarity
                .values()[cubeNBT.getInteger("Rarity")];

        switch (rarity) {
            case RARE: return meshes[1];
            case EPIC: return meshes[2];
            case MEGA: return meshes[3];
            default: return meshes[0];
        }
    }

}
