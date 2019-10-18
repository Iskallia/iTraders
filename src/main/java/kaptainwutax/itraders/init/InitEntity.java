package kaptainwutax.itraders.init;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.entity.*;
import kaptainwutax.itraders.entity.render.*;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class InitEntity {

	private static int ID = 0;

	public static void registerEntities() {
		registerEntityAndEgg("trader", EntityTrader.class, 0xF5F5DC, 0x8B4513);
		registerEntity("fighter", EntityFighter.class);
		registerEntity("miner", EntityMiner.class);
		registerEntity("mini_ghost", EntityMiniGhost.class);
		registerEntity("item_magic_ore_dust", EntityItemMagicOreDust.class);
	}

	public static void registerEntityRenderers() {
		registerEntityRenderer(EntityTrader.class, RenderTrader.getRenderFactory());
		registerEntityRenderer(EntityFighter.class, RenderFighter.getRenderFactory());
		registerEntityRenderer(EntityMiner.class, RenderMiner.getRenderFactory());
		registerEntityRenderer(EntityMiniGhost.class, RenderMiniGhost.getRenderFactory());
		registerEntityRenderer(EntityItemMagicOreDust.class, RenderItemMagicOreDust.getRenderFactory());
	}

	private static void registerEntity(String name, Class<? extends Entity> entityClass) {
		EntityRegistry.registerModEntity(Traders.getResource(name), entityClass, name, InitEntity.nextId(),
				Traders.getInstance(), 64, 1, true);
	}

	private static void registerEntityAndEgg(String name, Class<? extends Entity> entityClass, int primaryEggColor,
			int secondaryEggColor) {
		EntityRegistry.registerModEntity(Traders.getResource(name), entityClass, name, InitEntity.nextId(),
				Traders.getInstance(), 64, 1, true, primaryEggColor, secondaryEggColor);
	}

	private static void registerEntityRenderer(Class<? extends Entity> entityClass, IRenderFactory renderFactory) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
	}

	private static int nextId() {
		return InitEntity.ID++;
	}

}
