package com.JuntaroHatta20021215.hattimod;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.command.arguments.BlockStateInput;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("hattimod")
public class HattiMod {
    public static final String MOD_ID = "hattimod";

    public HattiMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemsAndBlocks.Items.register(modEventBus);
        ItemsAndBlocks.Blocks.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final ItemGroup TAB = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemsAndBlocks.Items.BAKED_APPLE.get());
        }
    };


    @SubscribeEvent
    public void onRegisterCommand(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSource> builderMaze = Commands.literal("maze")
            .then(Commands.argument("start position", BlockPosArgument.blockPos())
                .then(Commands.argument("size (must be odd number)", IntegerArgumentType.integer())
                    .executes(ctx -> {
                    CreateMaze.makeMaze(ctx, false);
                    return Command.SINGLE_SUCCESS; })
                    .then(Commands.argument("wall", BlockStateArgument.block())
                        .then(Commands.argument("floor", BlockStateArgument.block())
                            .executes(ctx -> {
                                CreateMaze.makeMaze(ctx, true);
                                return Command.SINGLE_SUCCESS;
                            })))))
            .then(Commands.literal("help").executes(ctx ->{
                CreateMaze.sendHelp(ctx);
                return Command.SINGLE_SUCCESS;
            }));
        event.getDispatcher().register(builderMaze);

        LiteralArgumentBuilder<CommandSource> builderMineSweeper = Commands.literal("minesweeper")
                .then(Commands.argument("start position", BlockPosArgument.blockPos())
                        .then(Commands.argument("size", IntegerArgumentType.integer())
                                .then(Commands.argument("mines", IntegerArgumentType.integer())
                                    .executes(ctx -> {
                                        MineSweeper.makeMineSweeper(ctx);
                                        return Command.SINGLE_SUCCESS;
                                    }))))
                .then(Commands.literal("help").executes(ctx ->{
                    MineSweeper.sendHelp(ctx);
                    return Command.SINGLE_SUCCESS;
                }))
                .then(Commands.literal("clearAllCash").executes(ctx -> {
                    MineSweeper.minesweeperClearAll(ctx);
                    return Command.SINGLE_SUCCESS;
                }));
        event.getDispatcher().register(builderMineSweeper);
    }
}
