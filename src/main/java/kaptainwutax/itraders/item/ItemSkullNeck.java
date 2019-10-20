package kaptainwutax.itraders.item;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.config.definition.PotionEffectDefinition;
import kaptainwutax.itraders.entity.EntityMiniGhost;
import kaptainwutax.itraders.init.InitConfig;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.util.RomanLiterals;
import kaptainwutax.itraders.util.math.Randomizer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/*
 * NBT: {
 *     HeadOwner: "iGoodie",
 *     MagicPower: 1234 (integer),
 *     PotionEffects: [
 *          { name:"mining_fatigue", amplifier:5 },
 *          { name:"haste", amplifier:1 }
 *     ]
 * }
 */
public class ItemSkullNeck extends Item implements IBauble {

    public static ItemStack generateRandom(String headOwner) {
        ItemStack stack = new ItemStack(InitItem.SKULL_NECKLACE, 1, 0);
        NBTTagCompound stackNBT = new NBTTagCompound();

        stackNBT.setString("HeadOwner", headOwner);
        stackNBT.setInteger("MagicPower", 36000);

        int numEffects = Randomizer.randomInt(
                InitConfig.CONFIG_SKULL_NECKLACE.NECKLACE_EFFECT_COUNT.getMin(),
                InitConfig.CONFIG_SKULL_NECKLACE.NECKLACE_EFFECT_COUNT.getMax()
        );

        NBTTagList potionEffects = new NBTTagList();

        for (PotionEffectDefinition effect : InitConfig.CONFIG_SKULL_NECKLACE.getRandomEffect(numEffects)) {
            NBTTagCompound effectNBT = new NBTTagCompound();
            effectNBT.setString("name", effect.getName());
            effectNBT.setInteger("amplifier", Randomizer.randomInt(
                    effect.getAmplifier().getMin(),
                    effect.getAmplifier().getMax()));
            potionEffects.appendTag(effectNBT);
        }

        stackNBT.setTag("PotionEffects", potionEffects);
        stack.setTagCompound(stackNBT);
        return stack;
    }

    public static List<PotionEffect> getPotionEffects(ItemStack stack) {
        NBTTagCompound stackNBT = stack.getTagCompound();

        if (stackNBT == null || !stackNBT.hasKey("PotionEffects", 9)) // 9 - LIST
            return null;

        List<PotionEffect> potionEffectList = new LinkedList<>();

        NBTTagList potionEffects = stackNBT.getTagList("PotionEffects", 10);
        for (NBTBase potionEffect : potionEffects) {
            String name = ((NBTTagCompound) potionEffect).getString("name");
            int amplifier = Math.max(1, ((NBTTagCompound) potionEffect).getInteger("amplifier"));

            if (name.isEmpty())
                continue; // Malformed PotionEffects entry

            Potion potion = Potion.getPotionFromResourceLocation(name);

            if (potion == null)
                continue; // Unknown potion name

            potionEffectList.add(new PotionEffect(potion, Integer.MAX_VALUE, amplifier, false, false));
        }

        return potionEffectList;
    }

    public static final Map<UUID, EntityMiniGhost> GHOST_MAP = new HashMap<>();

    public static EntityMiniGhost createMiniGhostFor(EntityLivingBase player, ItemStack stack) {
        EntityMiniGhost ghost = ((EntityMiniGhost) EntityList.createEntityByIDFromName(
                Traders.getResource("mini_ghost"), player.world));

        assert ghost != null;

        ghost.setParentUUID(player.getUniqueID());
        ghost.setCustomNameTag(stack.getTagCompound().getString("HeadOwner"));
        ghost.setPosition(player.posX, player.posY, player.posZ);
        player.world.spawnEntity(ghost);

        // Remove and overwrite old ghost
        EntityMiniGhost oldGhost = GHOST_MAP.put(player.getUniqueID(), ghost);
        if (oldGhost != null) oldGhost.world.removeEntity(oldGhost);

        return ghost;
    }

    public static void removeMiniGhostOf(EntityLivingBase player) {
        EntityMiniGhost ghost = GHOST_MAP.remove(player.getUniqueID());
        if (ghost != null) ghost.world.removeEntity(ghost);
    }

