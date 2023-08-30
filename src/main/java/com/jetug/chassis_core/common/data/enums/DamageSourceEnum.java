package com.jetug.chassis_core.common.data.enums;

import net.minecraft.world.damagesource.DamageSource;

public enum DamageSourceEnum {
    IN_FIRE             ( DamageSource.IN_FIRE            ),
    LIGHTNING_BOLT      ( DamageSource.LIGHTNING_BOLT     ),
    ON_FIRE             ( DamageSource.ON_FIRE            ),
    LAVA                ( DamageSource.LAVA               ),
    HOT_FLOOR           ( DamageSource.HOT_FLOOR          ),
    IN_WALL             ( DamageSource.IN_WALL            ),
    CRAMMING            ( DamageSource.CRAMMING           ),
    DROWN               ( DamageSource.DROWN              ),
    STARVE              ( DamageSource.STARVE             ),
    CACTUS              ( DamageSource.CACTUS             ),
    FALL                ( DamageSource.FALL               ),
    FLY_INTO_WALL       ( DamageSource.FLY_INTO_WALL      ),
    OUT_OF_WORLD        ( DamageSource.OUT_OF_WORLD       ),
    GENERIC             ( DamageSource.GENERIC            ),
    MAGIC               ( DamageSource.MAGIC              ),
    WITHER              ( DamageSource.WITHER             ),
    ANVIL               ( DamageSource.ANVIL              ),
    FALLING_BLOCK       ( DamageSource.FALLING_BLOCK      ),
    DRAGON_BREATH       ( DamageSource.DRAGON_BREATH      ),
    DRY_OUT             ( DamageSource.DRY_OUT            ),
    SWEET_BERRY_BUSH    ( DamageSource.SWEET_BERRY_BUSH   ),
    FREEZE              ( DamageSource.FREEZE             ),
    FALLING_STALACTITE  ( DamageSource.FALLING_STALACTITE ),
    STALAGMITE          ( DamageSource.STALAGMITE         );


    public static DamageSourceEnum getEnum(DamageSource damage){
        for (var value: values()) {
            if(value.damageSource.msgId.equals(damage.msgId))
                return value;
        }
        return null;
    }

    public final DamageSource damageSource;

    DamageSourceEnum( DamageSource damageSource) {
        this.damageSource = damageSource;
    }

    private static final ActionType[] values = ActionType.values();

    public static ActionType getById(int id) {
        return values[id];
    }

    public int getId() {
        return ordinal();
    }
}
