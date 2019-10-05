package kaptainwutax.itraders.item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpawnEggFighter extends Item {

    public static boolean shouldSpawnEntity(NBTTagCompound compound) {
        if(compound == null)return true;
        
        if (!compound.hasKey("EntityTag", 10))return true;
        NBTTagCompound entityTagNBT = compound.getCompoundTag("EntityTag");
        
        if(!entityTagNBT.hasKey("SizeMultiplier"))return true;
        float sizeMultiplier = entityTagNBT.getFloat("SizeMultiplier");

        double percentage = MathHelper.clamp(sizeMultiplier / 5.0f, 0.1f, 1.0f);
        return Math.random() < percentage;
    }

    public ItemSpawnEggFighter(String name) {
        this.setTranslationKey(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        } else if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (block == Blocks.MOB_SPAWNER) {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntityMobSpawner) {
                    MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic();
                    mobspawnerbaselogic.setEntityId(getNamedIdFrom(stack));
                    tileentity.markDirty();
                    worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            BlockPos blockpos = pos.offset(facing);
            double d0 = this.getYOffset(worldIn, blockpos);

            NBTTagCompound stackNBT = stack.getTagCompound();

            if (stack.hasDisplayName() && !shouldSpawnEntity(stackNBT)) {
                ItemStack headDrop = new ItemStack(Items.SKULL, 1, 3);
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("SkullOwner", stack.getDisplayName());
                headDrop.setTagCompound(nbt);
                EntityItem itemItem = new EntityItem(worldIn, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5D, headDrop);
                worldIn.spawnEntity(itemItem);
            } else {
                Entity entity = ItemSpawnEggFighter.spawnCreature(worldIn, ItemSpawnEggFighter.getNamedIdFrom(stack), (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5D);
                if(stack.hasDisplayName())entity.setCustomNameTag(UUID.randomUUID().toString());
                ItemMonsterPlacer.applyItemEntityDataToEntity(worldIn, (EntityPlayer) null, stack, entity);
            }

            if(!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }
    }

    protected double getYOffset(World world, BlockPos pos) {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(pos)).expand(0.0D, -1.0D, 0.0D);
        List<AxisAlignedBB> list = world.getCollisionBoxes((Entity) null, axisalignedbb);

        if (list.isEmpty()) {
            return 0.0D;
        } else {
            double d0 = axisalignedbb.minY;

            for (AxisAlignedBB axisalignedbb1 : list) {
                d0 = Math.max(axisalignedbb1.maxY, d0);
            }

            return d0 - (double) pos.getY();
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (worldIn.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
        }

        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

        if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid)) {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
            } else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, stack)) {
                NBTTagCompound stackNBT = stack.getTagCompound();

                if (stack.hasDisplayName() && !shouldSpawnEntity(stackNBT)) {
                    ItemStack headDrop = new ItemStack(Items.SKULL, 1, 3);
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setString("SkullOwner", stack.getDisplayName());
                    headDrop.setTagCompound(nbt);
                    EntityItem itemItem = new EntityItem(worldIn, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D, (double) blockpos.getZ() + 0.5D, headDrop);
                    worldIn.spawnEntity(itemItem);
                } else {
                    Entity entity = ItemSpawnEggFighter.spawnCreature(worldIn, ItemSpawnEggFighter.getNamedIdFrom(stack), (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D, (double) blockpos.getZ() + 0.5D);
                    if (stack.hasDisplayName()) entity.setCustomNameTag(stack.getDisplayName());
                    ItemMonsterPlacer.applyItemEntityDataToEntity(worldIn, (EntityPlayer) null, stack, entity);
                }

                if (!playerIn.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }

                playerIn.addStat(StatList.getObjectUseStats(this));
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
            } else {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
            }
        } else {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
        }
    }

    @Nullable
    public static ResourceLocation getNamedIdFrom(ItemStack stack) {
        return Traders.getResource("fighter");
    }

    @Nullable
    public static Entity spawnCreature(World worldIn, @Nullable ResourceLocation entityID, double x, double y, double z) {
        if (entityID != null) {
            Entity entity = null;
            entity = EntityList.createEntityByIDFromName(entityID, worldIn);

            if (entity instanceof EntityLiving) {
                EntityLiving entityliving = (EntityLiving) entity;
                entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                entityliving.rotationYawHead = entityliving.rotationYaw;
                entityliving.renderYawOffset = entityliving.rotationYaw;
                entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData) null);
                worldIn.spawnEntity(entity);
                entityliving.playLivingSound();
            }

            return entity;
        } else {
            return null;
        }
    }

    public static void applyItemEntityDataToEntity(World entityWorld, @Nullable EntityPlayer player, ItemStack stack, @Nullable Entity targetEntity) {
        MinecraftServer minecraftserver = entityWorld.getMinecraftServer();

        if (minecraftserver != null && targetEntity != null) {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound != null && nbttagcompound.hasKey("EntityTag", 10)) {
                if (!entityWorld.isRemote && targetEntity.ignoreItemEntityData() && (player == null || !minecraftserver.getPlayerList().canSendCommands(player.getGameProfile()))) {
                    return;
                }

                NBTTagCompound nbttagcompound1 = targetEntity.writeToNBT(new NBTTagCompound());
                UUID uuid = targetEntity.getUniqueID();
                nbttagcompound1.merge(nbttagcompound.getCompoundTag("EntityTag"));
                targetEntity.setUniqueId(uuid);
                targetEntity.readFromNBT(nbttagcompound1);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag isAdvanced) {
    	if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
    		tooltip.add("Press shift for more...");
    		return;
    	}   
    	
		if(!stack.hasTagCompound())return;
		NBTTagCompound stackTag = stack.getTagCompound();

		if(!stackTag.hasKey("EntityTag"))return;
		NBTTagCompound entityTag = stackTag.getCompoundTag("EntityTag");

		if(!entityTag.hasKey("SubData"))return;
		NBTTagCompound subTag = entityTag.getCompoundTag("SubData");

		if(subTag.hasKey("Months")) {
			int months = subTag.getInteger("Months");
			StringBuilder sb = new StringBuilder();
			sb.append(TextFormatting.GRAY + "Months: ");
			sb.append(TextFormatting.GREEN + String.valueOf(months));
			tooltip.add(sb.toString());
		}
		
		if(subTag.hasKey("Time")) {
			long time = subTag.getLong("Time") * 1000;
			SimpleDateFormat date1 = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat date2 = new SimpleDateFormat("yyyy-MM-dd");
			date1.setTimeZone(TimeZone.getTimeZone("UTC"));
			date2.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			StringBuilder sb = new StringBuilder();
			sb.append(TextFormatting.GRAY + "Time: ");
			sb.append(TextFormatting.GREEN + date2.format(new Date(time)));
			sb.append(" ");
			sb.append(TextFormatting.GREEN + date1.format(new Date(time)));
			tooltip.add(sb.toString());
		}
    }

}
