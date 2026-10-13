package net.josh.wungus.block.custom;

import com.mojang.serialization.MapCodec;
import net.josh.wungus.misc.ModDamageTypes;
import net.josh.wungus.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/** Wungus Game 2: a player stepping on it (without sneaking) clicks it, dies and it blows up. */
public class BlockLandmine extends Block {
    public static final MapCodec<BlockLandmine> CODEC = simpleCodec(BlockLandmine::new);

    public BlockLandmine(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pEntity.isSteppingCarefully()) {
            interact(pLevel, pPos, pEntity);
        }

        super.stepOn(pLevel, pPos, pState, pEntity);
    }

    private static void interact(Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof Player && pLevel instanceof ServerLevel serverLevel) {
            pLevel.playSound(null, pPos, ModSounds.LANDMINE_TRIGGER.get(), SoundSource.BLOCKS);
            pEntity.hurtServer(serverLevel, ModDamageTypes.causeLandMine(pLevel.registryAccess()), 10000);
            explode(serverLevel, pPos);
        }
    }

    private static void explode(ServerLevel pLevel, BlockPos pPos) {
        pLevel.playSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        // Doesn't break any blocks, the arena stays in one piece
        pLevel.explode(null, pPos.getX() + 0.5D, pPos.getY() + 1.0D, pPos.getZ() + 0.5D, 4.0F, false, Level.ExplosionInteraction.NONE);
    }
}
