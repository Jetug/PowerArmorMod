package com.jetug.chassis_core.common.util.helpers;

import static com.jetug.chassis_core.common.util.helpers.MathHelper.getInPercents;

public class AttackChargeController {


    private final int maxCharge;

    public int getAttackCharge() {
        return attackCharge;
    }

    protected int attackCharge = 0;

    public AttackChargeController(int maxCharge) {
        this.maxCharge = maxCharge;
    }

    public int getAttackChargeInPercent(){
        return getInPercents(attackCharge, maxCharge);
    }

    public boolean isChargingAttack(){
        return attackCharge > 5;
    }

    public boolean isMaxCharge(){
        return attackCharge == maxCharge;
    }

    public void addAttackCharge() {
        var value = 1;
        //if(isClientSide) doServerAction(ActionType.ADD_ATTACK_CHARGE);
        if(attackCharge + value <= maxCharge)
            attackCharge += value;
    }

    public void resetAttackCharge() {
        //if(isClientSide) doServerAction(ActionType.RESET_ATTACK_CHARGE);
        setAttackCharge(0);
    }

    public void setAttackCharge(int value){
        if(value == 0){
            var v = attackCharge;
        }
        if(value < 0)
            attackCharge = 0;
        else if(value > maxCharge)
            attackCharge = maxCharge;
        else
            attackCharge = value;
    }
}
