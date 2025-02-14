package uwu.lopyluna.create_dd.content.blocks.functional;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AxisBlock extends RotatedPillarBlock {
    boolean hasStrippedWood;
    BlockState strippedWood;
    boolean isFlammable;
    int getFlammability;
    int getFireSpreadSpeed;

    public static AxisBlock create(Properties properties) {
        return new AxisBlock(properties, false, Blocks.AIR.defaultBlockState(), false, 0, 0);
    }
    public static AxisBlock create(Properties properties, int getFlammability, int getFireSpreadSpeed) {
        return new AxisBlock(properties, false, Blocks.AIR.defaultBlockState(), true, getFlammability, getFireSpreadSpeed);
    }
    public static AxisBlock create(Properties properties, BlockState strippedWood) {
        return new AxisBlock(properties, true, strippedWood, false, 0, 0);
    }
    public static AxisBlock create(Properties properties, BlockState strippedWood, int getFlammability, int getFireSpreadSpeed) {
        return new AxisBlock(properties, true, strippedWood, true, getFlammability, getFireSpreadSpeed);
    }

    public AxisBlock(Properties properties, boolean hasStrippedWood, BlockState strippedWood, boolean isFlammable, int getFlammability, int getFireSpreadSpeed) {
        super(properties);
        this.hasStrippedWood = hasStrippedWood;
        this.strippedWood = strippedWood;
        this.isFlammable = isFlammable;
        this.getFlammability = getFlammability;
        this.getFireSpreadSpeed = getFireSpreadSpeed;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return isFlammable;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return getFlammability;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return getFireSpreadSpeed;
    }


    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (hasStrippedWood) {
            ItemStack item = player.getItemInHand(interactionHand);
            BlockState newState = strippedWood.setValue(AXIS, blockState.getValue(AXIS));

            if (item.getItem() instanceof AxeItem) {
                level.setBlock(blockPos, newState, 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, newState));
                item.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(interactionHand));

                level.playSound(player, blockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockPos, item);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
