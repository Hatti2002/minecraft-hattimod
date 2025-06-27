package com.JuntaroHatta20021215.hattimod;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

public class ItemsAndBlocks {
    private static final String MOD_ID = "hattimod";
    public static class Items {
        private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

        public static final RegistryObject<Item> BAKED_APPLE = ITEMS.register("baked_apple", () -> new Item(new Item.Properties().tab(HattiMod.TAB)
                .food(new Food.Builder().nutrition(6).saturationMod(4.5F).build())));
        public static final RegistryObject<HattiStick> HATTI_STICK = ITEMS.register("hatti_stick", () -> new HattiStick(new HattiStick.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<MineFlag> MINE_FLAG = ITEMS.register("mine_flag", () -> new MineFlag(new MineFlag.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_ONE = ITEMS.register("minesweeper_one", () -> new BlockItem(Blocks.MINESWEEPER_ONE.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_TWO = ITEMS.register("minesweeper_two", () -> new BlockItem(Blocks.MINESWEEPER_TWO.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_THREE = ITEMS.register("minesweeper_three", () -> new BlockItem(Blocks.MINESWEEPER_THREE.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_FOUR = ITEMS.register("minesweeper_four", () -> new BlockItem(Blocks.MINESWEEPER_FOUR.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_FIVE = ITEMS.register("minesweeper_five", () -> new BlockItem(Blocks.MINESWEEPER_FIVE.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_SIX = ITEMS.register("minesweeper_six", () -> new BlockItem(Blocks.MINESWEEPER_SIX.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_SEVEN = ITEMS.register("minesweeper_seven", () -> new BlockItem(Blocks.MINESWEEPER_SEVEN.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_EIGHT = ITEMS.register("minesweeper_eight", () -> new BlockItem(Blocks.MINESWEEPER_EIGHT.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_FLAG = ITEMS.register("minesweeper_flag", () -> new BlockItem(Blocks.MINESWEEPER_FLAG.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_BOMB = ITEMS.register("minesweeper_bomb", () -> new BlockItem(Blocks.MINESWEEPER_BOMB.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_ZERO = ITEMS.register("minesweeper_zero", () -> new BlockItem(Blocks.MINESWEEPER_ZERO.get(), new Item.Properties().tab(HattiMod.TAB)));
        public static final RegistryObject<Item> MINESWEEPER_BLOCK = ITEMS.register("minesweeper_block", () -> new BlockItem(Blocks.MINESWEEPER_BLOCK.get(), new Item.Properties().tab(HattiMod.TAB)));


        public static void register(IEventBus eventBus) {
            ITEMS.register(eventBus);
        }
    }

    public static class Blocks {
        private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
        private static AbstractBlock.Properties getMinesweeperBlockStatus() {
            return AbstractBlock.Properties
                    .of(Material.METAL, MaterialColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 2.0F)
                    .sound(SoundType.METAL)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(2);
        }

        public static final RegistryObject<Block> MINESWEEPER_ONE = BLOCKS.register("minesweeper_one", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_TWO = BLOCKS.register("minesweeper_two", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_THREE = BLOCKS.register("minesweeper_three", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_FOUR = BLOCKS.register("minesweeper_four", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_FIVE = BLOCKS.register("minesweeper_five", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_SIX = BLOCKS.register("minesweeper_six", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_SEVEN = BLOCKS.register("minesweeper_seven", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_EIGHT = BLOCKS.register("minesweeper_eight", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_FLAG = BLOCKS.register("minesweeper_flag", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_BOMB = BLOCKS.register("minesweeper_bomb", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_ZERO = BLOCKS.register("minesweeper_zero", () -> new Block(Blocks.getMinesweeperBlockStatus()));
        public static final RegistryObject<Block> MINESWEEPER_BLOCK = BLOCKS.register("minesweeper_block", () -> new Block(Blocks.getMinesweeperBlockStatus()));


        public static void register(IEventBus eventBus) {
            BLOCKS.register(eventBus);
        }
    }

}
