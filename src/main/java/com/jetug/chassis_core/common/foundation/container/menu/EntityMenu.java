package com.jetug.chassis_core.common.foundation.container.menu;

import com.jetug.chassis_core.common.foundation.container.slot.EquipmentSlot;
import com.jetug.chassis_core.common.util.Pos2I;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

import java.util.HashMap;

public class EntityMenu extends MenuBase {
    protected final Entity entity;
    private final HashMap<String, Integer> slotsMap = new HashMap<>();
    private Integer slotId = 0;

    public EntityMenu(MenuType<?> pMenuType, int containerId, Container container, Inventory playerInventory,
                      Entity entity, int size, int inventoryPosY) {
        super(pMenuType, containerId, container, playerInventory, size, inventoryPosY);
        this.entity = entity;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return super.stillValid(playerIn) && this.entity.isAlive() && this.entity.distanceTo(playerIn) < 8.0F;
    }

    protected void createSlot(String bodyPart, Pos2I pos){
        slotsMap.put(bodyPart, slotId);
        this.addSlot(new EquipmentSlot(bodyPart, container, slotId, pos.x, pos.y));
        slotId++;
    }
}