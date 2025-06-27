package com.JuntaroHatta20021215.hattimod;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class MineSweeper {
    public static void sendHelp(CommandContext<CommandSource> ctx) {
        String mazeHelp1 = "/minesweeper <position> <size> <mines>";
        String mazeHelp2 = "Position means corner of the minesweeper.";
        String mazeHelp3 = "Size must be 4 ~ 99.";
        String mazeHelp4 = "";
        String mazeHelp5 = "If you want clear cash about minesweeper which isn't finished, say";
        String mazeHelp6 =  "/minesweeper clearAllCash";
        String mazeHelp7 = "";
        String mazeHelp8 = "How to play";
        String mazeHelp9 = "Use Mine Flag to minesweeper block to set flag on it.";
        String mazeHelp10 = "Use Hatti Stick to minesweeper block to open it.";
        String mazeHelp11 = "When you open minesweeper block, please be careful of the mine.";
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp1), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp2), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp3), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp4), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp5), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp6), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp7), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp8), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp9), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp10), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp11), true);
    }
//////////////////////////////////////////////////////////
//    NON STATIC
    private final BlockPos startPos;
    private final int size;
    private final byte[] isMine;
    private final byte[] isOpen;

//    {{1,1,1,1,1},
//     {1,1,1,1,1},
//     {1,1,1,1,1},   -> {{},{},{},{},{}} -> {,,,,}
//     {1,1,1,1,1},
//     {1,1,1,1,1}}

    private void unCover(ItemUseContext ctx, BlockPos pos) {
        int x = pos.getX()-startPos.getX();
        int z = pos.getZ()-startPos.getZ();
//        0 and size-1 is edge
        if (x == 0 || x == size-1 || z == 0 || z == size-1){
            return;
        }
        int numOfBomb = 0;
        for (int p = x-1; p < x+2; p++) {
            for (int q = z-1; q < z+2; q++) {
                if(isMine[p*size+q] == 1) {
                    numOfBomb++;
                }
            }
        }

        if (isOpen[x*size+z] == 1) {
//            for debug

//            int a = numOfFlag(ctx, ctx.getClickedPos());
//            PlayerEntity player = ctx.getPlayer();
//            player.sendMessage(new StringTextComponent("num of bomb : " + Integer.toString(numOfBomb)), player.getUUID());
//            player.sendMessage(new StringTextComponent("num of flag : " + Integer.toString(a)), player.getUUID());

//            for debug end
            if (numOfBomb != 0 && numOfBomb == numOfFlag(ctx, pos)) {
                openSurroundings(ctx, pos, x, z);
            }
        } else {
            isOpen[x*size+z] = 1;
            if(isMine[x*size+z] == 1){
                boolean success = ctx.getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_BOMB.get().defaultBlockState());
            } else {
                setMinesweeperNumberBlock(ctx, pos, numOfBomb);
            }
            if (numOfBomb == 0) {
                openZero(ctx, x, z);
            }
        }
    }
    private void openSurroundings(ItemUseContext ctx, BlockPos pos, int x, int z) {
        //open surroundings (only non flag)
        //if open zero -> use open zero method

        for (int i = x-1; i < x+2; i++){
            for (int k = z-1; k < z+2; k++){
                if (i == 0 || i == size-1 || k == 0 || k == size-1){
                    continue;
                }
                if (isNotFlag(ctx, startPos.offset(i,0,k)) && isOpen[i*size+k] == 0) {
                    isOpen[i*size+k] = 1;
                    if(isMine[i*size+k] == 1){
                        boolean success = ctx.getLevel().setBlockAndUpdate(startPos.offset(i, 0, k), ItemsAndBlocks.Blocks.MINESWEEPER_BOMB.get().defaultBlockState());
                    } else {
                        int numOfBomb = 0;
                        for (int p = i-1; p < i+2; p++) {
                            for (int q = k-1; q < k + 2; q++) {
                                if(isMine[p*size+q] == 1) {
                                    numOfBomb++;
                                }
                            }
                        }
                        setMinesweeperNumberBlock(ctx, startPos.offset(i,0,k), numOfBomb);
                        if(numOfBomb == 0) {
                            openZero(ctx, i, k);
                        }
                    }
                }
            }
        }
    }
