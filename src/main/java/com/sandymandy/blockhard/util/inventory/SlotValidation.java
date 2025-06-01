package com.sandymandy.blockhard.util.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SlotValidation extends Slot {

    private final GirlInventory inventory;
    private final int index;
    public SlotValidation(GirlInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.inventory = inventory;
        this.index = index;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return inventory.isItemValid(index, stack);
    }
}
