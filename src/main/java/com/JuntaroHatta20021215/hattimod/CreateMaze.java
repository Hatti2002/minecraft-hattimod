package com.JuntaroHatta20021215.hattimod;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CreateMaze {
    public static void sendHelp (CommandContext<CommandSource> ctx) {
        String mazeHelp1 = "/maze <position> <size> [<wall> <floor>]";
        String mazeHelp2 = "Position means start position of the maze.";
        String mazeHelp3 = "Size must be odd number ond 3 ~ 99.";
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp1), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp2), true);
        ctx.getSource().sendSuccess(new StringTextComponent(mazeHelp3), true);
    }
    public static void makeMaze (CommandContext<CommandSource> ctx, boolean materialSelected) throws CommandSyntaxException {
        int MAZE_SIZE = IntegerArgumentType.getInteger(ctx, "size (must be odd number)");
        if (MAZE_SIZE % 2 == 0) {
            String errorMsg = "ERROR : Size must be odd number.";
            ctx.getSource().sendSuccess(new StringTextComponent(errorMsg).withStyle(TextFormatting.RED), true);
            return;
        }
        if (MAZE_SIZE > 100) {
            String errorMsg = "ERROR : Maze size is too large. It must be smaller than 100.";
            ctx.getSource().sendSuccess(new StringTextComponent(errorMsg).withStyle(TextFormatting.RED), true);
            return;
        }
        if (MAZE_SIZE < 3) {
            String errorMsg = "ERROR : Maze size is too small. It must be larger than 2.";
            ctx.getSource().sendSuccess(new StringTextComponent(errorMsg).withStyle(TextFormatting.RED), true);
            return;
        }
        GenMaze genMaze = new GenMaze(MAZE_SIZE);
        int[][] maze = genMaze.gen();
//        printChatMaze(ctx, maze);
        setMaze(ctx, maze, materialSelected);
    }

    private static class GenMaze{
        private int[][] maze;
        private int MAZE_SIZE;
        private static List<Coordinate> potential = new ArrayList<>();

        //coordinate class
        private static class Coordinate{
            final int x;
            final int y;
            Coordinate(int x,int y){
                this.x = x;
                this.y = y;
            }
        }
        //constructor
        GenMaze(int size) {
            this.MAZE_SIZE = size;
            this.maze = new int[size][size];
            //fill all with 1(wall)
            for (int i=0; i<size; i++) {
                Arrays.fill(this.maze[i],1);
            }
        }

        //generate maze
        public int[][] gen() {
            //wall = 1,empty = 0, start = 2, goal = 3
            maze[1][MAZE_SIZE-1] = 2;
            maze[MAZE_SIZE-2][0] = 3;
            //dig starting position
            maze[1][MAZE_SIZE-2] = 0;
            //dig maze
            dig(1, MAZE_SIZE-2);
            return this.maze;
        }

        //manege digging maze
        private void dig(final int x, final int y) {
            if(canExtend(x,y)){
                Coordinate location = extendMaze(x,y);
                final int next_x = location.x;
                final int next_y = location.y;
                maze[next_x][next_y] = 0;
                maze[(next_x+x)/2][(next_y+y)/2] = 0;
                if(next_x == MAZE_SIZE-2 && next_y == 1){
                    final int potential_size = potential.size();
                    if(potential_size != 0){
                        Random r = new Random();
                        final int index = r.nextInt(potential_size);
                        final int next_x_ex = potential.get(index).x;
                        final int next_y_ex = potential.get(index).y;
                        potential.remove(index);
                        dig(next_x_ex,next_y_ex);
                    }
                }else{
                    if(canExtend(x,y)){
                        potential.add(new Coordinate(x,y));
                    }
                    dig(next_x,next_y);
                }
            }else{
                final int potential_size = potential.size();
                if(potential_size != 0){
                    Random r = new Random();
                    final int index = r.nextInt(potential_size);
                    final int next_x = potential.get(index).x;
                    final int next_y = potential.get(index).y;
                    potential.remove(index);
                    dig(next_x,next_y);
                }
            }
        }

        //return (can extend or not)
        private boolean canExtend(final int x,final int y){
            try{
                if(maze[x][y+2] == 1){
                    return true;
                }
            }catch(ArrayIndexOutOfBoundsException e){};
            try{
                if(maze[x][y-2] == 1){
                    return true;
                }
            }catch(ArrayIndexOutOfBoundsException e){};
            try{
                if(maze[x+2][y] == 1){
                    return true;
                }
            }catch(ArrayIndexOutOfBoundsException e){};
            try{
                if(maze[x-2][y] == 1){
                    return true;
                }
            }catch(ArrayIndexOutOfBoundsException e){};
            return false;
        }

        //digging maze
        private Coordinate extendMaze(final int x,final int y) {
            List<Coordinate> coordinates = new ArrayList<>();
            int how_many_directions = 0;
            try{
                if(maze[x][y+2] == 1){
                    how_many_directions++;
                    coordinates.add(new Coordinate(x,y+2));
                }
            }catch(ArrayIndexOutOfBoundsException e){};
            try{
                if(maze[x][y-2] == 1){
                    how_many_directions++;
                    coordinates.add(new Coordinate(x,y-2));
                }
            }catch(ArrayIndexOutOfBoundsException e){};
            try{
                if(maze[x+2][y] == 1){
                    how_many_directions++;
                    coordinates.add(new Coordinate(x+2,y));
                }
            }catch(ArrayIndexOutOfBoundsException e){};
            try{
                if(maze[x-2][y] == 1){
                    how_many_directions++;
                    coordinates.add(new Coordinate(x-2,y));
                }
            }catch(ArrayIndexOutOfBoundsException e){};
            Random r = new Random();
            final int direction = r.nextInt(how_many_directions);
            return coordinates.get(direction);
        }
    }

    //only for debug
    private static void printChatMaze(CommandContext<CommandSource> ctx, int[][] maze) {
        for (int i=0; i< maze.length; i++) {
            String line = "";
            for (int k=0; k< maze.length; k++) {
                if(maze[i][k] == 0) {
                    line += "  ";
                } else if (maze[i][k] == 1) {
                    line += "[]";
                } else if (maze[i][k] == 2) {
                    line += "ST";
                } else if (maze[i][k] == 3) {
                    line += "GO";
                }
            }
            ctx.getSource().sendSuccess(new StringTextComponent(line), true);
        }
    }

    private static void setMaze (CommandContext<CommandSource> ctx, int[][] maze, boolean materialSelected) throws CommandSyntaxException {
        BlockPos startPosition = BlockPosArgument.getLoadedBlockPos(ctx, "start position");
        BlockPos pos = startPosition.offset(-1,-1,1-maze.length);

        long X = pos.getX();
        long Y = pos.getY();
        long Z = pos.getZ();

        BlockState wall = Blocks.IRON_BLOCK.defaultBlockState();
        BlockState floor = Blocks.IRON_BLOCK.defaultBlockState();
        BlockState goal = Blocks.REDSTONE_BLOCK.defaultBlockState();
        BlockState start = Blocks.EMERALD_BLOCK.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        ServerWorld world = ctx.getSource().getLevel();
        if (materialSelected) {
            wall = BlockStateArgument.getBlock(ctx, "wall").getState();
            floor = BlockStateArgument.getBlock(ctx, "floor").getState();
        }
        //wall = 1,empty = 0, start = 2, goal = 3
        int flag = 0;
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze.length; y++) {
                if (maze[x][y] == 0) {
                    boolean success1 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), floor);
                    Y++;
                    boolean success2 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), air);
                    Y++;
                    boolean success3 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), air);
                    Y -= 2;
                } else if (maze[x][y] == 1) {
                    boolean success1 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), floor);
                    Y++;
                    boolean success2 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), wall);
                    Y++;
                    boolean success3 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), wall);
                    Y -= 2;
                } else if (maze[x][y] == 2) {
                    boolean success1 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), start);
                    Y++;
                    boolean success2 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), air);
                    Y++;
                    boolean success3 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), air);
                    Y -= 2;
                } else if (maze[x][y] == 3) {
                    boolean success1 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), goal);
                    Y++;
                    boolean success2 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), air);
                    Y++;
                    boolean success3 = world.setBlockAndUpdate(new BlockPos(X,Y,Z), air);
                    Y -= 2;
                }
                Z++;
            }
            X++;
            Z -= maze.length;
        }
    }
}
