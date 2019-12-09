package iskallia.itraders.init;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.*;
import iskallia.itraders.block.render.*;
import iskallia.itraders.item.mesh.FighterEggMesh;
import iskallia.itraders.item.mesh.MinerEggMesh;
import iskallia.itraders.item.mesh.SkullNeckMesh;
import iskallia.itraders.item.mesh.SubCardMesh;
import iskallia.itraders.item.mesh.TraderEggMesh;
import iskallia.itraders.block.render.TESRCryoChamber;
import iskallia.itraders.block.render.TESRGraveStone;
import iskallia.itraders.block.render.TESRVendingMachine;
import iskallia.itraders.item.mesh.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class InitModel {

    public static void registerItemModels() {
        registerSimpleItemModel(InitItem.BIT_100, 0);
        registerSimpleItemModel(InitItem.BIT_500, 0);
        registerSimpleItemModel(InitItem.BIT_1000, 0);
        registerSimpleItemModel(InitItem.BIT_5000, 0);
        registerSimpleItemModel(InitItem.BIT_10000, 0);
        registerSimpleItemModel(InitItem.EGG_POUCH, 0);
        registerSimpleItemModel(InitItem.MAGIC_ORE_DUST, 0);
        registerSimpleItemModel(InitBlock.ITEM_GRAVE_STONE, 0);
        registerSimpleItemModel(InitBlock.ITEM_INFUSION_CAULDRON, 0);
        registerSimpleItemModel(InitItem.CRYO_CHAMBER, 0);
        registerSimpleItemModel(InitItem.POWER_CHAMBER, 0);
        registerSimpleItemModel(InitBlock.ITEM_GHOST_PEDESTAL, 0);
        registerSimpleItemModel(InitBlock.ITEM_MAGIC_ORE_BLOCK, 0);
        registerSimpleItemModel(InitItem.BOOSTER_TIER_1, 0);
        registerSimpleItemModel(InitItem.BOOSTER_TIER_2, 0);
        registerSimpleItemModel(InitItem.BOOSTER_TIER_3, 0);
        registerSimpleItemModel(InitItem.BOOSTER_TIER_4, 0);
        registerSimpleItemModel(InitItem.BOOSTER_TIER_5, 0);
        registerSimpleItemModel(InitItem.MAGIC_CRYSTAL, 0);
        registerSimpleItemModel(InitItem.RAENS22_STICK, 0);
        registerSimpleItemModel(InitItem.ITEM_VENDING_MACHINE, 0);
        registerSimpleItemModel(InitItem.ACCELERATION_BOTTLE, 0);
        registerSimpleItemModel(InitBlock.ITEM_CUBE_CHAMBER, 0);

        registerBlockModel(InitBlock.INFUSION_CAULDRON, 0);
        registerBlockModel(InitBlock.GHOST_PEDESTAL, 0);
        registerBlockModel(InitBlock.MAGIC_ORE_BLOCK, 0);
        registerBlockModel(InitBlock.CUBE_CHAMBER, 0); // TODO: Do the blockstate magic for infusion percentage
//        registerBlockModel(InitBlock.VENDING_MACHINE, 0); // Dis buddy is now placed with an item directly

        ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_TRADER, new TraderEggMesh(InitItem.SPAWN_EGG_TRADER));
        ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_FIGHTER, new FighterEggMesh(InitItem.SPAWN_EGG_FIGHTER));
        ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_MINER, new MinerEggMesh(InitItem.SPAWN_EGG_MINER));
        ModelLoader.setCustomMeshDefinition(InitItem.SKULL_NECKLACE, new SkullNeckMesh(InitItem.SKULL_NECKLACE));
        ModelLoader.setCustomMeshDefinition(InitItem.SUB_CARD, new SubCardMesh(InitItem.SUB_CARD));
        ModelLoader.setCustomMeshDefinition(InitBlock.ITEM_POWER_CUBE, new PowerCubeMesh(InitBlock.ITEM_POWER_CUBE));
        ModelLoader.setCustomMeshDefinition(InitItem.CARDBOARD_BOX, new CardboardBoxMesh(InitItem.CARDBOARD_BOX));
    }

    public static void registerTileEntityRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGraveStone.class, new TESRGraveStone());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCryoChamber.class, new TESRCryoChamber());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPowerChamber.class, new TESRPowerChamber());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPowerCube.class, new TESRPowerCube());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVendingMachine.class, new TESRVendingMachine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCubeChamber.class, new TESRCubeChamber());
    }

    /* ---------------------------------- */

    private static void registerSimpleItemModel(Item item, int metadata) {
        ModelLoader.setCustomModelResourceLocation(item, metadata,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    private static void registerBlockModel(Block block, int metadata) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), metadata,
                new ModelResourceLocation(Traders.getResource(block.getUnlocalizedName().substring(5)), "inventory"));
    }
}
