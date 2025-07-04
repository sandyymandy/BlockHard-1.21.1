package com.sandymandy.blockhard.screen;

import com.sandymandy.blockhard.BlockHard;
import com.sandymandy.blockhard.util.entity.AbstractGirlEntity;
import com.sandymandy.blockhard.util.inventory.slot.PublicArmorSlot;
import com.sandymandy.blockhard.util.inventory.GirlInventory;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class GirlScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final AbstractGirlEntity girl;
    public static final Identifier EMPTY_HELMET_SLOT_TEXTURE = Identifier.ofVanilla( "item/empty_armor_slot_helmet");
    public static final Identifier EMPTY_CHESTPLATE_SLOT_TEXTURE = Identifier.ofVanilla( "item/empty_armor_slot_chestplate");
    public static final Identifier EMPTY_LEGGINGS_SLOT_TEXTURE = Identifier.ofVanilla( "item/empty_armor_slot_leggings");
    public static final Identifier EMPTY_BOOTS_SLOT_TEXTURE = Identifier.ofVanilla( "item/empty_armor_slot_boots");


    // The codec-compatible constructor
    public GirlScreenHandler(int syncId, PlayerInventory playerInventory, BlockHard.GirlScreenData data) {
        this(syncId, playerInventory, data.entityId());
    }

    // This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    // and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public GirlScreenHandler(int syncId, PlayerInventory playerInventory, int girlId) {
        super(BlockHard.GIRL_SCREEN_HANDLER, syncId);
        PlayerEntity player = playerInventory.player;
        World world = player.getWorld();

        Entity entity = world.getEntityById(girlId);
        if (!(entity instanceof AbstractGirlEntity girlEntity)) {
            throw new IllegalStateException("LucyEntity not found or mismatched entity ID");
        }
        this.girl = girlEntity;

        Inventory inventory;
        if (girl != null) {
            inventory = girl.getInventory(); // ← Use your custom implementation, not a copy
        } else {
            inventory = new SimpleInventory(GirlInventory.TOTAL_SLOTS);
        }
        this.inventory = inventory;

        checkSize(inventory, GirlInventory.TOTAL_SLOTS);


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

        if (girl != null) {
            this.addSlot(new PublicArmorSlot(inventory, girl, EquipmentSlot.HEAD, GirlInventory.ARMOR_HEAD_SLOT, 8, 6, EMPTY_HELMET_SLOT_TEXTURE));
            this.addSlot(new PublicArmorSlot(inventory, girl, EquipmentSlot.CHEST, GirlInventory.ARMOR_CHEST_SLOT, 8, 24, EMPTY_CHESTPLATE_SLOT_TEXTURE));
            this.addSlot(new PublicArmorSlot(inventory, girl, EquipmentSlot.LEGS, GirlInventory.ARMOR_LEGS_SLOT, 8, 42, EMPTY_LEGGINGS_SLOT_TEXTURE));
            this.addSlot(new PublicArmorSlot(inventory, girl, EquipmentSlot.FEET, GirlInventory.ARMOR_FEET_SLOT, 8, 60, EMPTY_BOOTS_SLOT_TEXTURE));
        } else {
            this.addSlot(new Slot(inventory, GirlInventory.ARMOR_HEAD_SLOT, 8, 6));
            this.addSlot(new Slot(inventory, GirlInventory.ARMOR_CHEST_SLOT, 8, 24));
            this.addSlot(new Slot(inventory, GirlInventory.ARMOR_LEGS_SLOT, 8, 42));
            this.addSlot(new Slot(inventory, GirlInventory.ARMOR_FEET_SLOT, 8, 60));
        }

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

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            boolean fromLucyInventory = invSlot < GirlInventory.TOTAL_SLOTS;
            boolean moved = false;

            if (fromLucyInventory) {
                // Move from Lucy to player inventory
                if (!this.insertItem(originalStack, GirlInventory.TOTAL_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                moved = true;

            } else {
                // Move from player inventory to Lucy

                // 1. Try armor slots
                if (originalStack.getItem() instanceof ArmorItem armorItem) {
                    EquipmentSlot eqSlot = armorItem.getSlotType();
                    int targetIndex = switch (eqSlot) {
                        case HEAD -> GirlInventory.ARMOR_HEAD_SLOT;
                        case CHEST -> GirlInventory.ARMOR_CHEST_SLOT;
                        case LEGS -> GirlInventory.ARMOR_LEGS_SLOT;
                        case FEET -> GirlInventory.ARMOR_FEET_SLOT;
                        default -> -1;
                    };

                    if (targetIndex != -1 && !this.slots.get(targetIndex).hasStack()) {
                        if (this.insertItem(originalStack, targetIndex, targetIndex + 1, false)) {
                            moved = true;
                        }
                    }
                }

                // 2. Try main hand slot for tools/swords
                if (!moved && (originalStack.getItem() instanceof ToolItem || originalStack.getItem() instanceof SwordItem)) {
                    int mainIndex = GirlInventory.MAIN_HAND_SLOT;
                    if (!this.slots.get(mainIndex).hasStack()) {
                        if (this.insertItem(originalStack, mainIndex, mainIndex + 1, false)) {
                            moved = true;
                        }
                    }
                }

                // 3. Fallback only if it's not equipment, or slots are full
                if (!moved) {
                    boolean isArmor = originalStack.getItem() instanceof ArmorItem;
                    boolean isToolOrSword = originalStack.getItem() instanceof ToolItem || originalStack.getItem() instanceof SwordItem;

                    if (!isArmor && !isToolOrSword) {
                        // Not equipment → allowed to go into backpack
                        if (this.insertItem(originalStack, GirlInventory.BACKPACK_START, GirlInventory.BACKPACK_START + GirlInventory.BACKPACK_SIZE, false)) {
                            moved = true;
                        }
                    }

                    // If it's equipment but failed to go into the right slot, deny fallback
                    if (!moved) return ItemStack.EMPTY;
                }
            }

            // Final cleanup
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }




    public AbstractGirlEntity getGirl(){
        return this.girl;
    }



}
