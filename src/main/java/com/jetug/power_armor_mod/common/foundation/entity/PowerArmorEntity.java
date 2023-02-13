package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.client.gui.PowerArmorContainer;
import com.jetug.power_armor_mod.common.foundation.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.network.data.ArmorData;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.*;
import com.jetug.power_armor_mod.common.util.helpers.*;
import com.jetug.power_armor_mod.common.util.helpers.timer.*;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.jetug.power_armor_mod.common.foundation.registery.ItemsRegistry.*;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;
import static net.minecraft.util.Mth.*;
import static org.apache.logging.log4j.Level.*;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.*;

public class PowerArmorEntity extends LivingEntity implements IAnimatable, /*IJumpingMount,*/ IPowerArmor, ContainerListener {
    public static final String SLOT_TAG = "Slot";
    public static final String ITEMS_TAG = "Items";
    public static final float ROTATION = (float) Math.PI / 180F;

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);

//    public final PowerArmorPartEntity headHitBox;
//    public final PowerArmorPartEntity bodyHitBox;
//    public final PowerArmorPartEntity leftArmHitBox;
//    public final PowerArmorPartEntity rightArmHitBox;
//    public final PowerArmorPartEntity leftLegHitBox;
//    public final PowerArmorPartEntity rightLegHitBox;
//    public final PowerArmorPartEntity[] subEntities;

    public final BodyPart[] parts = new BodyPart[]{
            HEAD      ,
            BODY      ,
            LEFT_ARM  ,
            RIGHT_ARM ,
            LEFT_LEG  ,
            RIGHT_LEG ,
    };

    //private final Iterable<ItemStack> armorSlots;
    public final Speedometer speedometer = new Speedometer(this);
    public SimpleContainer inventory;

    protected boolean isJumping;
    protected float playerJumpPendingScale;

    private final boolean isClientSide = level.isClientSide;
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final TickTimer clientTimer = new TickTimer();

    private boolean isDashing = false;
    private DashDirection dashDirection;

    private int maxHeat = 0;
    private int heat = 0;

    public PowerArmorEntity(EntityType<? extends LivingEntity> type, Level worldIn) {
        super(type, worldIn);
        noCulling = true;

//        headHitBox      = createArmorPart(HEAD     , 0.6f, 0.6f);
//        bodyHitBox      = createArmorPart(BODY     , 0.7f, 1.0f);
//        leftArmHitBox   = createArmorPart(LEFT_ARM , 0.5f, 1.0f);
//        rightArmHitBox  = createArmorPart(RIGHT_ARM, 0.5f, 1.0f);
//        leftLegHitBox   = createArmorPart(LEFT_LEG , 0.6f, 1.0f);
//        rightLegHitBox  = createArmorPart(RIGHT_LEG, 0.6f, 1.0f);
//        subEntities = new PowerArmorPartEntity[]{ headHitBox, bodyHitBox, leftArmHitBox, rightArmHitBox, leftLegHitBox, rightLegHitBox };
        initInventory();

        clientTimer.addTimer(new LoopTimerTask(() -> {
            heat -= 1;
        }));
    }

//    private void updateHitboxes() {
//        if(!level.isClientSide) {
//            for (PowerArmorPartEntity subEntity : subEntities) {
//                if(!subEntity.shouldContinuePersisting())
//                    level.addFreshEntity(subEntity);
//            }
//        }
//    }

    public Iterable<ItemStack> getPartSlots(){
        var items = new ArrayList<ItemStack>();

        for(int i = 0; i < PowerArmorContainer.SIZE; i++){
            items.add(inventory.getItem(i));
        }

        return items;
    }

