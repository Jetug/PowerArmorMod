package com.jetug.power_armor_mod.common.data.constants;

import com.jetug.power_armor_mod.common.util.Pos2D;

import java.awt.*;

public class Gui {
    public static final int HOTBAR_SLOT_COUNT = 9;
    public static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    public static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    public static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    public static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    public static final int VANILLA_FIRST_SLOT_INDEX = 0;
    public static final int INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    public static final int TAB_HEIGHT = 27;
    public static final int TAB_WIDTH = 29;

    public static final Pos2D TOP_TAB_ICON_POS_1 = new Pos2D(6, -20) ;
    public static final Pos2D TOP_TAB_ICON_POS_2 = new Pos2D(35, -20) ;

    public static final Pos2D BOTTOM_TAB_ICON_POS_1 = new Pos2D(6, -20) ;
    public static final Pos2D BOTTOM_TAB_ICON_POS_2 = new Pos2D(35, -20) ;

    public static final Pos2D HEAD_SLOT_POS        = new Pos2D(68, 8);
    public static final Pos2D BODY_SLOT_POS        = new Pos2D(68, 44);
    public static final Pos2D LEFT_ARM_SLOT_POS    = new Pos2D(59, 26);
    public static final Pos2D RIGHT_ARM_SLOT_POS   = new Pos2D(77, 26);
    public static final Pos2D LEFT_LEG_SLOT_POS    = new Pos2D(59, 62);
    public static final Pos2D RIGHT_LEG_SLOT_POS   = new Pos2D(77, 62);
    public static final Pos2D ENGINE_SLOT_POS      = new Pos2D(98, 26);

    public static final Pos2D FRAME_BODY_SLOT_POS        = new Pos2D(8 , 17);
    public static final Pos2D FRAME_LEFT_ARM_SLOT_POS    = new Pos2D(80, 35);
    public static final Pos2D FRAME_RIGHT_ARM_SLOT_POS   = new Pos2D(8 , 35);
    public static final Pos2D FRAME_LEFT_LEG_SLOT_POS    = new Pos2D(80, 71);
    public static final Pos2D FRAME_RIGHT_LEG_SLOT_POS   = new Pos2D(8 , 71);
    public static final Pos2D LEFT_HAND_SLOT_POS         = new Pos2D(80, 53);
    public static final Pos2D RIGHT_HAND_SLOT_POS        = new Pos2D(8 , 53);
    public static final Pos2D BACK_SLOT_POS              = new Pos2D(152, 17);
    public static final Pos2D ENGINE_SLOT_POS2           = new Pos2D(152, 35);
    public static final Pos2D COOLING_SLOT_POS           = new Pos2D(152, 71);

    public static final Rectangle HEAD_ICON_OFFSET        = createStandardIcon(19, 0);
    public static final Rectangle BODY_ICON_OFFSET        = createStandardIcon(19, 17);
    public static final Rectangle LEFT_ARM_ICON_OFFSET    = createStandardIcon(36, 0);
    public static final Rectangle RIGHT_ARM_ICON_OFFSET   = createStandardIcon(53, 0);
    public static final Rectangle LEFT_LEG_ICON_OFFSET    = createStandardIcon(36, 17);
    public static final Rectangle RIGHT_LEG_ICON_OFFSET   = createStandardIcon(53, 17);

    private static Rectangle createStandardIcon(int x, int y){
        return new Rectangle(x, y, 16,16);
    }
}
