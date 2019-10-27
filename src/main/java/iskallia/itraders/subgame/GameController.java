package iskallia.itraders.subgame;

import hellfirepvp.astralsorcery.common.util.data.WorldBlockPos;
import iskallia.itraders.block.entity.TileEntitySubGameController;
import iskallia.itraders.subgame.arena.ArenaTemplate;
import iskallia.itraders.subgame.arena.ArenaTemplateManager;
import iskallia.itraders.subgame.input.GameInput;
import iskallia.itraders.subgame.lane.GameLane;
import iskallia.itraders.subgame.lane.LaneEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameController {

    private static final int LANE_AMOUNT = 5;
    private static final int LANE_LENGTH = 9;

    private final BlockPos offset;
    private final ArenaTemplate usedTemplate;
    private final EnumFacing direction;

    private GamePlayer player1 = null;
    private GamePlayer player2 = null;

    private List<GameLane> lanes = new LinkedList<>();
    private GamePhase currentPhase = GamePhase.STARTUP;
    private GameInput playerInput = null;

    public GameController(BlockPos offset, ArenaTemplate usedTemplate, EnumFacing direction) {
        this.offset = offset;
        this.usedTemplate = usedTemplate;
        this.direction = direction;

        buildLanes();
    }

    private void buildLanes() {
        //Super hardcoded stuff ahead. beware x)
        EnumFacing offsetDir = this.direction.rotateY();
        int lAmount = LANE_AMOUNT / 2;
        int length = LANE_LENGTH / 2;
        for (int offset = -lAmount; offset <= lAmount; offset++) {
            BlockPos offsetPos = this.offset.offset(offsetDir, offset);
            BlockPos from = offsetPos.offset(this.direction, length);
            BlockPos to = offsetPos.offset(this.direction, -length);
            this.lanes.add(new GameLane(from, to, this.direction));
        }
    }

    public ArenaTemplate getTemplate() {
        return usedTemplate;
    }

    public static GameController setupController(EntityPlayer gameCreator, BlockPos at, ArenaTemplate usedTemplate, EnumFacing templateDirection) {
        GameController ctrl = new GameController(at, usedTemplate, templateDirection);
        ctrl.player1 = new GamePlayer(gameCreator.getUniqueID());
        return ctrl;
    }

    public void writeToSaveNBT(NBTTagCompound tag) {
        tag.setInteger("orientation", this.direction.ordinal());
        tag.setString("template", ArenaTemplateManager.serializeArenaTemplate(this.usedTemplate));
    }

    public static GameController readFromSaveNBT(NBTTagCompound tag, BlockPos center) {
        EnumFacing direction = EnumFacing.values()[MathHelper.clamp(tag.getInteger("orientation"), 0, EnumFacing.values().length - 1)];
        ArenaTemplate template = ArenaTemplateManager.deserializeArenaTemplate(tag.getString("template"));
        return new GameController(center, template, direction);
    }

    public List<GameLane> getLanes() {
        return Collections.unmodifiableList(lanes);
    }

    public BlockPos getOffset() {
        return offset;
    }

    @Nullable
    public GamePlayer getIsPlayer(EntityPlayer vPlayer) {
        if (player1 != null && player1.isThisPlayer(vPlayer)) {
            return player1;
        }
        if (player2 != null && player2.isThisPlayer(vPlayer)) {
            return player2;
        }
        return null;
    }

    public boolean isPlayer1(GamePlayer player) {
        return this.player1 != null && this.player1.equals(player);
    }

    public boolean isPlayer2(GamePlayer player) {
        return this.player2 != null && this.player2.equals(player);
    }

    @Nullable
    public GameLane getInteractedLane(EntityPlayer player, BlockPos absPos) {
        if (player1 == null || player2 == null) {
            return null;
        }
        boolean isPlayer1 = this.player1.isThisPlayer(player);
        boolean isPlayer2 = this.player2.isThisPlayer(player);

        for (GameLane lane : lanes) {
            if ((isPlayer1 && lane.getInteractionPosFrom().equals(absPos)) ||
                    (isPlayer2 && lane.getInteractionPosTo().equals(absPos))) {
                return lane;
            }
        }
        return null;
    }

    public void tick(World world) {
        if (player1 != null && player2 != null) {
            if (player1.shouldKickDistance(world, v -> (float) v.squareDistanceTo(getOffset().getX(), getOffset().getY(), getOffset().getZ()))) {
                handleQuit(player1, player2);
                return;
            }
            if (player2.shouldKickDistance(world, v -> (float) v.squareDistanceTo(getOffset().getX(), getOffset().getY(), getOffset().getZ()))) {
                handleQuit(player2, player1);
                return;
            }
        }

        if (currentPhase.doesRunGameTick()) {
            lanes.forEach(GameLane::tick);
            boolean isFinished = true;
            for (GameLane lane : lanes) {
                if (!lane.areTasksFinished()) {
                    isFinished = false;
                }
            }
            if (isFinished) {
                currentPhase = currentPhase.getNextPhase();
            }
        } else if (!world.isRemote) {
            //Basically means we're currently waiting for ANY sort of input on serverside
            if (playerInput != null) {
                playerInput.processInput(this);
                playerInput = null;
            }
        }
    }

    private void handleQuit(GamePlayer left, GamePlayer winningPlayer) {
        //TODO inform disconnect or leaving game area of player 'left'
        handleGameWin(winningPlayer);
    }

    private void handleGameWin(GamePlayer winningPlayer) {
        //TODO handle win
    }

    @SideOnly(Side.CLIENT)
    public void renderTESRLaneContents(TileEntitySubGameController ctrl, double x, double y, double z, float pTicks) {
        this.lanes.forEach(lane -> {
            if (lane.getEntityPlayer1() != null) {
                lane.getEntityPlayer1().drawEntity(ctrl, this, x, y, z, pTicks);
            }
            if (lane.getEntityPlayer2() != null) {
                lane.getEntityPlayer2().drawEntity(ctrl, this, x, y, z, pTicks);
            }
        });
    }
}