//      x, y is relative from startPos
    private void openZero(ItemUseContext ctx, int x, int z) {
        for (int i = x-1; i < x+2; i++) {
            for (int k = z-1; k < z+2; k++) {
                //0 and size-1 is edge
                if (i == 0 || i == size-1 || k == 0 || k == size-1){
                    continue;
                }
                if (isOpen[i*size+k] == 0) {
                    isOpen[i*size+k] = 1;
                    int numOfBomb = 0;
                    for (int p = i-1; p < i+2; p++) {
                        for (int q = k-1; q < k + 2; q++) {
                            if(isMine[p*size+q] == 1) {
                                numOfBomb++;
                            }
                        }
                    }
                    setMinesweeperNumberBlock(ctx , startPos.offset(i, 0, k), numOfBomb);
                    if (numOfBomb == 0){
                        openZero(ctx, i, k);
                    }
                }
            }
        }
    }
    public CompoundNBT deserializer(){
        CompoundNBT nbt = new CompoundNBT();
        nbt.putByteArray("isMine", isMine);
        nbt.putByteArray("isOpen", isOpen);
        nbt.putInt("size", size);
        nbt.putLong("startPos", startPos.asLong());
        return nbt;
    }
///////////////////////////////////////////////////////////

    private MineSweeper(CommandContext<CommandSource> ctx, byte[] isMine) throws CommandSyntaxException {
        BlockPos startPosition = BlockPosArgument.getLoadedBlockPos(ctx, "start position");
        this.startPos = startPosition.offset(0, -1, 0);
        this.size = IntegerArgumentType.getInteger(ctx, "size");
        this.isMine = isMine;
        this.isOpen = new byte[size*size];
        for (int i = 0; i < size*size; i++) {
            this.isOpen[i] = 0;
        }
    }
    private MineSweeper(BlockPos pos, int size, byte[] isMine, byte[] isOpen) {
        this.startPos = pos;
        this.size = size;
        this.isMine = isMine;
        this.isOpen = isOpen;
    }

    public static MineSweeper serializer(CompoundNBT nbt) {
        return new MineSweeper(BlockPos.of(nbt.getLong("startPos")), nbt.getInt("size"), nbt.getByteArray("isMine"), nbt.getByteArray("isOpen"));
    }

    public static void makeMineSweeper(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final int size = IntegerArgumentType.getInteger(ctx, "size");
        final int mines = IntegerArgumentType.getInteger(ctx, "mines");
        if (size < 4 || size > 99) {
            sendHelp(ctx);
            return;
        } else if ((size-2)*(size-2) <= mines ){
            sendHelp(ctx);
            return;
        }
        //index 0 and size-1 = edge
        boolean[][] isMineBoolean = new boolean[size][size];
        Random r = new Random();
        for (int i = 0; i < mines; i++) {
            while(true){
                int x = r.nextInt(size-2)+1;
                int z = r.nextInt(size-2)+1;
                if(!isMineBoolean[x][z]){
                    isMineBoolean[x][z] = true;
                    break;
                }
            }
        }

        byte[] isMine = new byte[size*size];
        int count = 0;
        for (boolean[] array : isMineBoolean) {
            for (boolean value : array) {
                isMine[count] = value ? (byte)1 : (byte)0;
                count++;
            }
        }

        BlockPos pos = BlockPosArgument.getLoadedBlockPos(ctx, "start position");
        pos = pos.offset(0, -1, 0);
        for (int i = 0; i < size; i++){
            for (int k = 0; k < size; k++){
                if(i == 0 || k == 0 || i == size-1 || k == size-1){
                    boolean success = ctx.getSource().getLevel().setBlockAndUpdate(pos, Blocks.IRON_BLOCK.defaultBlockState());
                } else {
                    boolean success = ctx.getSource().getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_BLOCK.get().defaultBlockState());
                }
                pos = pos.offset(0,0,1);
            }
            pos = pos.offset(1,0,-size);
        }
        MinesweeperSavedData.putData(new MineSweeper(ctx, isMine), ctx.getSource().getLevel());
    }

    public static void open(ItemUseContext ctx) {
        World world = ctx.getLevel();
        List<MineSweeper> mineSweepers = MinesweeperSavedData.getMinesweeperList((ServerWorld)world);
        int index = getIndex(mineSweepers, ctx.getClickedPos());
        if (index == -1) {return;}
        MineSweeper mineSweeper = mineSweepers.get(index);

        mineSweeper.unCover(ctx, ctx.getClickedPos());
        if(isClear(mineSweeper)){
            replaceToBomb(ctx, mineSweeper);
            MinesweeperSavedData.delete((ServerWorld)ctx.getLevel(), index);
        } else if (isGameOver(mineSweeper)) {

            //check where to summon TNT
            for (int i = 0; i < mineSweeper.size; i++) {
                for (int k = 0; k < mineSweeper.size; k++) {
                    if (mineSweeper.isMine[i*mineSweeper.size+k]*mineSweeper.isOpen[i* mineSweeper.size+k] == 1) {
                        summonTNT(ctx, /*pos*/mineSweeper.startPos.offset(i,0,k));
                    }
                }
            }

            MinesweeperSavedData.delete((ServerWorld)ctx.getLevel(), index);
        } else {
            MinesweeperSavedData.update(mineSweeper, (ServerWorld)ctx.getLevel(), index);
        }
    }

    private static int getIndex(List<MineSweeper> mineSweepers, BlockPos clickedPos) {
        for(int i = 0; i < mineSweepers.size(); i++) {
            BlockPos pos = mineSweepers.get(i).startPos;
            int siz = mineSweepers.get(i).size;
            if(pos.getX() < clickedPos.getX() && clickedPos.getX() < pos.getX()+siz) {
                if(pos.getZ() < clickedPos.getZ() && clickedPos.getZ() < pos.getZ()+siz) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static boolean isClear(MineSweeper mineSweeper) {
        int count = 1;
        for (int i = 1; i < mineSweeper.size-1; i++) {
            for (int k = 1; k < mineSweeper.size-1; k++) {
                count *= (mineSweeper.isMine[i*mineSweeper.size+k] + mineSweeper.isOpen[i*mineSweeper.size+k]);
            }
        }
        return count == 1;
    }

    private static boolean isGameOver(MineSweeper mineSweeper) {
        int count = 0;
        for (int i = 1; i < mineSweeper.size-1; i++) {
            for (int k = 1; k < mineSweeper.size-1; k++) {
                count += (mineSweeper.isMine[i*mineSweeper.size+k] * mineSweeper.isOpen[i*mineSweeper.size+k]);
            }
        }
        return count != 0;
    }

    public static void minesweeperClearAll(CommandContext<CommandSource> ctx) {
        MinesweeperSavedData.clearAllData(ctx.getSource().getLevel());
    }

    private static void summonTNT(ItemUseContext ctx, BlockPos pos) {
        TNTEntity tnt = new TNTEntity(ctx.getLevel(), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, null);
        tnt.setFuse(20);
        ctx.getLevel().addFreshEntity(tnt);
    }

    private static void replaceToBomb(ItemUseContext ctx, MineSweeper mineSweeper) {
        for (int i = 1; i < mineSweeper.size-1; i++) {
            for(int k = 1; k < mineSweeper.size-1; k++) {
                if (mineSweeper.isMine[i*mineSweeper.size+k] == 1) {
                    boolean success = ctx.getLevel().setBlockAndUpdate(mineSweeper.startPos.offset(i,0,k), ItemsAndBlocks.Blocks.MINESWEEPER_BOMB.get().defaultBlockState());
                }
            }
        }
    }

    private static boolean isBlockOneToEight(Block block) {
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
        }
        return false;
    }

    private static boolean isBlockZero(Block block) {
        return block == ItemsAndBlocks.Blocks.MINESWEEPER_ZERO.get();
    }

    private static void setMinesweeperNumberBlock (ItemUseContext ctx, BlockPos pos, int numOfBomb) {
        if (numOfBomb == 0){
            boolean success = ctx.getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_ZERO.get().defaultBlockState());
        } else if (numOfBomb == 1) {
            boolean success = ctx.getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_ONE.get().defaultBlockState());
        } else if (numOfBomb == 2) {
            boolean success = ctx.getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_TWO.get().defaultBlockState());
        } else if (numOfBomb == 3) {
            boolean success = ctx.getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_THREE.get().defaultBlockState());
        } else if (numOfBomb == 4) {
            boolean success = ctx.getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_FOUR.get().defaultBlockState());
        } else if (numOfBomb == 5) {
            boolean success = ctx.getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_FIVE.get().defaultBlockState());
        } else if (numOfBomb == 6) {
            boolean success = ctx.getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_SIX.get().defaultBlockState());
        } else if (numOfBomb == 7) {
            boolean success = ctx.getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_SEVEN.get().defaultBlockState());
        } else if (numOfBomb == 8) {
            boolean success = ctx.getLevel().setBlockAndUpdate(pos, ItemsAndBlocks.Blocks.MINESWEEPER_EIGHT.get().defaultBlockState());
        }
    }

    private static int numOfFlag (ItemUseContext ctx, BlockPos pos) {
        int numOfFlag = 0;
        for (int i = pos.getX()-1; i < pos.getX()+2; i++) {
            for (int k = pos.getZ()-1; k < pos.getZ()+2; k++) {
                if (ctx.getLevel().getBlockState(new BlockPos(i,pos.getY(),k)).getBlock() == ItemsAndBlocks.Blocks.MINESWEEPER_FLAG.get()) {
                    numOfFlag++;
                }
            }
        }
        return numOfFlag;
    }

    private static boolean isNotFlag (ItemUseContext ctx, BlockPos pos) {
        return ctx.getLevel().getBlockState(pos).getBlock() != ItemsAndBlocks.Blocks.MINESWEEPER_FLAG.get();
    }
}