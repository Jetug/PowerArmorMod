package com.jetug.power_armor_mod.common.capability.data;

import com.jetug.power_armor_mod.common.entity.entitytype.PowerArmorPartEntity;
import com.jetug.power_armor_mod.common.network.PacketHandler;
import com.jetug.power_armor_mod.common.network.packet.ArmorClientUpdatePacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartClientPacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartPacket;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;

public class ArmorPartData implements IArmorPartData {
    public static final String DURABILITY = "durability";
    public static final String DEFENSE = "defense";
    private static final int size = 6;

    private final Entity entity;
    private float durability;
    private double defense;

    private float head    ;
    private float body    ;
    private float leftArm ;
    private float rightArm;
    private float leftLeg ;
    private float rightLeg;
    private final float[] durabilityArray = new float[6];


    public ArmorPartData(Entity entity) {
        this.entity = entity;
//        durabilityArray.add(head    );
//        durabilityArray.add(body    );
//        durabilityArray.add(leftArm );
//        durabilityArray.add(rightArm);
//        durabilityArray.add(leftLeg );
//        durabilityArray.add(rightLeg);

    }

//    @Override
//    public float[] getDurabilityArray() {
//        return durability;
//    }
//
//    @Override
//    public void setDurabilityArray(float[] array) {
//        if(array != null && array.length == size)
//            durability = array;
//    }

    @Override
    public float getDurability(BodyPart part) {
        return durabilityArray[part.getId()];

//        switch (part){
//            case HEAD:
//                return head;
//                //break;
//            case BODY:
//                return body;
//                //break;
//            case LEFT_ARM:
//                return leftArm;
//                //break;
//            case RIGHT_ARM:
//                return rightArm;
//                //break;
//            case LEFT_LEG:
//                return leftLeg;
//                //break;
//            case RIGHT_LEG:
//                return rightLeg;
//                //break;
//        }
//        return head;
    }

    @Override
    public void setDurability(BodyPart part, float value) {
        durabilityArray[part.getId()] = value;
//        switch (part){
//            case HEAD:
//                head = value;
//                break;
//            case BODY:
//                body = value;
//                break;
//            case LEFT_ARM:
//                leftArm = value;
//                break;
//            case RIGHT_ARM:
//                rightArm = value;
//                break;
//            case LEFT_LEG:
//                leftLeg = value;
//                break;
//            case RIGHT_LEG:
//                rightLeg = value;
//                break;
//        }
//        durability = value;
    }

    @Override
    public double getDefense() {
        return defense;
    }

    @Override
    public void setDefense(double value) {
        defense = value;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

//    @Override
//    public void copyFrom(IArmorPartData source) {
//        durability = source.getDurabilityArray();
//        defense = source.getDefense();
//    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        for(int i = 0; i < durabilityArray.length; i++) {
            nbt.putFloat(DURABILITY + i, durabilityArray[i]);
        }
//        int count = -1;

//        nbt.putFloat(DURABILITY + ++count, durabilityArray[count]);
//        nbt.putFloat(DURABILITY + ++count, durabilityArray[count]);
//        nbt.putFloat(DURABILITY + ++count, durabilityArray[count]);
//        nbt.putFloat(DURABILITY + ++count, durabilityArray[count]);
//        nbt.putFloat(DURABILITY + ++count, durabilityArray[count]);
//        nbt.putFloat(DURABILITY + ++count, durabilityArray[count]);

        nbt.putDouble(DEFENSE, defense);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for(int i = 0; i < durabilityArray.length; i++) {
            durabilityArray[i] = nbt.getFloat(DURABILITY + i);
        }
//        int count = -1;

//        durabilityArray[++count] = nbt.getFloat(ArmorPartData.DURABILITY + count);
//        durabilityArray[++count] = nbt.getFloat(ArmorPartData.DURABILITY + count);
//        durabilityArray[++count] = nbt.getFloat(ArmorPartData.DURABILITY + count);
//        durabilityArray[++count] = nbt.getFloat(ArmorPartData.DURABILITY + count);
//        durabilityArray[++count] = nbt.getFloat(ArmorPartData.DURABILITY + count);
//        durabilityArray[++count] = nbt.getFloat(ArmorPartData.DURABILITY + count);

        defense = nbt.getDouble(ArmorPartData.DEFENSE);
//
//        durability = data.getDouble(DURABILITY);
//        defense = data.getDouble(DEFENSE);
    }

    @Override
    public void syncWithClient(ServerPlayerEntity player) {
        PacketHandler.sendTo(new ArmorPartPacket(this), player);
    }

    @Override
    public void syncWithServer() {
        PacketHandler.sendToServer(new ArmorPartClientPacket(this));
    }

    @Override
    public void syncFromServer() {
        PacketHandler.sendToServer(new ArmorClientUpdatePacket(this));
    }

    @Override
    public boolean syncWithAll() {
        return false;
    }
}