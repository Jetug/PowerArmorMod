package com.jetug.chassis_core.client.utils;

import com.jetug.chassis_core.client.KeyBindings;
import com.jetug.chassis_core.common.input.InputKey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

import java.util.Map;

import static com.jetug.chassis_core.client.ClientConfig.OPTIONS;

@OnlyIn(Dist.CLIENT)
public class KeyUtils {
    @OnlyIn(Dist.CLIENT)
    public static Lazy<Map<Integer, InputKey>> keyMap = Lazy.of(() -> Map.of(
            OPTIONS.keyUp     .getKey().getValue()  , InputKey.UP    ,
            OPTIONS.keyDown   .getKey().getValue()  , InputKey.DOWN  ,
            OPTIONS.keyLeft   .getKey().getValue()  , InputKey.LEFT  ,
            OPTIONS.keyRight  .getKey().getValue()  , InputKey.RIGHT ,
            OPTIONS.keyJump   .getKey().getValue()  , InputKey.JUMP  ,
            KeyBindings.LEAVE .getKey().getValue()  , InputKey.LEAVE ,
            OPTIONS.keyUse    .getKey().getValue()  , InputKey.USE   ,
            OPTIONS.keyAttack .getKey().getValue()  , InputKey.ATTACK
    ));

    @OnlyIn(Dist.CLIENT)
    public static InputKey getByKey(int key){
        return keyMap.get().get(key);
    }
}
