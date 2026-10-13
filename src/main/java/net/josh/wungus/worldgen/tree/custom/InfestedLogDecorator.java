package net.josh.wungus.worldgen.tree.custom;

import com.mojang.serialization.Codec;
import net.josh.wungus.block.custom.InfestedAilanthusLogBlock;
import net.josh.wungus.worldgen.tree.ModTreeDecoratorTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sometimes (probability per tree) turns one log low on the trunk into an infested log with 1 or 2 lanternflies in it.
 * Only used by the naturally generated ailanthus trees, trees grown from saplings don't come with lanternflies.
 */
public class InfestedLogDecorator extends TreeDecorator {
    public static final Codec<InfestedLogDecorator> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability")
            .xmap(InfestedLogDecorator::new, decorator -> decorator.probability).codec();

    // The infested log is at most this many blocks above the lowest log, where the trunk is still bare
    private static final int MAX_HEIGHT = 3;

    private final float probability;

    public InfestedLogDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.INFESTED_LOG.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();
        List<BlockPos> logs = context.logs();
        if (logs.isEmpty() || random.nextFloat() >= this.probability) {
            return;
        }

        // Logs with another log right below them stand upright (the sideways branch logs never have one),
        // and the lowest trunk log is left alone because the ground is below it
        Set<BlockPos> logSet = new HashSet<>(logs);
        int lowest = logs.stream().mapToInt(BlockPos::getY).min().getAsInt();
        List<BlockPos> trunk = logs.stream()
                .filter(pos -> logSet.contains(pos.below()) && pos.getY() <= lowest + MAX_HEIGHT)
                .toList();
        if (trunk.isEmpty()) {
            return;
        }

        BlockPos pos = trunk.get(random.nextInt(trunk.size()));
        context.setBlock(pos, InfestedAilanthusLogBlock.infestedLog(Direction.Axis.Y, 1 + random.nextInt(2)));
    }
}
