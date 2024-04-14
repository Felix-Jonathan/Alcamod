package com.alcamod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class AlcaniteBlock extends Block {

    public AlcaniteBlock() {
        super(Block.Properties.of(Material.METAL)
                .strength(5.0F, 10.0F)
                .sound(SoundType.ANCIENT_DEBRIS)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops());
    }

}
