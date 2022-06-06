package com.jetug.power_armor_mod.common.util.annotations;

import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.enums.EquipmentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {
    BodyPart ArmorPart();
    EquipmentType Type();
}