    public ItemSkullNeck(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setMaxStackSize(1);
        this.setMaxDamage(100); // Will act like the percentage value
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (player.world.isRemote) return;

        EntityMiniGhost ghost = GHOST_MAP.get(player.getUniqueID());

        if (ghost != null) {
            ghost.setPositionAndRotation(
                    player.posX,
                    player.posY + 1.60f,
                    player.posZ,
                    ghost.rotationYaw,
                    player.rotationPitch
            );
        }

        // Re-apply potion effects
        List<PotionEffect> potionEffects = getPotionEffects(itemstack);
        if (potionEffects != null && getMagicPower(itemstack) > 0) {
            addEffectsTo(player, potionEffects);
        }

        // Consume magic power
        boolean drained = consumeMagicPower(itemstack, 1);
        if (drained) onMagicPowerDrained(itemstack, player);

        // Re-calculate magic power percentage
        int damagePercent = 100 - (int) (100 * (getMagicPower(itemstack) / 36_000f));
        itemstack.setItemDamage(damagePercent);
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        if (!player.world.isRemote) {
            createMiniGhostFor(player, itemstack);
        }
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        if (!player.world.isRemote) {
            List<PotionEffect> potionEffects = getPotionEffects(itemstack);

            if (potionEffects != null) {
                removeEffectsFrom(player, potionEffects);
            }

            removeMiniGhostOf(player);
        }
    }

    public void onMagicPowerDrained(ItemStack stack, EntityLivingBase player) {
        List<PotionEffect> potionEffects = getPotionEffects(stack);
        if (potionEffects != null) removeEffectsFrom(player, potionEffects);
        player.world.playSound(null,
                player.posX, player.posY, player.posZ,
                SoundEvents.ENTITY_ITEM_BREAK,
                SoundCategory.MASTER,
                1.0f, (float) Math.random());
    }

    public void addEffectsTo(EntityLivingBase player, List<PotionEffect> potionEffects) {
        for (PotionEffect potionEffect : potionEffects) {
            player.addPotionEffect(potionEffect);
        }
    }

    public void removeEffectsFrom(EntityLivingBase player, List<PotionEffect> potionEffects) {
        for (PotionEffect potionEffect : potionEffects) {
            player.removePotionEffect(potionEffect.getPotion());
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound stackNBT = stack.getTagCompound();

        if (stackNBT != null && stackNBT.hasKey("HeadOwner")) {
            tooltip.add("Ghost: " + stackNBT.getString("HeadOwner"));
        }

        List<PotionEffect> potionEffects = getPotionEffects(stack);

        if (potionEffects != null) {
            for (PotionEffect potionEffect : potionEffects) {
                String effectTranslationKey = potionEffect.getEffectName();

                TextFormatting color = potionEffect.getPotion().isBadEffect()
                        ? TextFormatting.RED
                        : TextFormatting.GREEN;

                String effectName = I18n.format(effectTranslationKey);
                String amplifier = RomanLiterals.translate(potionEffect.getAmplifier() + 1);

                tooltip.add(color + effectName + " " + amplifier);
            }
        }

        int magicPower = getMagicPower(stack);

        tooltip.add("");
        tooltip.add(magicPower <= 0
                ? TextFormatting.RED + "Magic powers drained"
                : TextFormatting.BLUE + "Magic power: " + magicPower + " ticks");

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public int getMagicPower(ItemStack stack) {
        NBTTagCompound stackNBT = stack.getTagCompound();

        if (stackNBT == null || !stackNBT.hasKey("MagicPower", Constants.NBT.TAG_INT))
            return -1;

        return stackNBT.getInteger("MagicPower");
    }

    public boolean consumeMagicPower(ItemStack stack, int amount) {
        NBTTagCompound stackNBT = stack.getTagCompound();
        int magicPower = getMagicPower(stack);

        if (stackNBT == null || magicPower == -1) {
            stack.setTagCompound(new NBTTagCompound());
            stackNBT = stack.getTagCompound();
        }

        int remainingMagicPower = Math.max(0, magicPower - amount);

        stackNBT.setInteger("MagicPower", remainingMagicPower);

        // Changed to 0 from a non-0
        return magicPower != 0 && remainingMagicPower == 0;
    }

}
