package com.jetug.begining.common.entity.entity_type;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.jetug.begining.common.util.constants.Attributes.BODY_ARMOR_HEALTH;
import static net.minecraft.entity.ai.attributes.Attributes.*;

public class PlayerPowerArmorEntity extends CreatureEntity implements IAnimatable
{
    private AnimationFactory factory = new AnimationFactory(this);

    public PlayerPowerArmorEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return CreatureEntity.createMobAttributes()
                .add(MAX_HEALTH, 11.0D)
                .add(ATTACK_DAMAGE, 0.0D)
                .add(MOVEMENT_SPEED, 0.25D)
                .add(ATTACK_KNOCKBACK, 0.0D);
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<PlayerPowerArmorEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
        //getAttribute(BODY_ARMOR_HEALTH).getValue();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
            event.getController().animationSpeed = 4.0D;
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }


//    private Player pilot;
//
//    public int armamentLeftHand = -1;
//    public int armamentRightHand = -1;
//
//    private boolean leftInputDown = false;
//    private boolean rightInputDown = false;
//    private boolean forwardInputDown = false;
//    private boolean backInputDown = false;
//
//    public MSMob(World worldIn) {
//        super(worldIn);
//        this.heal(Float.MAX_VALUE);
//    }
//
//    @SideOnly(Side.CLIENT)
//    public void updateInputs(boolean p_184442_1_,boolean p_184442_2_,boolean p_184442_3_,boolean p_184442_4_) {
//        this.leftInputDown = p_184442_1_;
//        this.rightInputDown = p_184442_2_;
//        this.forwardInputDown = p_184442_3_;
//        this.backInputDown = p_184442_4_;
//        System.out.println(this.backInputDown);
//    }
//
//    private void control() {
//        if(this.isBeingRidden()) {
//            float f = 0.0F;
//
//            if(this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) f += 0.005F;
//            if(this.forwardInputDown) f += 0.04F;
//            if(this.backInputDown) f -= 0.005F;
//
//            this.motionX += (double)f;
//            this.motionZ += (double)f;
//        }
//    }
//
//    @Override
//    public boolean canPassengerSteer() {
//        return true;
//    }
//
//    @Override
//    public boolean canBreatheUnderwater() {
//        return true;
//    }
//
//    @Override
//    public NBTTagCompound writeToNBT(NBTTagCompound root) {
//        root = super.writeToNBT(root);
//        NBTTagCompound ms = new NBTTagCompound();
//        ms.setInteger("armamentLeftHand",this.armamentLeftHand);
//        ms.setInteger("armamentRightHand",this.armamentRightHand);
//        NBTTagList armaments = new NBTTagList();
//        for(int i = 0;i < this.getMSRegistryEntry().getArmamentCount();i++) {
//            MobileSuitArmament armament = this.getMSRegistryEntry().getArmament(i);
//            armaments.appendTag(armament.saveNBT());
//        }
//        ms.setTag("armaments",armaments);
//        root.setTag("mobileSuit",ms);
//        return root;
//    }
//
//    @Override
//    public void readFromNBT(NBTTagCompound root) {
//        super.readFromNBT(root);
//        if(root.hasKey("mobileSuit")) {
//            NBTTagCompound ms = root.getCompoundTag("mobileSuit");
//            this.armamentLeftHand = ms.getInteger("armamentLeftHand");
//            this.armamentRightHand = ms.getInteger("armamentRightHand");
//            if(ms.hasKey("armaments")) {
//                NBTTagList armaments = ms.getTagList("armaments",10);
//                for(int i = 0;i < armaments.tagCount();i++) {
//                    MobileSuitArmament armament = this.getMSRegistryEntry().getArmament(i);
//                    armament.loadNBT(armaments.getCompoundTagAt(i));
//                }
//            }
//        }
//    }
//
//    @Override
//    public boolean processInteract(EntityPlayer player,EnumHand hand) {
//        if(player.inventory.getStackInSlot(player.inventory.currentItem).getItem().getUnlocalizedName().equals("item."+GundamMod.MODID+".wrench")) {
//            // TODO: show customization interface
//        } else {
//            if(this.pilot != null) return false;
//            this.pilot = player;
//            this.pilot.startRiding(this);
//            IHumanCapability human = Human.getHuman(this.pilot);
//            human.setMS(this);
//            if(this.pilot.world.isRemote) Minecraft.getMinecraft().setRenderViewEntity(this);
//        }
//        return true;
//    }
//
//    @Override
//    public void onUpdate() {
//        super.onUpdate();
//        this.control();
//        this.move(MoverType.SELF,this.motionX,this.motionY,this.motionZ);
//    }
//
//    public EntityPlayer getPilot() {
//        return this.pilot;
//    }
//
//    public MobileSuit getMSRegistryEntry() {
//        return MSRegistry.getMobileSuit(this.getName());
//    }
//
//    @Override
//    public boolean canRiderInteract() {
//        return true;
//    }
//
//    @Override
//    public boolean canBeSteered() {
//        return true;
//    }
//
//    private void updateRiderPosition(Entity entity) {
//        if(entity != null) {
//            entity.setPosition(this.posX,this.posY+(getMountedYOffset()+entity.getYOffset())/2,this.posZ);
//        }
//    }
//
//    @Override
//    public void updatePassenger(Entity passenger) {
//        this.updateRiderPosition(passenger);
//        passenger.setInvisible(true);
//        this.rotationPitch = passenger.rotationPitch;
//        this.rotationYaw = passenger.rotationYaw;
//        this.motionX = passenger.motionX;
//        this.motionY = passenger.motionY;
//        this.motionZ = passenger.motionZ;
//    }
//
//    @Override
//    public void removePassenger(Entity passenger) {
//        if(passenger != null) passenger.setPosition(this.posX,this.posY,this.posZ);
//        super.removePassenger(passenger);
//        passenger.setInvisible(false);
//        if(passenger instanceof EntityPlayer) {
//            IHumanCapability human = Human.getHuman((EntityPlayer)passenger);
//            human.setMS(null);
//            this.pilot = null;
//            human.syncToServer();
//            Minecraft.getMinecraft().setRenderViewEntity(passenger);
//        }
//    }
}