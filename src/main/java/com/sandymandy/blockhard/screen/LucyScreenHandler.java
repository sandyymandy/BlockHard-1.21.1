package com.sandymandy.blockhard.screen;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.util.inventory.GirlInventory;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class LucyScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    // This constructor gets called on the client when the server wants it to open the screenHandler,
    // The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    // sync this empty inventory with the inventory on the server.
    public LucyScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(17));
    }

    // This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    // and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public LucyScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(BlockHard.LUCY_SCREEN_HANDLER, syncId);
        checkSize(inventory, GirlInventory.TOTAL_SLOTS);
        this.inventory = inventory;

        // ───── Backpack slots (4x3) = indices 5..16 ─────Add commentMore actions
        int slotIndex = GirlInventory.BACKPACK_START; // 5
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                this.addSlot(new Slot(inventory, slotIndex++, 98 + col * 18, 6 + row * 18));
            }
        }

        // ───── Main Hand Slot = index 0 ─────
        this.addSlot(new Slot(inventory, GirlInventory.MAIN_HAND_SLOT, 125, 63) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof ToolItem || stack.getItem() instanceof SwordItem;
            }
        });

        // ───── Armor Slots (indices 1..4) ─────
        this.addSlot(new Slot(inventory, GirlInventory.ARMOR_HEAD_SLOT, 8, 6) {
            @Override public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof ArmorItem armor && armor.getSlotType() == EquipmentSlot.HEAD;
            }
        });
        this.addSlot(new Slot(inventory, GirlInventory.ARMOR_CHEST_SLOT, 8, 24) {
            @Override public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof ArmorItem armor && armor.getSlotType() == EquipmentSlot.CHEST;
            }
        });
        this.addSlot(new Slot(inventory, GirlInventory.ARMOR_LEGS_SLOT, 8, 42) {
            @Override public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof ArmorItem armor && armor.getSlotType() == EquipmentSlot.LEGS;
            }
        });
        this.addSlot(new Slot(inventory, GirlInventory.ARMOR_FEET_SLOT, 8, 60) {
            @Override public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof ArmorItem armor && armor.getSlotType() == EquipmentSlot.FEET;
            }
        });

        int m;
        int l;
        // The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            boolean isLucyInventory = invSlot < GirlInventory.TOTAL_SLOTS;
            int playerInvStart = GirlInventory.TOTAL_SLOTS;
            int playerInvEnd = this.slots.size();

            if (isLucyInventory) {
                // Move from Lucy to Player inventory
                if (!this.insertItem(originalStack, playerInvStart, playerInvEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Move from Player to Lucy inventory
                boolean moved = false;

                // Try armor slots
                if (originalStack.getItem() instanceof ArmorItem armorItem) {
                    int slotIndex = switch (armorItem.getSlotType()) {
                        case HEAD -> GirlInventory.ARMOR_HEAD_SLOT;
                        case CHEST -> GirlInventory.ARMOR_CHEST_SLOT;
                        case LEGS -> GirlInventory.ARMOR_LEGS_SLOT;
                        case FEET -> GirlInventory.ARMOR_FEET_SLOT;
                        default -> -1;
                    };
                    if (slotIndex != -1 && this.slots.get(slotIndex).getStack().isEmpty()) {
                        moved = this.insertItem(originalStack, slotIndex, slotIndex + 1, false);
                    }
                }

                // Try main hand slot
                if (!moved && (originalStack.getItem() instanceof ToolItem || originalStack.getItem() instanceof SwordItem)) {
                    Slot mainHand = this.slots.get(GirlInventory.MAIN_HAND_SLOT);
                    if (mainHand.getStack().isEmpty()) {
                        moved = this.insertItem(originalStack, GirlInventory.MAIN_HAND_SLOT, GirlInventory.MAIN_HAND_SLOT + 1, false);
                    }
                }

                // Try backpack slots
                if (!moved) {
                    moved = this.insertItem(originalStack, GirlInventory.BACKPACK_START, GirlInventory.BACKPACK_SIZE + 1, false);
                }

                if (!moved) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

}
