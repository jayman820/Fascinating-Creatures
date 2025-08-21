package net.josh.wungus.util;

import net.josh.wungus.WungusMod;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    public static final WoodType AILANTHUS = WoodType.register(new WoodType(WungusMod.MOD_ID + ":ailanthus", BlockSetType.OAK));
}
