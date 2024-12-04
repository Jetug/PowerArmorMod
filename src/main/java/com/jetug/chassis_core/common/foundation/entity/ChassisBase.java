package com.jetug.chassis_core.common.foundation.entity;

import com.jetug.chassis_core.client.ClientConfig;
import com.jetug.chassis_core.client.render.utils.GeoUtils;
import com.jetug.chassis_core.client.render.utils.ResourceHelper;
import com.jetug.chassis_core.common.data.json.ChassisConfig;
import com.jetug.chassis_core.common.data.json.EquipmentConfig;
import com.jetug.chassis_core.common.events.ContainerChangedEvent;
import com.jetug.chassis_core.common.foundation.item.ChassisArmor;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import com.jetug.chassis_core.common.foundation.item.StackUtils;
import com.jetug.chassis_core.common.network.data.ArmorData;
import com.jetug.chassis_core.common.util.helpers.timer.TickTimer;
import mod.azure.azurelib.cache.object.GeoBone;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;
import static com.jetug.chassis_core.common.data.constants.NBT.ITEMS_TAG;
import static com.jetug.chassis_core.common.util.extensions.Collection.arrayListOf;
import static com.jetug.chassis_core.common.util.helpers.ContainerUtils.copyContainer;
import static com.jetug.chassis_core.common.util.helpers.ContainerUtils.isContainersEqual;
import static com.jetug.chassis_core.common.util.helpers.InventoryHelper.deserializeInventory;
import static com.jetug.chassis_core.common.util.helpers.InventoryHelper.serializeInventory;
import static java.util.Arrays.stream;
import static java.util.Collections.addAll;

public class ChassisBase extends EmptyLivingEntity implements ContainerListener {
    public static final int INVENTORY_SIZE = 6;
    public static HashMap<String, Integer> PART_IDS = new HashMap<>();

    static {
        var i = 0;
        PART_IDS.put(HELMET, i++);
        PART_IDS.put(BODY_ARMOR, i++);
        PART_IDS.put(LEFT_ARM_ARMOR, i++);
        PART_IDS.put(RIGHT_ARM_ARMOR, i++);
        PART_IDS.put(LEFT_LEG_ARMOR, i++);
        PART_IDS.put(RIGHT_LEG_ARMOR, i++);
    }

    public final String[] armor = new String[]{
            HELMET,
            BODY_ARMOR,
            LEFT_ARM_ARMOR,
            RIGHT_ARM_ARMOR,
            LEFT_LEG_ARMOR,
            RIGHT_LEG_ARMOR,
    };
    protected final TickTimer timer = new TickTimer();
    protected final boolean isClientSide = level().isClientSide;
    protected final boolean isServerSide = !level().isClientSide;
    private final Lazy<String> chassisId = Lazy.of(() -> ResourceHelper.getResourceName(ForgeRegistries.ENTITY_TYPES.getKey(this.getType())));

    public final HashMap<String, ArrayList<GeoBone>> attachmentForBone = new HashMap<>();
    public final HashMap<String, ResourceLocation> textureForBone = new HashMap<>();

    public Collection<String> bonesToHide = new ArrayList<>();
    public SimpleContainer inventory;
    protected float totalDefense;
    protected float totalToughness;
    protected int inventorySize = 6;
    protected HashMap<String, Integer> partIdMap = PART_IDS;
    protected String[] armorParts = new String[]{
            HELMET,
            BODY_ARMOR,
            LEFT_ARM_ARMOR,
            RIGHT_ARM_ARMOR,
            LEFT_LEG_ARMOR,
            RIGHT_LEG_ARMOR,
    };
    private ChassisConfig config = null;
    private ListTag serializedInventory;
    private Container previousContainer;

    public ChassisBase(EntityType<? extends LivingEntity> pEntityType, Level pLevel, HashMap<String, Integer> partIdMap) {
        super(pEntityType, pLevel);
        this.partIdMap = partIdMap;
        this.inventorySize = partIdMap.size();
        init();
    }

    public ChassisBase(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        init();
    }

    public static ChassisEquipment getAsChassisEquipment(ItemStack itemStack) {
        return (ChassisEquipment) itemStack.getItem();
    }

    public static <T, R> Collection<R> returnCollection(Collection<T> collection, Function<T, R> function) {
        var result = new ArrayList<R>();
        for (var item : collection) {
            var val = function.apply(item);
            if (val != null) result.add(val);
        }
        return result;
    }

