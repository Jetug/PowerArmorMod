package com.jetug.power_armor_mod.common.foundation.container.menu.base;

import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public class EntityMenu extends MenuBase {
    protected final Entity entity;

    public EntityMenu(MenuType<?> pMenuType, int containerId, Container container, Inventory playerInventory,
                      Entity entity, int size, int inventoryPosY) {
        super(pMenuType, containerId, container, playerInventory, size, inventoryPosY);
        this.entity = entity;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return super.stillValid(playerIn) && this.entity.isAlive() && this.entity.distanceTo(playerIn) < 8.0F;
    }
}