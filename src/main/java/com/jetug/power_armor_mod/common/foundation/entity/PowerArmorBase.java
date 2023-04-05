package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.client.gui.PowerArmorContainer;
import com.jetug.power_armor_mod.common.foundation.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.network.actions.DashAction;
import com.jetug.power_armor_mod.common.network.actions.InventorySyncAction;
import com.jetug.power_armor_mod.common.network.data.ArmorData;
import com.jetug.power_armor_mod.common.util.enums.ActionType;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.helpers.timer.TickTimer;
import com.jetug.power_armor_mod.common.util.interfaces.SimpleAction;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.*;

import java.util.ArrayList;

import static com.jetug.power_armor_mod.common.network.PacketSender.*;
import static com.jetug.power_armor_mod.common.network.data.ArmorData.*;
import static com.jetug.power_armor_mod.common.util.constants.NBT.*;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;
import static com.jetug.power_armor_mod.common.util.helpers.InventoryHelper.*;
import static com.jetug.power_armor_mod.common.util.helpers.MathHelper.*;

public class PowerArmorBase extends EmptyLivingEntity implements ContainerListener {
    protected static final int MAX_ATTACK_CHARGE = 60;
    public static final int COOLING = 5;

    protected final TickTimer timer = new TickTimer();
    protected final boolean isClientSide = level.isClientSide;
    protected final boolean isServerSide = !level.isClientSide;
    protected float totalDefense;
    protected float totalToughness;
    protected int maxHeat = 1000;
    protected int heat = 50;
    protected int attackCharge = 0;
    protected SimpleContainer inventory;

    public final BodyPart[] armor = new BodyPart[]{
            HEAD      ,
            BODY      ,
            LEFT_ARM  ,
            RIGHT_ARM ,
            LEFT_LEG  ,
            RIGHT_LEG ,
    };

    public final BodyPart[] equipment = new BodyPart[]{
            HEAD      ,
            BODY      ,
            LEFT_ARM  ,
            RIGHT_ARM ,
            LEFT_LEG  ,
            RIGHT_LEG ,
            ENGINE
    };
    public PowerArmorBase(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        noCulling = true;
        initInventory();
//        syncDataWithClient();
//        syncDataWithClient();
        getTotalArmor();
    }

    @Override
    public void tick() {
        super.tick();
        syncDataWithClient();
        syncDataWithServer();
        subHeat(COOLING);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        saveInventory(compound);
        compound.putInt(HEAT, heat);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        loadInventory(compound);
        heat = compound.getInt(HEAT);
    }

    @Override
    public void containerChanged(@NotNull Container container) {
        getTotalArmor();

//        if(isServerSide)
//            doClientAction(new InventorySyncAction(inventory), get);
    }

    public void addHeat(int value){
        if(value <= 0) return;

        if(heat + value <= maxHeat)
            heat += value;
        else heat = maxHeat;
    }

    public void subHeat(int value){
        if(value <= 0) return;

        if(heat - value >= 0)
            heat -= value;
        else heat = 0;
    }

    public int getHeatInPercent(){
        return getInPercents(heat, maxHeat);
    }

    public int getAttackChargeInPercent(){
        return getInPercents(attackCharge, MAX_ATTACK_CHARGE);
    }

    public boolean isChargingAttack(){
        return attackCharge > 0;
    }

    public boolean isMaxCharge(){
        return attackCharge == MAX_ATTACK_CHARGE;
    }

    public void addAttackCharge() {
        var value = 1;
        if(isClientSide) doServerAction(ActionType.ADD_ATTACK_CHARGE);
        if(attackCharge + value <= MAX_ATTACK_CHARGE)
            attackCharge += value;
    }

    public void resetAttackCharge() {
        if(isClientSide) doServerAction(ActionType.RESET_ATTACK_CHARGE);
        setAttackCharge(0);
    }

    public void setAttackCharge(int value){
        if(value == 0){
            var v = attackCharge;
        }
        if(value < 0)
            attackCharge = 0;
        else if(value > MAX_ATTACK_CHARGE)
            attackCharge = MAX_ATTACK_CHARGE;
        else
            attackCharge = value;
    }

    public Iterable<ItemStack> getPartSlots(){
        var items = new ArrayList<ItemStack>();

        for(int i = 0; i < PowerArmorContainer.SIZE; i++){
            items.add(inventory.getItem(i));
        }

        return items;
    }

    public boolean isEquipmentVisible(BodyPart bodyPart){
        if(bodyPart.isArmorItem())
            return hasArmor(bodyPart);
        else return !getEquipment(bodyPart).isEmpty();
    }

    public boolean hasArmor(BodyPart bodyPart) {
        return getArmorDurability(bodyPart) != 0;
    }

    public ItemStack getEquipment(BodyPart part) {
        return inventory.getItem(part.ordinal());
    }

    public int getArmorDurability(BodyPart bodyPart) {
        var itemStack = inventory.getItem(bodyPart.getId());
        if(itemStack.isEmpty()) return 0;
        return itemStack.getMaxDamage() - itemStack.getDamageValue();
    }

    public ArmorData getArmorData(){
        var data = new ArmorData(getId());
        data.inventory = serializeInventory(inventory);
        data.heat = heat;
        //data.attackCharge = attackCharge;

        return data;
    }

    public void setArmorData(ArmorData data){
        if(isClientSide) setClientArmorData(data);
        else setServerArmorData(data);
    }

    public void setClientArmorData(ArmorData data){
        deserializeInventory(inventory, data.inventory);
    }

    public void setServerArmorData(ArmorData data){
        //deserializeInventory(inventory, data.inventory);
        //heat = data.heat;
        //setAttackCharge(data.attackCharge);
    }

    public void setInventory(ListTag tags){
        deserializeInventory(inventory, tags);
    }

    protected void doHeatAction(int heat, SimpleAction action){
        if(!canDoAction(heat)) return;
        action.execute();
        addHeat(heat);
    }

    protected boolean canDoAction(int heat){
        return heat + this.heat <= maxHeat;
    }

    protected void syncDataWithClient() {
        if(isServerSide) getArmorData().sentToClient();
    }

    protected void syncDataWithServer() {
        if(isClientSide) getArmorData().sentToServer();
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

    private void getTotalArmor(){
        totalDefense   = 0;
        totalToughness = 0;

        for (var part : armor){
            var itemStack = inventory.getItem(part.ordinal());
            if (!itemStack.isEmpty()){
                totalDefense += ((PowerArmorItem)itemStack.getItem()).getMaterial().getDefenseForSlot(part);
                totalToughness += ((PowerArmorItem)itemStack.getItem()).getMaterial().getToughness();
            }
        }
    }


}
