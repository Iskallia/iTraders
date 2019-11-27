package iskallia.itraders.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.init.InitItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Traders.MOD_ID)
public class EventAnvil {

	private static final Pattern SKULL_NAME = Pattern.compile("(?<skullname>.*?)('s Head)?", Pattern.CASE_INSENSITIVE);

	@SubscribeEvent
	public static void onSkullRenamed(AnvilRepairEvent event) {
		ItemStack stack = event.getItemResult();

		if(stack.getItem() != Items.SKULL)return;

		Matcher matcher = SKULL_NAME.matcher(stack.getDisplayName());

		if(matcher.matches()) {
			String skullName = matcher.group("skullname");
			NBTTagCompound nbt = stack.getTagCompound();
			
			if(nbt != null) {
				nbt.setString("SkullOwner", skullName);
				stack.setTagCompound(nbt);
			}
		}
	}

	@SubscribeEvent
	public static void onMinerCrafted(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		Item rightItem = right.getItem();
		
		if(left.getItem() != InitItem.SPAWN_EGG_FIGHTER)return;
		if(!rightItem.getToolClasses(right).contains("pickaxe"))return;
		
		ItemStack minerEgg = new ItemStack(InitItem.SPAWN_EGG_MINER, 1);
		if(left.hasTagCompound())minerEgg.setTagCompound(left.getTagCompound());
		if(left.hasDisplayName())minerEgg.setStackDisplayName(left.getDisplayName());
		
		if(!minerEgg.hasTagCompound())minerEgg.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = minerEgg.getTagCompound();
		if(!nbt.hasKey("EntityTag"))nbt.setTag("EntityTag", new NBTTagCompound());
		NBTTagCompound entityTag = nbt.getCompoundTag("EntityTag");
		
		NBTTagList handItems = entityTag.getTagList("HandItems", 10);
		NBTTagCompound minerEggNbt = right.writeToNBT(new NBTTagCompound());
		handItems.appendTag(minerEggNbt);
		handItems.appendTag(new NBTTagCompound());
		entityTag.setTag("HandItems", handItems);
		
		event.setCost(1);
		event.setOutput(minerEgg);
	}
	
	@SubscribeEvent
	public static void onFighterRescaled(AnvilUpdateEvent event) {
		int amount = getScalingAmount(event.getLeft(), event.getRight());
		if(amount == 0)return;
		
		ItemStack left = event.getLeft().copy();
		ItemStack output = left.copy();
		
		if(left.hasTagCompound())output.setTagCompound(left.getTagCompound());
		if(left.hasDisplayName())output.setStackDisplayName(left.getDisplayName());
		
		if(!output.hasTagCompound())output.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = output.getTagCompound();
		
		if(!nbt.hasKey("EntityTag", Constants.NBT.TAG_COMPOUND)) {
			nbt.setTag("EntityTag", new NBTTagCompound());
		}
		
		NBTTagCompound entityTag = nbt.getCompoundTag("EntityTag");
		
		if(!entityTag.hasKey("SubData", Constants.NBT.TAG_COMPOUND)) {
			entityTag.setTag("SubData", new NBTTagCompound());
		}
		
		NBTTagCompound subData = entityTag.getCompoundTag("SubData");	
		
		subData.setInteger("Months", subData.getInteger("Months") + amount);
		
		event.setOutput(output);
		event.setCost(Math.abs(amount));
		event.setMaterialCost(Math.abs(amount));
	}
	
	@SubscribeEvent
	public static void onAcceleratorCharge(AnvilUpdateEvent event) {
		if (!event.getRight().hasDisplayName())
			return;
		String name = event.getRight().getDisplayName();

		int duration = getDuration(event.getLeft(), event.getRight());
		if (duration == 0)
			return;

		ItemStack left = event.getLeft();
		ItemStack output = left.copy();

		if (!output.hasTagCompound())
			output.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = output.getTagCompound();

		int subCount = nbt.getInteger("SubCount");

		if (!nbt.hasKey("SelectedSub"))
			nbt.setInteger("SelectedSub", 0);

		// TODO: 10 needs to be configurable
		if (subCount >= 10)
			return;

		nbt.setInteger("SubCount", subCount + 1);

		if (!nbt.hasKey("SubList"))
			nbt.setTag("SubList", new NBTTagList());
		NBTTagList subList = nbt.getTagList("SubList", Constants.NBT.TAG_COMPOUND);

		NBTTagCompound newSub = new NBTTagCompound();
		newSub.setString("Name", name);
		newSub.setInteger("Duration", duration);

		subList.appendTag(newSub);

		output.setTagCompound(nbt);

		event.setOutput(output);
		event.setCost(duration / 10);
		event.setMaterialCost(1);
	}

	private static int getDuration(ItemStack left, ItemStack right) {
		int duration = 0;
		
		if(left.getItem() != InitItem.ACCELERATION_BOTTLE)return duration;
		if(right.getItem() != InitItem.SPAWN_EGG_FIGHTER)return duration;
		
		if(!right.hasTagCompound()) return duration;
		
		NBTTagCompound nbt = right.getTagCompound();
		
		if(!nbt.hasKey("EntityTag")) return duration;
		
		NBTTagCompound entityTag = nbt.getCompoundTag("EntityTag");
		
		if(!entityTag.hasKey("SubData")) return duration;
		
		NBTTagCompound subData = entityTag.getCompoundTag("SubData");
		
		int months = subData.getInteger("Months");
		
		//TODO: set 10 this configurable
		duration = months * 10;
				
		return duration;
	}

	private static int getScalingAmount(ItemStack left, ItemStack right) {
		if(!InitConfig.CONFIG_FIGHTER.CRAFTABLE_EGGS)return 0;
		
		if(left.getItem() != InitItem.SPAWN_EGG_FIGHTER)return 0;
		if(right.getItem() != Items.EMERALD && right.getItem() != Items.ROTTEN_FLESH)return 0;
		
		int signum = right.getItem() == Items.EMERALD ? 1 : -1;
		
		int count = right.getCount();
		int months = InitItem.SPAWN_EGG_FIGHTER.getMonths(left);
		if(months < 0)months = 0;
		
		if(months + count * signum < 0) {
			count = months;
		}
		
		return count * signum;
	}

}
