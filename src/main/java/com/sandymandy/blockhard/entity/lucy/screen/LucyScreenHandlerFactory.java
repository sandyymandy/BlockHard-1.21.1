package com.sandymandy.blockhard.entity.lucy.screen;

import com.sandymandy.blockhard.entity.lucy.LucyEntity;
import com.sandymandy.blockhard.entity.lucy.LucyInit;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class LucyScreenHandlerFactory implements ExtendedScreenHandlerFactory {
    private final LucyEntity lucy;

    public LucyScreenHandlerFactory(LucyEntity lucy) {
        this.lucy = lucy;
    }

    // Called on the server → sends data to client
    @Override
    public Object getScreenOpeningData(ServerPlayerEntity player) {
        return new LucyInit.LucyScreenData(lucy.getId());
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.blockhard.lucy_inventory");
    }

    // Called on the client
    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new LucyScreenHandler(syncId, playerInventory, lucy.getId());
    }
}
