package com.JuntaroHatta20021215.hattimod;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MinesweeperSavedData extends WorldSavedData {
    public static final String NAME = HattiMod.MOD_ID + "_hatti";
    public MinesweeperSavedData(String NAME) {
        super(NAME);
    }
    public MinesweeperSavedData() {
        this(NAME);
    }

    private final List<MineSweeper> DATA = new ArrayList<>();

    @Override
    public void load(CompoundNBT nbt) {
        CompoundNBT saveData = nbt.getCompound("saveData");
        for (int i = 0; saveData.contains("data"+i); i++){
            DATA.add(MineSweeper.serializer(saveData.getCompound("data"+i)));
        }
    }

    @Override
    @Nonnull
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        CompoundNBT saveData = new CompoundNBT();
        for (ListIterator<MineSweeper> iterator = DATA.listIterator(); iterator.hasNext(); ){
            saveData.put("data"+iterator.nextIndex(), iterator.next().deserializer());
        }
        nbt.put("saveData", saveData);
        return nbt;
    }

    public static void putData(MineSweeper mineSweeper, ServerWorld world) {
        MinesweeperSavedData data = world.getDataStorage().computeIfAbsent(MinesweeperSavedData::new, MinesweeperSavedData.NAME);
        data.DATA.add(mineSweeper);
        data.setDirty();
    }

    public static MinesweeperSavedData getData(ServerWorld world) {
        return world.getDataStorage().get(MinesweeperSavedData::new, MinesweeperSavedData.NAME);
    }

    public static List<MineSweeper> getMinesweeperList(ServerWorld world) {
        MinesweeperSavedData data = MinesweeperSavedData.getData(world);
        return data.DATA;
    }

    public static void update(MineSweeper mineSweeper, ServerWorld world, int index) {
        MinesweeperSavedData data = world.getDataStorage().computeIfAbsent(MinesweeperSavedData::new, MinesweeperSavedData.NAME);
        data.DATA.set(index, mineSweeper);
        data.setDirty();
    }

    public static void delete(ServerWorld world, int index) {
        MinesweeperSavedData data = world.getDataStorage().computeIfAbsent(MinesweeperSavedData::new, MinesweeperSavedData.NAME);
        data.DATA.remove(index);
        data.setDirty();
    }

    public static void clearAllData(ServerWorld world) {
        MinesweeperSavedData data = world.getDataStorage().computeIfAbsent(MinesweeperSavedData::new, MinesweeperSavedData.NAME);
        data.DATA.clear();
        data.setDirty();
    }
}
