package net.josh.wungus.worldgen.tree;

import net.josh.wungus.WungusMod;
import net.josh.wungus.worldgen.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower AILANTHUS = new TreeGrower(WungusMod.MOD_ID + ":ailanthus",
            Optional.empty(), Optional.of(ModConfiguredFeatures.AILANTHUS_KEY), Optional.empty());
}
