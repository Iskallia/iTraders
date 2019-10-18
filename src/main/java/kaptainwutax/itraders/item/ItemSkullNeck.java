package kaptainwutax.itraders.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.config.ConfigSkullNecklace;
import kaptainwutax.itraders.config.definition.PotionEffectDefinition;
import kaptainwutax.itraders.entity.EntityMiniGhost;
import kaptainwutax.itraders.init.InitConfig;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.util.Randomizer;
import kaptainwutax.itraders.util.RomanLiterals;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

/*
 * NBT: {
 *     HeadOwner: "iGoodie",
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
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (player.world.isRemote) return;

        EntityMiniGhost ghost = GHOST_MAP.get(player.getUniqueID());

        if (ghost == null) return;

        ghost.setPositionAndRotation(
                player.posX,
                player.posY + 1.60f,
                player.posZ,
                ghost.rotationYaw,
                player.rotationPitch
        );
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        if (!player.world.isRemote) {
            List<PotionEffect> potionEffects = getPotionEffects(itemstack);

            if (potionEffects != null) {
                for (PotionEffect potionEffect : potionEffects) {
                    player.addPotionEffect(potionEffect);
                }
            }

            createMiniGhostFor(player, itemstack);
        }
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        if (!player.world.isRemote) {
            List<PotionEffect> potionEffects = getPotionEffects(itemstack);

            if (potionEffects != null) {
                for (PotionEffect potionEffect : potionEffects) {
                    player.removePotionEffect(potionEffect.getPotion());
                }
            }

            removeMiniGhostOf(player);
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

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
