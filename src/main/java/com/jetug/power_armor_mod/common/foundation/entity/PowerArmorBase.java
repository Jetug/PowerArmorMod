package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.client.gui.PowerArmorContainer;
import com.jetug.power_armor_mod.common.foundation.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.network.data.ArmorData;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PowerArmorBase extends EmptyLivingEntity implements ContainerListener {
    public static final String SLOT_TAG = "Slot";
    public static final String ITEMS_TAG = "Items";

    public SimpleContainer inventory;

    protected final boolean isClientSide = level.isClientSide;
    protected final boolean isServerSide = !level.isClientSide;

    public boolean hasArmor(BodyPart bodyPart) {
        return getArmorDurability(bodyPart) != 0;
    }

    protected PowerArmorBase(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        noCulling = true;
        initInventory();
        syncDataWithClient();
        syncDataWithClient();
    }

    public int getArmorDurability(BodyPart bodyPart) {
        var itemStack = inventory.getItem(bodyPart.getId());

        if(itemStack.isEmpty()) return 0;

        var dur = itemStack.getDamageValue();
        var max = itemStack.getMaxDamage();
        var res = max - dur;
        return res;
    }

    public void damageArmor(BodyPart bodyPart, float damage) {
        var itemStack = inventory.getItem(bodyPart.getId());
        Global.LOGGER.info("damageArmor" + isClientSide);
        if(!itemStack.isEmpty()){
            var item = (PowerArmorItem)itemStack.getItem();
            item.damageArmor(itemStack, 1);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        saveInventory(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        loadInventory(compound);
    }

    @Override
    public void containerChanged(@NotNull Container container) {
        syncDataWithClient();
    }

    public ArmorData getArmorData(){
        var data = new ArmorData(getId());
        data.inventory = serializeInventory(inventory);
        return data;
    }

    public void setArmorData(ArmorData data){
        deserializeInventory(inventory, data.inventory);
    }

    public void setInventory(ListTag tags){
        deserializeInventory(inventory, tags);
    }

    protected void syncDataWithClient() {
        if(isServerSide) getArmorData().sentToClient();
    }

    protected void syncDataWithServer() {
        getArmorData().sentToServer();
    }

    protected void saveInventory(CompoundTag compound){
        if (inventory == null) return;
        compound.put(ITEMS_TAG, serializeInventory(inventory));
    }

    protected void loadInventory(@NotNull CompoundTag compound) {
        ListTag nbtTags = compound.getList(ITEMS_TAG, 10);
        initInventory();
        deserializeInventory(inventory, nbtTags);
    }

    protected void initInventory(){
        SimpleContainer inventoryBuff = this.inventory;
        this.inventory = new SimpleContainer(PowerArmorContainer.SIZE);
        if (inventoryBuff != null) {
            inventoryBuff.removeListener(this);
            int i = Math.min(inventoryBuff.getContainerSize(), this.inventory.getContainerSize());
            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventoryBuff.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }
        this.inventory.addListener(this);
    }

    private ListTag serializeInventory(@NotNull SimpleContainer inventory){
        ListTag nbtTags = new ListTag();

        for (int slotId = 0; slotId < inventory.getContainerSize(); ++slotId) {
            var itemStack = inventory.getItem(slotId);
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.putByte(SLOT_TAG, (byte) slotId);
            itemStack.save(compoundNBT);
            nbtTags.add(compoundNBT);
        }

        return nbtTags;
    }

    private void deserializeInventory(SimpleContainer inventory, ListTag nbtTags){
        for (Tag nbt : nbtTags) {
            var compoundNBT = (CompoundTag) nbt;
            int slotId = compoundNBT.getByte(SLOT_TAG) & 255;
            inventory.setItem(slotId, ItemStack.of(compoundNBT));
        }
    }


}