    public void init() {
        noCulling = true;
        initInventory();
        updateParams();
    }

    //GETTERS
    public float getTotalDefense() {
        return totalDefense;
    }

    public float getTotalToughness() {
        return totalToughness;
    }

    public Collection<String> getBonesToHide() {
        return bonesToHide;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public void damageArmor(DamageSource damageSource, float damage) {
        if (isServerSide) {
            if (!damageSource.is(DamageTypes.FALL)) {
                damageArmorItem(HELMET, damageSource, damage);
                damageArmorItem(BODY_ARMOR, damageSource, damage);
                damageArmorItem(LEFT_ARM_ARMOR, damageSource, damage);
                damageArmorItem(RIGHT_ARM_ARMOR, damageSource, damage);
            }
            damageArmorItem(LEFT_LEG_ARMOR, damageSource, damage);
            damageArmorItem(RIGHT_LEG_ARMOR, damageSource, damage);
        }
    }

    public void damageArmorItem(String chassisPart, DamageSource damageSource, float damage) {
        var itemStack = getEquipment(chassisPart);

        if (itemStack.getItem() instanceof ChassisArmor armorItem) {
            armorItem.damageArmor(itemStack, (int) damage);
            setEquipment(chassisPart, itemStack);
        }
    }

//    public ArrayList<String> getHiddenBones(){
//        var hidden = new ArrayList<String>();
//        for(var item : getVisibleEquipment()) {
//            var equipment = getAsChassisEquipment(item);
//            var allMods = equipment.getConfig().mods.clone();
//            var mods = StackUtils.getAttachments(item);
//
//            Arrays.stream(allMods).toList().removeAll(mods);
//            hidden.add(Arrays.toString(allMods));
//        }
//        return hidden;
//    }

    public ArrayList<String> getMods() {
        var res = new ArrayList<String>();
        for (var config : getItemConfigs())
            res.addAll(List.of(config.mods));
        return res;
    }

    public ArrayList<String> getVisibleMods() {
        var res = new ArrayList<String>();
        for (var item : getVisibleEquipment())
            res.addAll(StackUtils.getAttachments(item));
        return res;
    }
//    protected void createPartIdMap(){
//        var i = 0;
//        partIdMap.put(HELMET         , i++);
//        partIdMap.put(BODY_ARMOR     , i++);
//        partIdMap.put(LEFT_ARM_ARMOR , i++);
//        partIdMap.put(RIGHT_ARM_ARMOR, i++);
//        partIdMap.put(LEFT_LEG_ARMOR , i++);
//        partIdMap.put(RIGHT_LEG_ARMOR, i++);
//        inventorySize = i;

//    }

//    private final Map<String, EquipmentConfig> history = new HashMap<>();

    @OnlyIn(Dist.CLIENT)
    public Collection<GeoBone> getAttachmentForBone(String chassisBone) {
        var result = attachmentForBone.get(chassisBone);
        return result == null ? new ArrayList<>() : result;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTextureForBone(String bone) {
        return textureForBone.get(bone);
    }

    public Integer getPartId(String chassisPart) {
        var val = partIdMap.get(chassisPart);
        return val != null ? val : 0;
    }

    private int tickTimer = 10;

    @Override
    public void tick() {
        super.tick();

        syncDataWithClient();
//        syncDataWithServer();

        if(isClientSide) {
            if (tickTimer == 0) {
                tickTimer = 10;
                updateBones();
            } else tickTimer--;
        }
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
            containerReallyChanged(container);
            previousContainer = copyContainer(container);
        }
    }

    @Nullable
    public ChassisConfig getConfig() {
        if (config == null)
            config = ClientConfig.modResourceManager.getFrameConfig(getModelId());
        return config;
    }

    public String getModelId() {
        return chassisId.get();
    }

    public void containerReallyChanged(Container container) {
        updateParams();
        serializedInventory = serializeInventory(inventory);
        MinecraftForge.EVENT_BUS.post(new ContainerChangedEvent(this));
    }

    public boolean isEquipmentVisible(String chassisPart) {
        if (isArmorItem(chassisPart))
            return hasArmor(chassisPart);
        else return !getEquipment(chassisPart).isEmpty();
    }

    public boolean isArmorItem(String chassisPart) {
        return stream(armorParts).toList().contains(chassisPart);
    }

    public boolean hasArmor(String chassisPart) {
        return getArmorDurability(chassisPart) != 0;
    }

    public Collection<String> getEquipment() {
        return partIdMap.keySet();
    }

    public Collection<String> getPovEquipment() {
        return Collections.singleton(RIGHT_ARM_ARMOR);
    }

    public boolean hasEquipment(String part) {
        return !getEquipment(part).isEmpty();
    }

    public Collection<ItemStack> getVisibleEquipment() {
        return returnCollection(getEquipment(),
                (part) -> isEquipmentVisible(part) ?
                        getEquipment(part) : null);
    }

    public Collection<EquipmentConfig> getItemConfigs() {
        return returnCollection(getVisibleEquipment(), (equipment) -> ((ChassisEquipment) equipment.getItem()).getConfig());
    }

    public ItemStack getEquipment(String chassisPart) {
        return inventory.getItem(getPartId(chassisPart));
    }

    public void setEquipment(String chassisPart, ItemStack itemStack) {
        inventory.setItem(getPartId(chassisPart), itemStack);
    }

    public int getArmorDurability(String chassisPart) {
        var itemStack = getEquipment(chassisPart);
        if (itemStack.isEmpty()) return 0;
        return itemStack.getMaxDamage() - itemStack.getDamageValue();
    }

    public ArmorData getArmorData() {
        var data = new ArmorData(getId());
        data.inventory = serializedInventory;
        return data;
    }

    public void setArmorData(ArmorData data) {
        if (isClientSide) setClientArmorData(data);
        else setServerArmorData(data);
    }

    public void setClientArmorData(ArmorData data) {
        deserializeInventory(inventory, data.inventory);
    }

    public void setServerArmorData(ArmorData data) {
        //deserializeInventory(inventory, data.inventory);
        //heat = data.heat;
        //setAttackCharge(data.attackCharge);
    }

    public void setInventory(ListTag tags) {
        deserializeInventory(inventory, tags);
    }

    protected void initInventory() {
        SimpleContainer inventoryBuff = this.inventory;
        this.inventory = new SimpleContainer(inventorySize);
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
        if (isServerSide) getArmorData().sentToClient();
    }

    protected void syncDataWithServer() {
        if (isClientSide) getArmorData().sentToServer();
    }

    protected void saveInventory(CompoundTag compound) {
        if (inventory == null) return;
        compound.put(ITEMS_TAG, serializeInventory(inventory));
    }

    protected void loadInventory(@NotNull CompoundTag compound) {
        ListTag nbtTags = compound.getList(ITEMS_TAG, 10);
        initInventory();
        deserializeInventory(inventory, nbtTags);
    }

    protected float getMinSpeed() {
        return 0.05f;
    }

    private void updateParams() {
        updateTotalArmor();
        updateSpeed();
        if (isClientSide)
            updateBones();
    }



    @OnlyIn(Dist.CLIENT)
    protected void updateBones() {
        attachmentForBone.clear();
        bonesToHide.clear();
        textureForBone.clear();

        for (var stack : getVisibleEquipment()) {
            var item = getAsChassisEquipment(stack);
            var config = item.getConfig();
            if(config == null) continue;
            addAll(bonesToHide, config.hide);

            for (var attachment : config.attachments) {
                if (stream(config.mods).toList().contains(attachment.armor)
                        && !StackUtils.hasAttachment(stack, attachment.armor))
                    continue;

                var bone = GeoUtils.getBone(config.getModel(), attachment.armor);
                if (bone == null) continue;

                var variant = StackUtils.getVariant(stack);

                textureForBone.put(attachment.armor, config.getTexture(variant));

                if (!attachmentForBone.containsKey(attachment.frame))
                    attachmentForBone.put(attachment.frame, arrayListOf(bone));
                else attachmentForBone.get(attachment.frame).add(bone);
            }
        }
    }

    protected void updateSpeed() {
        setSpeed(getSpeedAttribute());
    }

    public void updateTotalArmor() {
        this.totalDefense = 0;
        this.totalToughness = 0;

        for (var part : armor) {
            if (getEquipment(part).getItem() instanceof ChassisArmor armorItem) {
                this.totalDefense += armorItem.getMaterial().getDefenseForSlot(part);
                this.totalToughness += armorItem.getMaterial().getToughness();
            }
        }
    }

    public float getSpeedAttribute() {
        return (float) getAttributeValue(Attributes.MOVEMENT_SPEED);
    }
}
