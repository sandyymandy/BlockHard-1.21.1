package com.sandymandy.blockhard.util.inventory;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.collection.DefaultedList;

public interface GirlInventory extends Inventory {
    // ─────────────── SLOT INDICES ───────────────
    int MAIN_HAND_SLOT     = 0;
    int ARMOR_HEAD_SLOT    = 1;
    int ARMOR_CHEST_SLOT   = 2;
    int ARMOR_LEGS_SLOT    = 3;
    int ARMOR_FEET_SLOT    = 4;
    int BACKPACK_START     = 5;
    int BACKPACK_SIZE      = 12;             // 5..16 inclusive
    int TOTAL_SLOTS        = BACKPACK_START + BACKPACK_SIZE; // 17

    // Must return the same instance every time
    DefaultedList<ItemStack> getItems();

    // ─────────────── Factory Methods ───────────────

    static GirlInventory of(DefaultedList<ItemStack> items) {
        return new GirlInventory() {
            @Override
            public DefaultedList<ItemStack> getItems() {
                return items;
            }
        };
    }

    static GirlInventory ofSize() {
        return of(DefaultedList.ofSize(TOTAL_SLOTS, ItemStack.EMPTY));
    }

    // ─────────────── Inventory Logic ───────────────

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : getItems()) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) markDirty();
        return result;
    }

    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > stack.getMaxCount()) {
            stack.setCount(stack.getMaxCount());
        }
        markDirty();
    }

    @Override
    default void markDirty() {
        // Optional: Sync logic or custom behavior
    }

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    default void clear() {
        getItems().clear();
    }

    // ─────────────── Custom Validation ───────────────

    default boolean isItemValid(int slot, ItemStack stack) {
        if (stack.isEmpty()) return true; // Allow clearing

        if (slot == MAIN_HAND_SLOT) {
            return stack.getItem() instanceof SwordItem || stack.getItem() instanceof ToolItem;
        }

        if (slot >= ARMOR_HEAD_SLOT && slot <= ARMOR_FEET_SLOT) {
            if (!(stack.getItem() instanceof ArmorItem armor)) return false;

            EquipmentSlot target = switch (slot) {
                case ARMOR_HEAD_SLOT -> EquipmentSlot.HEAD;
                case ARMOR_CHEST_SLOT -> EquipmentSlot.CHEST;
                case ARMOR_LEGS_SLOT  -> EquipmentSlot.LEGS;
                case ARMOR_FEET_SLOT  -> EquipmentSlot.FEET;
                default -> null;
            };

            return armor.getSlotType() == target;
        }

        // Backpack slots (5–16) allow any item
        return slot >= BACKPACK_START && slot < BACKPACK_START + BACKPACK_SIZE;
    }
}
