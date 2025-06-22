package com.sandymandy.blockhard.screen;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.util.entity.AbstractGirlEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class GirlScreenHandlerFactory implements ExtendedScreenHandlerFactory {
    private final AbstractGirlEntity girl;

    public GirlScreenHandlerFactory(AbstractGirlEntity girl) {
        this.girl = girl;
    }

    // Called on the server → sends data to client
    @Override
    public Object getScreenOpeningData(ServerPlayerEntity player) {
        return new BlockHard.GirlScreenData(girl.getId());
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.blockhard.girl_inventory");
    }

    // Called on the client
    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new GirlScreenHandler(syncId, playerInventory, girl.getId());
    }
}
