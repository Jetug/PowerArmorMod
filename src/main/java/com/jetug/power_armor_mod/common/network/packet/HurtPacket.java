package com.jetug.power_armor_mod.common.network.packet;

import com.jetug.power_armor_mod.common.data.enums.DamageSourceEnum;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HurtPacket {
    int entityId = -1;
    DamageSourceEnum damageSource;
    float damage;

    public HurtPacket(int entityId, DamageSource damageSource, float damage) {
        var ds = DamageSourceEnum.getEnum(damageSource);
        init(entityId, ds, damage);
    }

    public HurtPacket(int entityId, DamageSourceEnum damageSource, float damage) {
        init(entityId, damageSource, damage);
    }

    public HurtPacket() {}

    private void init(int entityId, DamageSourceEnum damageSource, float damage){
        this.entityId = entityId;
        this.damageSource = damageSource ;
        this.damage = damage;
    }

    public static void write(HurtPacket message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.entityId);
        buffer.writeEnum(message.damageSource);
        buffer.writeFloat(message.damage);
    }

    public static HurtPacket read(FriendlyByteBuf buffer) {
        var entityId = buffer.readInt();
        var damageSource = buffer.readEnum(DamageSourceEnum.class);
        var damage = buffer.readFloat();

        return new HurtPacket(entityId, damageSource, damage);
    }

    public static void handle(HurtPacket message, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;
        var entity = player.level.getEntity(message.entityId);
        if (entity == null) return;
        entity.hurt(message.damageSource.damageSource, 0);
    }
}