//    @Override
//    public EntityDimensions getDimensions(Pose p_21047_) {
//        return super.getDimensions(p_21047_);
//    }

    private PowerArmorPartEntity createArmorPart(BodyPart bodyPart, float xz, float y){
        return new PowerArmorPartEntity(this, bodyPart, xz, y);
    }

    public void openGUI(Player playerEntity) {
        Global.referenceMob = this;

        if (!this.level.isClientSide) {
            playerEntity.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_) {
                    return new PowerArmorContainer(p_createMenu_1_, inventory, p_createMenu_2_, PowerArmorEntity.this);
                }

                @Override
                public Component getDisplayName() {
                    return PowerArmorEntity.this.getDisplayName();
                }
            });
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.JUMP_STRENGTH, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    public ItemStack getItem(BodyPart part) {
        return inventory.getItem(part.ordinal());
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

    private void syncDataWithClient() {
        getArmorData().sentToClient();
    }

    private void syncDataWithServer() {
        getArmorData().sentToServer();
    }

    public void setInventory(ListTag tags){
        deserializeInventory(inventory, tags);
    }

    private void saveInventory(CompoundTag compound){
        if (inventory == null) return;
        compound.put(ITEMS_TAG, serializeInventory(inventory));
    }

    private void loadInventory(@NotNull CompoundTag compound) {
        ListTag nbtTags = compound.getList(ITEMS_TAG, 10);
        initInventory();
        deserializeInventory(inventory, nbtTags);
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

    private void initInventory(){
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

    public boolean hasArmor(BodyPart bodyPart) {
        return getArmorDurability(bodyPart) != 0;
    }

    public int getArmorDurability(BodyPart bodyPart) {
        var isClientSide = getLevel().isClientSide;
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

    public void dash(DashDirection direction) {
        if (!(getControllingPassenger() instanceof Player player) || !isOnGround())
            return;

        isDashing = true;
        clientTimer.addTimer(new PlayOnceTimerTask(10, () -> isDashing = false));
        dashDirection = direction;

        float viewYRot = player.getViewYRot(1);
        float rotation = viewYRot * ROTATION;
        float x = sin(rotation) * 3;
        float z = cos(rotation) * 3;

        Vec3 vector = new Vec3(-x, 0, z);

        switch (direction) {
            case FORWARD -> vector = new Vec3(-x, 0, z);
            case BACK -> vector = new Vec3(x, 0, -z);
            case RIGHT -> {
                float rot = (viewYRot + 90) * ROTATION;
                vector = new Vec3(-sin(rot), 0, cos(rot));
            }
            case LEFT -> {
                float rot = (viewYRot - 90) * ROTATION;
                vector = new Vec3(-sin(rot), 0, cos(rot));
            }
            case UP -> vector = new Vec3(0, 1, 0);
        }

        push(vector);
    }

    public void push(Vec3 vector){
        setDeltaMovement(getDeltaMovement().add(vector));
    }

    public boolean hurt(PowerArmorPartEntity part, DamageSource damageSource, float damage) {
//        if(isClientSide) {
//            PacketHandler.sendToServer(new HurtPacket(this.getId(), damageSource, damage));
//        }

        damageArmor(part.bodyPart, damage);
        return super.hurt(damageSource, damage);

    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        Global.LOGGER.log(INFO, "HURT isClientSide: " + level.isClientSide);

        if(!isClientSide){
            damageArmor(HEAD     , damage);
            damageArmor(BODY     , damage);
            damageArmor(LEFT_ARM , damage);
            damageArmor(RIGHT_ARM, damage);
            damageArmor(LEFT_LEG , damage);
            damageArmor(RIGHT_LEG, damage);
        }

        var attacker = damageSource.getEntity();
        if(attacker != null) {
            var minecraft = Minecraft.getInstance();
            minecraft.getProfiler().push("pick");
            minecraft.crosshairPickEntity = null;
            double d0 = attacker.getPickRadius();

            Vec3 vec3 = this.getEyePosition();
            double d1 = d0;

            d1 *= d1;
            if (minecraft.hitResult != null) {
                d1 = minecraft.hitResult.getLocation().distanceToSqr(vec3);
            }

            Vec3 vec31 = attacker.getViewVector(1.0F);
            Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
            AABB aabb = attacker.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);

            var entityHitResult = ProjectileUtil.getEntityHitResult(attacker, vec3, vec32, aabb, (p_172770_) ->
                    !p_172770_.isSpectator() && p_172770_.isPickable(), d1);

            //if(entityHitResult != null)
            if (entityHitResult != null) {
                var loc = entityHitResult.getLocation();
                var attackerAye = attacker.getEyePosition();
                var attackerForward = attacker.getForward();
                var attackVector = attackerAye.subtract(this.getEyePosition()).add(attackerForward);
                var headAABB = new AABB(position().add(0, 2.1, 0), position().add(0.6, 2.1 + 0.6, 0.6));
                //var headAABB = new AABB(new Vec3(0, 2.1, 0), new Vec3(0.6, 2.1 + 0.6, 0.6));
                if (headAABB.contains(loc)) {
                    Global.LOGGER.log(INFO, "HURT HEAD");
                }
            }
        }
        return super.hurt(damageSource, damage);
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean p_110255_1_) {
        this.isJumping = p_110255_1_;
    }

    @Override
    public void tick() {
        super.tick();

        if(!level.isClientSide) {
            getArmorData().sentToClient();

            if(isDashing) {
                pushEntitiesAround();
            }
        }

        speedometer.tick();
        clientTimer.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if(getControllingPassenger() instanceof Player player) {
            this.yHeadRot = this.getYRot();
            this.yBodyRot = player.yBodyRot;//this.getYRot();
        }

        //moveHitboxes();
    }

//    private void moveHitboxes() {
//        Vec3[] aVector3d = new Vec3[subEntities.length];
//
//        for (int j = 0; j < subEntities.length; ++j) {
//            aVector3d[j] = new Vec3(subEntities[j].getX(), subEntities[j].getY(), subEntities[j].getZ());
//        }
//
//        float rotation = getYRot() * ROTATION;
//        float xPos = cos(rotation);
//        float zPos = sin(rotation);
//        float armPos = 0.6f;
//        float legPos = 0.2f;
//
//        tickPart(headHitBox, 0, 2.1, 0);
//        tickPart(bodyHitBox, 0, 1.2, 0);
//        tickPart(rightArmHitBox, xPos * -armPos , 1.1, zPos * -armPos);
//        tickPart(leftArmHitBox , xPos * armPos , 1.1, zPos * armPos);
//        tickPart(rightLegHitBox, xPos * -legPos, 0, zPos * -legPos);
//        tickPart(leftLegHitBox , xPos * legPos , 0, zPos * legPos);
//
//        for (int l = 0; l < subEntities.length; ++l) {
//            subEntities[l].xo = aVector3d[l].x;
//            subEntities[l].yo = aVector3d[l].y;
//            subEntities[l].zo = aVector3d[l].z;
//            subEntities[l].xOld = aVector3d[l].x;
//            subEntities[l].yOld = aVector3d[l].y;
//            subEntities[l].zOld = aVector3d[l].z;
//        }
//    }

    private void tickPart(PowerArmorPartEntity part, double x, double y, double z) {
        part.setPos(getX() + x, getY() + y, getZ() + z);
    }

    @Override
    public void checkDespawn() {}

//    @Override
//    public PartEntity<?>[] getParts() {
//        return this.subEntities;
//    }
//
//    @Override
//    public boolean isMultipartEntity() {
//        return true;
//    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public boolean isInvisible() {
        var clientPlayer = Minecraft.getInstance().player;
        var pov = Minecraft.getInstance().options.getCameraType();

        if (hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON)
            return true;
        return super.isInvisible();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (super.isInvulnerableTo(damageSource)) {
            return true;
        }
        else return damageSource.getEntity() == getControllingPassenger();
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vector, InteractionHand hand) {
        Global.LOGGER.log(INFO, level.isClientSide);
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() == PA_FRAME.get())
            return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            openGUI(player);
            return InteractionResult.SUCCESS;
        }
        else if(!isVehicle()) {
            this.doPlayerRide(player);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }


//    @Override
//    public InteractionResult interact(Player player, InteractionHand hand) {
//        this.doPlayerRide(player);
//        return super.interact(player, hand);
//    }


    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }

    @Override
    public void positionRider(Entity entity) {
        super.positionRider(entity);

        float posX = sin(this.yBodyRot * ROTATION);
        float posZ = cos(this.yBodyRot * ROTATION);
        double posXZ = -0.3;
        double posY = 0.9;
        entity.setPos(this.getX() + (posXZ * posX),
                this.getY() + this.getPassengersRidingOffset() + entity.getMyRidingOffset() - posY,
                this.getZ() - (posXZ * posZ));

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (!this.isAlive()) return;

        if (this.isVehicle() &&  getControllingPassenger() instanceof Player) {
            var livingEntity = (LivingEntity)this.getControllingPassenger();
            assert livingEntity != null;
            this.setYRot(livingEntity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(livingEntity.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());

            float f = livingEntity.xxa /* * 0.5F*/;
            float f1 = livingEntity.zza;

            if (this.playerJumpPendingScale > 0.0F && !this.isJumping() && this.onGround) {
                double jump = this.getCustomJump() * this.playerJumpPendingScale * this.getBlockJumpFactor();

                Vec3 deltaMovement = this.getDeltaMovement();
                this.setDeltaMovement(deltaMovement.x, jump, deltaMovement.z);
                isJumping = true;
                hasImpulse = true;

                if (f1 > 0.0F) {
                    float f2 = sin(this.getYRot() * ROTATION);
                    float f3 = cos(this.getYRot() * ROTATION);
                    this.setDeltaMovement(this.getDeltaMovement().add(
                            -0.4F * f2 * this.playerJumpPendingScale,
                            0.0D,
                            0.4F * f3 * this.playerJumpPendingScale));
                }

                this.playerJumpPendingScale = 0.0F;
            }

            this.flyingSpeed = this.getSpeed() * 0.1F;
            if (this.isControlledByLocalInstance()) {
                this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                super.travel(new Vec3(f, travelVector.y, f1));
            } else if (livingEntity instanceof Player) {
                this.setDeltaMovement(Vec3.ZERO);
            }

            if (this.onGround) {
                this.playerJumpPendingScale = 0.0F;
                isJumping = false;
            }
        }
        else {
            this.flyingSpeed = 0.02F;
            super.travel(travelVector);
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity p_20123_) {
        return super.getDismountLocationForPassenger(p_20123_);
    }

    @Override
    public boolean causeFallDamage(float height, float p_149500_, @NotNull DamageSource p_149501_) {
        if (height > 1.0F) {
            this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        }

        int damage = this.calculateFallDamage(height, p_149500_);
        if (damage <= 0) {
            return false;
        } else {
            this.hurt(p_149501_, damage);
            if (this.isVehicle()) {
                for(Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(p_149501_, (float) damage);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return this.armorItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {}

    @Override
    public void containerChanged(@NotNull Container container) {
        if (!this.level.isClientSide) {
            syncDataWithClient();
        }
    }
    public void doPlayerRide(Player player) {
        player.setYRot(getYRot());
        player.setXRot(getXRot());
        player.startRiding(this);
    }



    public ArmorData getArmorData(){
        var data = new ArmorData(getId());
        data.inventory = serializeInventory(inventory);
        return data;
    }

    public void setArmorData(ArmorData data){
        deserializeInventory(inventory, data.inventory);
    }

//    @Override
//    public boolean causeFallDamage(float height, float p_225503_2_, @NotNull DamageSource damageSource) {
//        pushEntitiesAround();
//
//        int immune = 5;
//        int damage = this.calculateFallDamage(height, p_225503_2_) / 2;
//        if (damage <= immune) {
//            return false;
//        } else {
//            if (this.isVehicle()) {
//                for(Entity entity : getIndirectPassengers()) {
//                    entity.hurt(DamageSource.FALL, damage - immune);
//                }
//            }
//            animateHurt();
//            playBlockFallSound();
//            return true;
//        }
//    }

    public void pushEntitiesAround(){
        for(Entity entity : getEntitiesOfClass(3,1,3)) {
            if (entity == this || entity == getControllingPassenger())
                continue;
            double push = 0.5;
            Vec3 direction = VectorHelper.getDirection(position(), entity.position());
            entity.push(direction.x * push, 0.5, direction.z * push);
            entity.hurt(DamageSource.ANVIL, 10);
        }
    }

    private List<Entity> getEntitiesOfClass(double x, double y, double z) {
        return this.level.getEntitiesOfClass(Entity.class, new AABB(position(), position()).inflate(x, y, z));
    }

    public void jump(){
        this.playerJumpPendingScale = 1.0F;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<PowerArmorEntity> controller = new AnimationController(this, "controller", 0, this::animateArms);
        AnimationController<PowerArmorEntity> legsController = new AnimationController(this, "legs_controller", 0, this::animateLegs);
        data.addAnimationController(controller);
        data.addAnimationController(legsController);
    }

    @Nullable
    private Player getPlayer(){
        if(getControllingPassenger() instanceof Player player)
            return player;
        return null;
    }

    private <E extends IAnimatable> PlayState animateLegs(AnimationEvent<E> event) {
        AnimationController<E> controller = event.getController();
        controller.animationSpeed = 1.0D;

        var player = getPlayer();
        if (player != null) {
            if (event.isMoving()) {
                setAnimation(controller, "walk_legs", LOOP);
                controller.animationSpeed = speedometer.getSpeed() * 4.0D;
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState animateArms(AnimationEvent<E> event) {
        AnimationController<E> controller = event.getController();
        controller.animationSpeed = 1.0D;

        var player = getPlayer();
        if (player != null) {
            if (player.attackAnim > 0) {
                controller.animationSpeed = 2.0D;
                setAnimation(controller, "hit", PLAY_ONCE);
                return PlayState.CONTINUE;
            } else if (isDashing) {
                return animateDash(controller);
            } else if (hurtTime > 0) {
                setAnimation(controller, "hurt", PLAY_ONCE);
                return PlayState.CONTINUE;
            }
            else if (event.isMoving()) {
                setAnimation(controller, "walk_arms", LOOP);
                controller.animationSpeed = speedometer.getSpeed() * 4.0D;
                return PlayState.CONTINUE;
            }
        }

        setAnimation(controller, "idle", LOOP);
        return PlayState.CONTINUE;
    }

    @NotNull
    private <E extends IAnimatable> PlayState animateDash(AnimationController<E> controller) {
        switch (dashDirection) {
            case FORWARD -> {
                setAnimation(controller, "dash_forward", HOLD_ON_LAST_FRAME);
            }
            case BACK -> {
                setAnimation(controller, "dash_back", HOLD_ON_LAST_FRAME);
            }
            case RIGHT -> {
                setAnimation(controller, "dash_right", HOLD_ON_LAST_FRAME);
            }
            case LEFT -> {
                setAnimation(controller, "dash_left", HOLD_ON_LAST_FRAME);
            }
            case UP -> {
                setAnimation(controller, "dash_back", HOLD_ON_LAST_FRAME);
            }
        }
        return PlayState.CONTINUE;
    }

    private void setAnimation(AnimationController<?> controller, String name, ILoopType loopType){
        controller.setAnimation(new AnimationBuilder().addAnimation(name, loopType));
    }
}