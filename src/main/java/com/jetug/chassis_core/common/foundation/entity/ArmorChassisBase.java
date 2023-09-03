package com.jetug.chassis_core.common.foundation.entity;

import com.jetug.chassis_core.client.ClientConfig;
import com.jetug.chassis_core.common.data.json.FrameSettings;
import com.jetug.chassis_core.common.events.*;
import com.jetug.chassis_core.common.foundation.item.*;
import com.jetug.chassis_core.client.render.utils.ResourceHelper;
import com.jetug.chassis_core.common.util.helpers.timer.*;
import com.jetug.chassis_core.common.network.data.*;
import com.jetug.chassis_core.common.data.enums.*;

import net.minecraft.nbt.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.*;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.*;

import javax.annotation.Nullable;
import java.util.*;

import static com.jetug.chassis_core.common.data.constants.NBT.*;
import static com.jetug.chassis_core.common.data.enums.BodyPart.*;
import static com.jetug.chassis_core.common.util.helpers.ContainerUtils.copyContainer;
import static com.jetug.chassis_core.common.util.helpers.ContainerUtils.isContainersEqual;
import static com.jetug.chassis_core.common.util.helpers.InventoryHelper.*;

public class ArmorChassisBase extends EmptyLivingEntity implements ContainerListener {
    public static final int COOLING = 5;
    public static final int P_SIZE = values().length;

    protected final TickTimer timer = new TickTimer();
    protected final boolean isClientSide = level.isClientSide;
    protected final boolean isServerSide = !level.isClientSide;

    protected float totalDefense;
    protected float totalToughness;
    public SimpleContainer inventory;

    private String chassisId = null;
    private FrameSettings settings = null;
    private ListTag serializedInventory;
    private Container previousContainer;

    public final String[] armor = new String[]{
            HELMET,
            BODY_ARMOR,
            LEFT_ARM_ARMOR,
            RIGHT_ARM_ARMOR,
            LEFT_LEG_ARMOR,
            RIGHT_LEG_ARMOR,
    };
    protected HashMap<String, Integer> partIdMap = new HashMap<>();

    public String[] getEquipment(){
        return partIdMap.keySet().toArray(new String[0]);
    }

    public final BodyPart[] equipment = BodyPart.values();


    public ArmorChassisBase(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        noCulling = true;
        initInventory();
        updateParams();
        //getType().getModelId()
    }

    @Override
    public void tick() {
        super.tick();
        syncDataWithClient();
        syncDataWithServer();
    }

    @Override
    protected void tickEffects() {
        removeEffects();
    }

    public void removeEffects() {
        for (var effectInstance : this.getActiveEffects()) {
            removeEffect(effectInstance.getEffect());
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
        if (previousContainer == null || !isContainersEqual(previousContainer, container)) {
            containerRealyChanged(container);
            previousContainer = copyContainer(container);
        }
    }

    @Nullable
    public FrameSettings getSettings(){
        if(settings == null)
            settings = ClientConfig.modResourceManager.getFrameSettings(getModelId());
        return settings;
    }

    public String getModelId(){
        if(chassisId == null)
            chassisId = ResourceHelper.getResourceName(getType().getRegistryName());
        return chassisId;
    }

    public void containerRealyChanged(Container container){
        updateParams();
        serializedInventory = serializeInventory(inventory);
        MinecraftForge.EVENT_BUS.post(new ContainerChangedEvent(this));
    }

    protected String[] armorParts = new String[]{
            HELMET          ,
            BODY_ARMOR      ,
            LEFT_ARM_ARMOR  ,
            RIGHT_ARM_ARMOR ,
            LEFT_LEG_ARMOR  ,
            RIGHT_LEG_ARMOR ,
    }

    public boolean isArmorItem(String chassisPart){
        return Arrays.stream(armorParts).toList().contains(chassisPart);
    }

    public boolean isEquipmentVisible(String chassisPart){
        if(isArmorItem(chassisPart))
            return hasArmor(chassisPart);
        else return !getEquipment(chassisPart).isEmpty();
    }

    public boolean hasArmor(BodyPart bodyPart) {
        return getArmorDurability(bodyPart) != 0;
    }

    public ItemStack getEquipment(BodyPart part) {
        return inventory.getItem(part.ordinal());
    }

    public boolean hasEquipment(BodyPart part){
        return !inventory.getItem(part.ordinal()).isEmpty();
    }
//
//    public boolean hasPowerKnuckle(){
//        return (getEquipment(RIGHT_HAND).getItem() instanceof PowerKnuckle);
//    }
//
//    public PowerKnuckle getPowerKnuckle(){
//        return (PowerKnuckle) getEquipment(RIGHT_HAND).getItem();
//    }
//
//    public boolean hasJetpack(){
//        return getEquipment(BACK).getItem() instanceof JetpackItem;
//    }
//
//    public JetpackItem getJetpack(){
//        return (JetpackItem) getEquipment(BACK).getItem();
//    }

    public int getArmorDurability(BodyPart bodyPart) {
        var itemStack = inventory.getItem(bodyPart.getId());
        if(itemStack.isEmpty()) return 0;
        return itemStack.getMaxDamage() - itemStack.getDamageValue();
    }

    public ArmorData getArmorData(){
        var data = new ArmorData(getId());
        data.inventory = serializedInventory;//serializeInventory(inventory);
        //data.heat = heatController.getHeat();
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

    protected void initInventory(){
        SimpleContainer inventoryBuff = this.inventory;
        this.inventory = new SimpleContainer(P_SIZE);
        if (inventoryBuff != null) {
            inventoryBuff.removeListener(this);
            int i = Math.min(inventoryBuff.getContainerSize(), this.inventory.getContainerSize());
            for (int j = 0; j < i; ++j) {
                var itemStack = inventoryBuff.getItem(j);
                if (!itemStack.isEmpty())
                    this.inventory.setItem(j, itemStack.copy());
            }
        }
        this.inventory.addListener(this);
        serializedInventory = serializeInventory(inventory);
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

    private void updateParams(){
        updateTotalArmor();
        updateSpeed();
    }

    protected float getMinSpeed(){
        return 0.05f;
    }

    protected void updateSpeed(){
        setSpeed(getSpeedAttribute());
    }

    private void updateTotalArmor(){
        this.totalDefense   = 0;
        this.totalToughness = 0;

        for (var part : armor){
            if (inventory.getItem(part.ordinal()).getItem() instanceof ChassisArmor armorItem){
                this.totalDefense   += armorItem.getMaterial().getDefenseForSlot(part);
                this.totalToughness += armorItem.getMaterial().getToughness();
            }
        }
    }

    public float getSpeedAttribute(){
        return (float)getAttributeValue(Attributes.MOVEMENT_SPEED);
    }
}
