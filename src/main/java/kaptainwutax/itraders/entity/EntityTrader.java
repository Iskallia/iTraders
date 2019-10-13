package kaptainwutax.itraders.entity;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kaptainwutax.itraders.SkinProfile;
import kaptainwutax.itraders.init.InitConfig;
import kaptainwutax.itraders.util.Trade;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityTrader extends EntityVillager {

	public final SkinProfile skin = new SkinProfile();
	private String lastName = "Trader";

	public EntityTrader(World world) {
		super(world);
		this.setCustomNameTag(lastName);
	}

	@Override
	protected void updateAITasks() {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (this.world.isRemote) {
			String name = this.getCustomNameTag();

			if (!lastName.equals(name)) {
				this.skin.updateSkin(name);
				this.lastName = name;
			}
		}
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData livingdata1 = super.onInitialSpawn(difficulty, livingdata);
		this.setCustomTrades();
		this.setCustomNameTag(this.getCustomNameTag());
		return livingdata1;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
	}

	private void setCustomTrades() {
		MerchantRecipeList recipeList = new MerchantRecipeList();

		List<Trade> trades = InitConfig.CONFIG_TRADER.TRADES.stream().filter(trade -> trade.isValid())
				.collect(Collectors.toList());

		Collections.shuffle(trades);

		for (int i = 0; i < Math.min(trades.size(), InitConfig.CONFIG_TRADER.TRADES_COUNT); i++) {
			Trade trade = trades.get(i);
			if (trade == null)
				continue;

			recipeList.add(new MerchantRecipe(trade.getBuy() == null ? ItemStack.EMPTY : trade.getBuy().toStack(),
					trade.getExtra() == null ? ItemStack.EMPTY : trade.getExtra().toStack(),
					trade.getSell() == null ? ItemStack.EMPTY : trade.getSell().toStack(), 0,
					this.rand.nextInt(13) + 3));
		}

		Field tradesField = EntityVillager.class.getDeclaredFields()[7];
		tradesField.setAccessible(true);

		try {
			tradesField.set(this, recipeList);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
