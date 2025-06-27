package com.JuntaroHatta20021215.hattimod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HattiStick extends Item {
    public HattiStick (Properties properties) {
        super(properties);
    }


    @Override
    public ActionResultType useOn (ItemUseContext ctx) {
        World world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        PlayerEntity player = ctx.getPlayer();
        BlockState state = world.getBlockState(pos);

        if (!world.isClientSide() && isMinesweeperNumberBlock(state.getBlock())) {
            MineSweeper.open(ctx);
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    private boolean isMinesweeperNumberBlock(Block block) {
        if(block == ItemsAndBlocks.Blocks.MINESWEEPER_ONE.get()){
            return true;
        } else if(block == ItemsAndBlocks.Blocks.MINESWEEPER_TWO.get()){
            return true;
        } else if(block == ItemsAndBlocks.Blocks.MINESWEEPER_THREE.get()){
            return true;
        } else if(block == ItemsAndBlocks.Blocks.MINESWEEPER_FOUR.get()){
            return true;
        } else if(block == ItemsAndBlocks.Blocks.MINESWEEPER_FIVE.get()){
            return true;
        } else if(block == ItemsAndBlocks.Blocks.MINESWEEPER_SIX.get()){
            return true;
        } else if(block == ItemsAndBlocks.Blocks.MINESWEEPER_SEVEN.get()){
            return true;
        } else if(block == ItemsAndBlocks.Blocks.MINESWEEPER_EIGHT.get()){
            return true;
        } else if(block == ItemsAndBlocks.Blocks.MINESWEEPER_ZERO.get()){
            return true;
        } else if(block == ItemsAndBlocks.Blocks.MINESWEEPER_BLOCK.get()){
            return true;
        }
        return false;
    }
}
