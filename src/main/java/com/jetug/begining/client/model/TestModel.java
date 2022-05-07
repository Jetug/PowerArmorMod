package com.jetug.begining.client.model;

import com.jetug.begining.ExampleMod;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class TestModel<T extends Entity> extends EntityModel<T> {
    public static final ModelResourceLocation LAYER_LOCATION = new ModelResourceLocation(new ResourceLocation(ExampleMod.MOD_ID, "test_model"), "main");

    private final ModelRenderer head;
    private final ModelRenderer helmet;
    private final ModelRenderer body;
    private final ModelRenderer body_top;
    private final ModelRenderer body_top_armor;
    private final ModelRenderer body_back;
    private final ModelRenderer Baloon1;
    private final ModelRenderer Baloon2;
    private final ModelRenderer boby_bottom;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer back_plate;
    private final ModelRenderer front_plate;
    private final ModelRenderer right_arm;
    private final ModelRenderer right_upper_arm;
    private final ModelRenderer right_shoulder_armor;
    private final ModelRenderer right_upper_arm_armor;
    private final ModelRenderer left_arm_piston2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer piston_head3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer right_lower_arm;
    private final ModelRenderer right_forearm_armor;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer right_forearm_frame;
    private final ModelRenderer right_hand2;
    private final ModelRenderer left_arm;
    private final ModelRenderer left_upper_arm;
    private final ModelRenderer left_upper_arm_frame;
    private final ModelRenderer left_arm_piston5;
    private final ModelRenderer cube_r8;
    private final ModelRenderer piston_head5;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer left_shoulder_armor;
    private final ModelRenderer left_upper_arm_armor;
    private final ModelRenderer left_lower_arm;
    private final ModelRenderer left_forearm_armor;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer left_forearm_frame;
    private final ModelRenderer drill;
    private final ModelRenderer left_leg;
    private final ModelRenderer left_upper_leg;
    private final ModelRenderer upper_leg_frame;
    private final ModelRenderer right_leg_piston2;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer piston_head4;
    private final ModelRenderer cube_r15;
    private final ModelRenderer cube_r16;
    private final ModelRenderer left_upper_leg_armor;
    private final ModelRenderer left_lower_leg;
    private final ModelRenderer lower_leg_frame;
    private final ModelRenderer cube_r17;
    private final ModelRenderer left_shoe;
    private final ModelRenderer left_knee;
    private final ModelRenderer cube_r18;
    private final ModelRenderer left_lower_leg_armor;
    private final ModelRenderer right_leg;
    private final ModelRenderer right_upper_leg;
    private final ModelRenderer right_upper_leg_frame;
    private final ModelRenderer right_leg_piston;
    private final ModelRenderer cube_r19;
    private final ModelRenderer cube_r20;
    private final ModelRenderer piston_head7;
    private final ModelRenderer cube_r21;
    private final ModelRenderer cube_r22;
    private final ModelRenderer right_upper_leg_armor;
    private final ModelRenderer right_lower_leg;
    private final ModelRenderer right_lower_leg_frame;
    private final ModelRenderer cube_r23;
    private final ModelRenderer right_shoe;
    private final ModelRenderer right_lower_leg_armor;
    private final ModelRenderer right_knee;
    private final ModelRenderer cube_r24;

    public TestModel() {
        texWidth = 256;
        texHeight = 256;

        head = new ModelRenderer(this);
        head.setPos(0.0F, -13.0F, 1.0F);
        head.texOffs(37, 43).addBox(-4.0F, -6.0F, -4.25F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        helmet = new ModelRenderer(this);
        helmet.setPos(26.25F, 2.0F, -15.0F);
        head.addChild(helmet);
        helmet.texOffs(128, 16).addBox(-31.25F, -5.0F, 19.0F, 10.0F, 4.0F, 2.0F, 0.0F, false);
        helmet.texOffs(57, 137).addBox(-31.25F, -5.0F, 9.0F, 10.0F, 4.0F, 1.0F, 0.0F, false);
        helmet.texOffs(128, 159).addBox(-28.25F, -6.0F, 8.0F, 4.0F, 4.0F, 2.0F, 0.0F, false);
        helmet.texOffs(154, 43).addBox(-30.25F, -8.0F, 19.0F, 8.0F, 3.0F, 1.0F, 0.0F, false);
        helmet.texOffs(156, 115).addBox(-30.25F, -9.0F, 18.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        helmet.texOffs(62, 46).addBox(-30.25F, -9.0F, 10.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        helmet.texOffs(80, 19).addBox(-27.25F, -8.0F, 9.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        helmet.texOffs(46, 34).addBox(-29.25F, -10.0F, 11.0F, 6.0F, 1.0F, 7.0F, 0.0F, false);
        helmet.texOffs(99, 23).addBox(-32.25F, -5.0F, 9.99F, 1.0F, 4.0F, 10.0F, 0.0F, false);
        helmet.texOffs(116, 77).addBox(-31.25F, -8.0F, 9.99F, 1.0F, 3.0F, 9.0F, 0.0F, false);
        helmet.texOffs(25, 96).addBox(-21.25F, -5.0F, 9.99F, 1.0F, 4.0F, 10.0F, 0.0F, false);
        helmet.texOffs(71, 116).addBox(-22.25F, -8.0F, 9.99F, 1.0F, 3.0F, 9.0F, 0.0F, false);
        helmet.texOffs(50, 143).addBox(-23.25F, -9.0F, 10.99F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        helmet.texOffs(112, 142).addBox(-30.25F, -9.0F, 10.99F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setPos(21.0F, -7.0F, -2.0F);


        body_top = new ModelRenderer(this);
        body_top.setPos(-21.0F, -2.0F, 3.0F);
        body.addChild(body_top);


        body_top_armor = new ModelRenderer(this);
        body_top_armor.setPos(30.0F, 30.0F, -15.0F);
        body_top.addChild(body_top_armor);
        body_top_armor.texOffs(143, 94).addBox(-35.0F, -25.0F, 7.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(0, 21).addBox(-37.0F, -30.0F, 9.0F, 14.0F, 7.0F, 12.0F, 0.0F, false);
        body_top_armor.texOffs(95, 92).addBox(-36.0F, -34.0F, 21.0F, 12.0F, 12.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(0, 0).addBox(-38.0F, -33.0F, 10.0F, 16.0F, 10.0F, 10.0F, 0.0F, false);
        body_top_armor.texOffs(53, 10).addBox(-36.0F, -22.0F, 13.0F, 12.0F, 2.0F, 6.0F, 0.0F, false);
        body_top_armor.texOffs(132, 59).addBox(-36.0F, -22.0F, 20.0F, 12.0F, 1.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(172, 15).addBox(-26.0F, -21.0F, 19.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(172, 4).addBox(-36.0F, -21.0F, 19.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(172, 0).addBox(-37.0F, -20.0F, 19.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(170, 161).addBox(-25.0F, -20.0F, 19.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(170, 150).addBox(-25.0F, -20.0F, 10.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(169, 62).addBox(-37.0F, -20.0F, 10.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(0, 41).addBox(-37.0F, -23.0F, 13.0F, 14.0F, 1.0F, 8.0F, 0.0F, false);
        body_top_armor.texOffs(38, 168).addBox(-36.0F, -23.0F, 10.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(109, 44).addBox(-34.0F, -23.0F, 10.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(51, 106).addBox(-32.0F, -23.0F, 10.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(75, 0).addBox(-28.0F, -23.0F, 10.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(0, 168).addBox(-26.0F, -23.0F, 10.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        body_top_armor.texOffs(51, 166).addBox(-24.0F, -23.0F, 10.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        body_top_armor.texOffs(45, 83).addBox(-24.0F, -22.0F, 13.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(45, 80).addBox(-24.0F, -22.0F, 17.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(18, 166).addBox(-37.0F, -23.0F, 10.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        body_top_armor.texOffs(127, 109).addBox(-37.0F, -22.0F, 18.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        body_top_armor.texOffs(51, 110).addBox(-24.0F, -22.0F, 18.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        body_top_armor.texOffs(130, 117).addBox(-36.0F, -33.0F, 8.0F, 12.0F, 2.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(13, 111).addBox(-36.0F, -26.0F, 8.0F, 12.0F, 3.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(27, 51).addBox(-36.0F, -31.0F, 8.0F, 3.0F, 5.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(0, 51).addBox(-27.0F, -31.0F, 8.0F, 3.0F, 5.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(128, 81).addBox(-36.0F, -32.0F, 9.0F, 12.0F, 2.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(98, 38).addBox(-37.0F, -34.0F, 20.0F, 14.0F, 4.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(70, 92).addBox(-37.0F, -34.0F, 9.0F, 1.0F, 4.0F, 11.0F, 0.0F, false);
        body_top_armor.texOffs(0, 92).addBox(-24.0F, -34.0F, 9.0F, 1.0F, 4.0F, 11.0F, 0.0F, false);
        body_top_armor.texOffs(38, 106).addBox(-36.0F, -32.0F, 10.0F, 1.0F, 3.0F, 10.0F, 0.0F, false);
        body_top_armor.texOffs(105, 9).addBox(-25.0F, -32.0F, 10.0F, 1.0F, 3.0F, 10.0F, 0.0F, false);
        body_top_armor.texOffs(134, 56).addBox(-35.0F, -33.0F, 7.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(69, 168).addBox(-35.0F, -32.0F, 7.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        body_top_armor.texOffs(100, 19).addBox(-26.0F, -32.0F, 7.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        body_back = new ModelRenderer(this);
        body_back.setPos(29.0F, 28.0F, -15.0F);
        body_top.addChild(body_back);


        Baloon1 = new ModelRenderer(this);
        Baloon1.setPos(0.0F, 0.0F, -0.25F);
        body_back.addChild(Baloon1);
        Baloon1.texOffs(91, 137).addBox(-27.0F, -32.5F, 23.25F, 3.0F, 14.0F, 2.0F, 0.0F, false);
        Baloon1.texOffs(0, 63).addBox(-27.0F, -30.5F, 26.25F, 3.0F, 10.0F, 1.0F, 0.0F, false);
        Baloon1.texOffs(108, 106).addBox(-28.0F, -31.5F, 22.25F, 5.0F, 12.0F, 4.0F, 0.0F, false);
        Baloon1.texOffs(23, 132).addBox(-29.0F, -30.5F, 23.25F, 7.0F, 10.0F, 2.0F, 0.0F, false);

        Baloon2 = new ModelRenderer(this);
        Baloon2.setPos(0.0F, 0.0F, 0.25F);
        body_back.addChild(Baloon2);
        Baloon2.texOffs(80, 137).addBox(-34.0F, -32.5F, 22.75F, 3.0F, 14.0F, 2.0F, 0.0F, false);
        Baloon2.texOffs(0, 21).addBox(-34.0F, -30.5F, 25.75F, 3.0F, 10.0F, 1.0F, 0.0F, false);
        Baloon2.texOffs(61, 108).addBox(-35.0F, -31.5F, 21.75F, 5.0F, 12.0F, 4.0F, 0.0F, false);
        Baloon2.texOffs(122, 130).addBox(-36.0F, -30.5F, 22.75F, 7.0F, 10.0F, 2.0F, 0.0F, false);

        boby_bottom = new ModelRenderer(this);
        boby_bottom.setPos(9.0F, 25.0F, -12.0F);
        body.addChild(boby_bottom);
        boby_bottom.texOffs(43, 0).addBox(-36.5F, -17.0F, 13.0F, 13.0F, 4.0F, 5.0F, 0.0F, false);
        boby_bottom.texOffs(0, 125).addBox(-32.0F, -13.6F, 12.0F, 4.0F, 2.0F, 7.0F, 0.0F, false);
        boby_bottom.texOffs(104, 0).addBox(-33.0F, -14.6F, 12.0F, 6.0F, 1.0F, 7.0F, 0.0F, false);
        boby_bottom.texOffs(68, 19).addBox(-31.0F, -11.6F, 12.0F, 2.0F, 1.0F, 7.0F, 0.0F, false);
        boby_bottom.texOffs(154, 145).addBox(-34.0F, -17.0F, 18.0F, 8.0F, 3.0F, 1.0F, 0.0F, false);
        boby_bottom.texOffs(123, 103).addBox(-35.75F, -20.0F, 12.0F, 12.0F, 4.0F, 1.0F, 0.0F, false);
        boby_bottom.texOffs(87, 66).addBox(-34.5F, -16.5F, 11.501F, 9.0F, 2.0F, 7.0F, 0.0F, false);
        boby_bottom.texOffs(57, 65).addBox(-36.75F, -16.5F, 12.501F, 13.0F, 2.0F, 5.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-34.114F, -15.536F, 15.4F);
        boby_bottom.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 0.7854F);
        cube_r1.texOffs(59, 87).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-25.914F, -15.536F, 15.4F);
        boby_bottom.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, 0.7854F);
        cube_r2.texOffs(14, 92).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);

        back_plate = new ModelRenderer(this);
        back_plate.setPos(-29.9465F, -15.5465F, 19.7196F);
        boby_bottom.addChild(back_plate);
        setRotationAngle(back_plate, 0.0873F, 0.0F, 0.0F);
        back_plate.texOffs(64, 153).addBox(-3.5F, -0.125F, 0.0F, 7.0F, 6.0F, 1.0F, 0.0F, false);
        back_plate.texOffs(134, 0).addBox(-4.5F, -0.125F, -1.0F, 9.0F, 7.0F, 1.0F, 0.0F, false);
        back_plate.texOffs(120, 73).addBox(-2.5F, 5.875F, 0.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        back_plate.texOffs(38, 95).addBox(-3.5F, 6.875F, -1.0F, 7.0F, 1.0F, 1.0F, 0.0F, false);

        front_plate = new ModelRenderer(this);
        front_plate.setPos(-30.0F, -15.6442F, 11.5316F);
        boby_bottom.addChild(front_plate);
        setRotationAngle(front_plate, -0.1309F, 0.0F, 0.0F);
        front_plate.texOffs(84, 41).addBox(-2.0F, 6.85F, -0.1F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        front_plate.texOffs(21, 116).addBox(-3.0F, 4.85F, -0.1F, 6.0F, 2.0F, 1.0F, 0.0F, false);
        front_plate.texOffs(157, 118).addBox(-3.0F, -0.15F, -1.1F, 6.0F, 5.0F, 1.0F, 0.0F, false);
        front_plate.texOffs(45, 76).addBox(-2.0F, 4.85F, -1.1F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        front_plate.texOffs(143, 30).addBox(-4.0F, -0.15F, -0.1F, 8.0F, 5.0F, 1.0F, 0.0F, false);

        right_arm = new ModelRenderer(this);
        right_arm.setPos(-8.0F, -8.0F, 0.0F);


        right_upper_arm = new ModelRenderer(this);
        right_upper_arm.setPos(33.9875F, -3.375F, -22.1875F);
        right_arm.addChild(right_upper_arm);
        right_upper_arm.texOffs(50, 73).addBox(-40.5375F, 1.375F, 18.6875F, 8.0F, 6.0F, 7.0F, 0.0F, false);
        right_upper_arm.texOffs(102, 138).addBox(-39.4875F, 6.875F, 20.1875F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        right_shoulder_armor = new ModelRenderer(this);
        right_shoulder_armor.setPos(-38.175F, 3.0F, 23.2F);
        right_upper_arm.addChild(right_shoulder_armor);
        right_shoulder_armor.texOffs(41, 21).addBox(-3.8125F, -2.625F, -5.0125F, 8.0F, 1.0F, 10.0F, 0.0F, false);
        right_shoulder_armor.texOffs(90, 54).addBox(-2.8125F, -3.625F, -4.0125F, 7.0F, 1.0F, 8.0F, 0.0F, false);
        right_shoulder_armor.texOffs(128, 73).addBox(-1.8125F, -4.625F, -3.0125F, 6.0F, 1.0F, 6.0F, 0.0F, false);
        right_shoulder_armor.texOffs(138, 85).addBox(-3.8125F, -1.625F, -5.0125F, 8.0F, 7.0F, 1.0F, 0.0F, false);
        right_shoulder_armor.texOffs(137, 62).addBox(-3.8125F, -1.625F, 3.9875F, 8.0F, 7.0F, 1.0F, 0.0F, false);
        right_shoulder_armor.texOffs(150, 106).addBox(-2.8125F, -1.625F, 4.9875F, 7.0F, 7.0F, 1.0F, 0.0F, false);
        right_shoulder_armor.texOffs(150, 97).addBox(-2.8125F, -1.625F, -5.9125F, 7.0F, 7.0F, 1.0F, 0.0F, false);
        right_shoulder_armor.texOffs(87, 106).addBox(-4.8125F, -1.625F, -4.0125F, 2.0F, 7.0F, 8.0F, 0.0F, false);

        right_upper_arm_armor = new ModelRenderer(this);
        right_upper_arm_armor.setPos(0.0F, 0.0F, 0.0F);
        right_upper_arm.addChild(right_upper_arm_armor);
        right_upper_arm_armor.texOffs(84, 99).addBox(-38.9376F, 2.455F, 24.3375F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        right_upper_arm_armor.texOffs(41, 159).addBox(-39.9875F, 11.375F, 19.6875F, 1.0F, 3.0F, 5.0F, 0.0F, false);
        right_upper_arm_armor.texOffs(9, 151).addBox(-35.9875F, 14.375F, 20.6875F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        right_upper_arm_armor.texOffs(138, 23).addBox(-39.9875F, 14.375F, 20.6875F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        right_upper_arm_armor.texOffs(28, 158).addBox(-35.9875F, 11.375F, 19.6875F, 1.0F, 3.0F, 5.0F, 0.0F, false);
        right_upper_arm_armor.texOffs(80, 0).addBox(-39.9875F, 7.375F, 19.6875F, 5.0F, 4.0F, 5.0F, 0.0F, false);
        right_upper_arm_armor.texOffs(0, 58).addBox(-38.9875F, 11.375F, 23.6875F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        left_arm_piston2 = new ModelRenderer(this);
        left_arm_piston2.setPos(-37.3375F, 14.275F, 27.7749F);
        right_upper_arm.addChild(left_arm_piston2);
        setRotationAngle(left_arm_piston2, 0.2182F, 0.0F, 0.0F);


        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(-12.6384F, -13.6616F, -9.3874F);
        left_arm_piston2.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, 0.0F);
        cube_r3.texOffs(156, 59).addBox(10.9884F, 1.7616F, 7.55F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        piston_head3 = new ModelRenderer(this);
        piston_head3.setPos(13.8116F, -12.6616F, -27.8374F);
        left_arm_piston2.addChild(piston_head3);


        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-13.9616F, 12.7616F, 27.5F);
        piston_head3.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.7854F, 0.0F, 0.0F);
        cube_r4.texOffs(112, 28).addBox(-1.0F, -1.25F, -1.25F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(-26.95F, 5.0F, 18.95F);
        piston_head3.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, 0.0F);
        cube_r5.texOffs(0, 0).addBox(11.9884F, 1.5116F, 7.55F, 2.0F, 7.0F, 2.0F, 0.0F, false);

        right_lower_arm = new ModelRenderer(this);
        right_lower_arm.setPos(-3.5F, 11.0F, 0.05F);
        right_arm.addChild(right_lower_arm);


        right_forearm_armor = new ModelRenderer(this);
        right_forearm_armor.setPos(38.75F, -0.75F, -17.0F);
        right_lower_arm.addChild(right_forearm_armor);
        right_forearm_armor.texOffs(84, 35).addBox(-42.25F, -2.25F, 8.45F, 1.0F, 7.0F, 11.0F, 0.0F, false);
        right_forearm_armor.texOffs(24, 78).addBox(-42.25F, -1.25F, 19.45F, 7.0F, 5.0F, 1.0F, 0.0F, false);
        right_forearm_armor.texOffs(31, 76).addBox(-36.25F, -2.25F, 8.45F, 1.0F, 7.0F, 11.0F, 0.0F, false);
        right_forearm_armor.texOffs(62, 33).addBox(-41.25F, 3.75F, 8.45F, 5.0F, 1.0F, 11.0F, 0.0F, false);
        right_forearm_armor.texOffs(79, 54).addBox(-41.25F, -2.25F, 8.45F, 5.0F, 1.0F, 4.0F, 0.0F, false);
        right_forearm_armor.texOffs(84, 13).addBox(-37.25F, -2.25F, 12.45F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        right_forearm_armor.texOffs(84, 10).addBox(-41.25F, -2.25F, 12.45F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(-31.75F, 0.75F, 15.45F);
        right_forearm_armor.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, -1.3439F, 0.0F);
        cube_r6.texOffs(166, 72).addBox(1.7704F, -1.5001F, 9.5689F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(-27.75F, 0.75F, 15.45F);
        right_forearm_armor.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 1.3439F, 0.0F);
        cube_r7.texOffs(166, 92).addBox(-9.8195F, -1.5001F, -7.9697F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        right_forearm_frame = new ModelRenderer(this);
        right_forearm_frame.setPos(38.75F, -0.75F, -17.0F);
        right_lower_arm.addChild(right_forearm_frame);
        right_forearm_frame.texOffs(28, 145).addBox(-41.25F, 0.15F, 21.7F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        right_forearm_frame.texOffs(0, 92).addBox(-40.75F, -0.75F, 19.45F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        right_forearm_frame.texOffs(59, 49).addBox(-40.75F, -0.75F, 8.45F, 4.0F, 4.0F, 11.0F, 0.0F, false);
        right_forearm_frame.texOffs(70, 98).addBox(-40.75F, -0.75F, 19.2F, 4.0F, 3.0F, 1.0F, 0.0F, false);
        right_forearm_frame.texOffs(84, 33).addBox(-37.75F, -0.75F, 19.45F, 1.0F, 3.0F, 4.0F, 0.0F, false);

        right_hand2 = new ModelRenderer(this);
        right_hand2.setPos(56.5594F, 0.2241F, -17.0F);
        right_lower_arm.addChild(right_hand2);
        right_hand2.texOffs(28, 149).addBox(-60.0594F, -3.2241F, 7.2F, 7.0F, 7.0F, 1.0F, 0.0F, false);

        left_arm = new ModelRenderer(this);
        left_arm.setPos(8.0F, -7.0F, 0.0F);


        left_upper_arm = new ModelRenderer(this);
        left_upper_arm.setPos(22.0F, 29.0F, 22.0F);
        left_arm.addChild(left_upper_arm);


        left_upper_arm_frame = new ModelRenderer(this);
        left_upper_arm_frame.setPos(0.0F, 0.0F, 0.0F);
        left_upper_arm.addChild(left_upper_arm_frame);
        left_upper_arm_frame.texOffs(137, 139).addBox(-20.5F, -26.5F, -24.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        left_upper_arm_frame.texOffs(100, 106).addBox(-19.9501F, -31.02F, -19.85F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        left_upper_arm_frame.texOffs(0, 78).addBox(-23.3F, -32.0F, -25.5F, 8.0F, 6.0F, 7.0F, 0.0F, false);

        left_arm_piston5 = new ModelRenderer(this);
        left_arm_piston5.setPos(-18.35F, -19.1F, -16.4126F);
        left_upper_arm_frame.addChild(left_arm_piston5);
        setRotationAngle(left_arm_piston5, 0.2182F, 0.0F, 0.0F);


        cube_r8 = new ModelRenderer(this);
        cube_r8.setPos(-12.6384F, -13.6616F, -9.3874F);
        left_arm_piston5.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, 0.0F);
        cube_r8.texOffs(157, 48).addBox(10.9884F, 1.7616F, 7.55F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        piston_head5 = new ModelRenderer(this);
        piston_head5.setPos(13.8116F, -12.6616F, -27.8374F);
        left_arm_piston5.addChild(piston_head5);


        cube_r9 = new ModelRenderer(this);
        cube_r9.setPos(-13.9616F, 12.7616F, 27.5F);
        piston_head5.addChild(cube_r9);
        setRotationAngle(cube_r9, -0.7854F, 0.0F, 0.0F);
        cube_r9.texOffs(36, 158).addBox(-1.0F, -1.25F, -1.25F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        cube_r10 = new ModelRenderer(this);
        cube_r10.setPos(-26.95F, 5.0F, 18.95F);
        piston_head5.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, 0.0F);
        cube_r10.texOffs(41, 21).addBox(11.9884F, 1.5116F, 7.55F, 2.0F, 7.0F, 2.0F, 0.0F, false);

        left_shoulder_armor = new ModelRenderer(this);
        left_shoulder_armor.setPos(-17.8125F, -30.375F, -20.9875F);
        left_upper_arm.addChild(left_shoulder_armor);
        left_shoulder_armor.texOffs(0, 51).addBox(-4.1875F, -2.625F, -5.0125F, 8.0F, 1.0F, 10.0F, 0.0F, false);
        left_shoulder_armor.texOffs(94, 76).addBox(-4.1875F, -3.625F, -4.0125F, 7.0F, 1.0F, 8.0F, 0.0F, false);
        left_shoulder_armor.texOffs(129, 37).addBox(-4.1875F, -4.625F, -3.0125F, 6.0F, 1.0F, 6.0F, 0.0F, false);
        left_shoulder_armor.texOffs(141, 130).addBox(-4.1875F, -1.625F, -5.0125F, 8.0F, 7.0F, 1.0F, 0.0F, false);
        left_shoulder_armor.texOffs(138, 121).addBox(-4.1875F, -1.625F, 3.9875F, 8.0F, 7.0F, 1.0F, 0.0F, false);
        left_shoulder_armor.texOffs(102, 151).addBox(-4.1875F, -1.625F, 4.9875F, 7.0F, 7.0F, 1.0F, 0.0F, false);
        left_shoulder_armor.texOffs(143, 150).addBox(-4.1875F, -1.625F, -5.9125F, 7.0F, 7.0F, 1.0F, 0.0F, false);
        left_shoulder_armor.texOffs(0, 108).addBox(2.8125F, -1.625F, -4.0125F, 2.0F, 7.0F, 8.0F, 0.0F, false);

        left_upper_arm_armor = new ModelRenderer(this);
        left_upper_arm_armor.setPos(0.0F, 0.0F, 0.0F);
        left_upper_arm.addChild(left_upper_arm_armor);
        left_upper_arm_armor.texOffs(116, 59).addBox(-21.0F, -26.0F, -24.5F, 5.0F, 8.0F, 5.0F, 0.0F, false);

        left_lower_arm = new ModelRenderer(this);
        left_lower_arm.setPos(3.5F, 10.0F, -0.1F);
        left_arm.addChild(left_lower_arm);


        left_forearm_armor = new ModelRenderer(this);
        left_forearm_armor.setPos(15.75F, -0.75F, -16.85F);
        left_lower_arm.addChild(left_forearm_armor);
        left_forearm_armor.texOffs(90, 0).addBox(-19.25F, -2.25F, 8.45F, 1.0F, 7.0F, 11.0F, 0.0F, false);
        left_forearm_armor.texOffs(155, 0).addBox(-19.25F, -1.25F, 19.45F, 7.0F, 5.0F, 1.0F, 0.0F, false);
        left_forearm_armor.texOffs(45, 87).addBox(-13.25F, -2.25F, 8.45F, 1.0F, 7.0F, 11.0F, 0.0F, false);
        left_forearm_armor.texOffs(0, 65).addBox(-18.25F, 3.75F, 8.45F, 5.0F, 1.0F, 11.0F, 0.0F, false);
        left_forearm_armor.texOffs(148, 37).addBox(-18.25F, -2.25F, 8.45F, 5.0F, 1.0F, 4.0F, 0.0F, false);
        left_forearm_armor.texOffs(89, 67).addBox(-14.25F, -2.25F, 12.45F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        left_forearm_armor.texOffs(89, 64).addBox(-18.25F, -2.25F, 12.45F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r11 = new ModelRenderer(this);
        cube_r11.setPos(-8.75F, 0.75F, 15.45F);
        left_forearm_armor.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, -1.3439F, 0.0F);
        cube_r11.texOffs(27, 167).addBox(1.7704F, -1.5001F, 9.5689F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r12 = new ModelRenderer(this);
        cube_r12.setPos(-4.75F, 0.75F, 15.45F);
        left_forearm_armor.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, 1.3439F, 0.0F);
        cube_r12.texOffs(96, 167).addBox(-9.8195F, -1.5001F, -7.9697F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        left_forearm_frame = new ModelRenderer(this);
        left_forearm_frame.setPos(15.75F, -0.75F, -16.85F);
        left_lower_arm.addChild(left_forearm_frame);
        left_forearm_frame.texOffs(67, 150).addBox(-18.25F, 0.15F, 21.7F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        left_forearm_frame.texOffs(139, 166).addBox(-17.75F, -0.75F, 20.45F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        left_forearm_frame.texOffs(26, 60).addBox(-17.75F, -0.75F, 8.45F, 4.0F, 4.0F, 11.0F, 0.0F, false);
        left_forearm_frame.texOffs(128, 166).addBox(-17.75F, -0.75F, 19.2F, 4.0F, 3.0F, 1.0F, 0.0F, false);
        left_forearm_frame.texOffs(60, 166).addBox(-14.75F, -0.75F, 20.45F, 1.0F, 3.0F, 3.0F, 0.0F, false);

        drill = new ModelRenderer(this);
        drill.setPos(-1.3827F, -1.1226F, -2.85F);
        left_lower_arm.addChild(drill);
        drill.texOffs(126, 150).addBox(-1.1173F, -0.8774F, -13.55F, 5.0F, 5.0F, 3.0F, 0.0F, false);
        drill.texOffs(54, 159).addBox(-0.1173F, 0.1226F, -16.55F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        drill.texOffs(27, 95).addBox(0.8827F, 1.1226F, -18.55F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        drill.texOffs(134, 45).addBox(-2.1173F, -1.8774F, -10.55F, 7.0F, 7.0F, 3.0F, 0.0F, false);
        drill.texOffs(21, 120).addBox(-3.1173F, -2.8774F, -7.55F, 9.0F, 9.0F, 2.0F, 0.0F, false);

        left_leg = new ModelRenderer(this);
        left_leg.setPos(4.0F, 5.0F, 1.5F);


        left_upper_leg = new ModelRenderer(this);
        left_upper_leg.setPos(13.5F, 0.0F, -1.5F);
        left_leg.addChild(left_upper_leg);


        upper_leg_frame = new ModelRenderer(this);
        upper_leg_frame.setPos(0.0F, 0.0F, -19.0F);
        left_upper_leg.addChild(upper_leg_frame);
        upper_leg_frame.texOffs(122, 90).addBox(-16.0F, 0.0F, 18.0F, 5.0F, 7.0F, 5.0F, 0.0F, false);
        upper_leg_frame.texOffs(59, 129).addBox(-16.5F, -0.1F, 17.5F, 6.0F, 1.0F, 6.0F, 0.0F, false);
        upper_leg_frame.texOffs(170, 48).addBox(-11.75F, 0.0F, 22.25F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        upper_leg_frame.texOffs(9, 170).addBox(-11.75F, 0.0F, 17.75F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        upper_leg_frame.texOffs(169, 167).addBox(-16.25F, 0.0F, 17.75F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        upper_leg_frame.texOffs(153, 168).addBox(-16.25F, 0.0F, 22.25F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        upper_leg_frame.texOffs(0, 108).addBox(-14.25F, 0.92F, 22.19F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        right_leg_piston2 = new ModelRenderer(this);
        right_leg_piston2.setPos(-21.4802F, 7.2953F, 17.3607F);
        upper_leg_frame.addChild(right_leg_piston2);
        setRotationAngle(right_leg_piston2, -0.0698F, 0.0F, 0.0F);


        cube_r13 = new ModelRenderer(this);
        cube_r13.setPos(0.0F, -6.375F, 0.0F);
        right_leg_piston2.addChild(cube_r13);
        setRotationAngle(cube_r13, -0.7854F, 0.0F, 0.0F);
        cube_r13.texOffs(121, 90).addBox(7.2F, -0.7F, -0.7F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r14 = new ModelRenderer(this);
        cube_r14.setPos(-10.7938F, 3.1047F, 18.55F);
        right_leg_piston2.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.0F, 0.0F, 0.0F);
        cube_r14.texOffs(53, 10).addBox(18.0938F, -9.2797F, -19.25F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        piston_head4 = new ModelRenderer(this);
        piston_head4.setPos(-5.7938F, 0.9047F, 21.55F);
        right_leg_piston2.addChild(piston_head4);


        cube_r15 = new ModelRenderer(this);
        cube_r15.setPos(5.802F, -0.885F, -21.55F);
        piston_head4.addChild(cube_r15);
        setRotationAngle(cube_r15, -0.7854F, 0.0F, 0.0F);
        cube_r15.texOffs(95, 122).addBox(7.4F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r16 = new ModelRenderer(this);
        cube_r16.setPos(-4.35F, 0.0F, -3.5F);
        piston_head4.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, 0.0F, 0.0F);
        cube_r16.texOffs(38, 98).addBox(17.652F, -6.885F, -18.55F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        left_upper_leg_armor = new ModelRenderer(this);
        left_upper_leg_armor.setPos(12.5F, 14.0F, -13.0F);
        left_upper_leg.addChild(left_upper_leg_armor);
        left_upper_leg_armor.texOffs(147, 71).addBox(-30.0F, -13.0F, 10.5F, 8.0F, 5.0F, 1.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(81, 154).addBox(-30.0F, -13.0F, 17.5F, 8.0F, 4.0F, 1.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(40, 111).addBox(-24.0F, -16.0F, 17.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(100, 110).addBox(-24.0F, -16.0F, 10.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(116, 168).addBox(-26.0F, -15.0F, 17.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(167, 111).addBox(-26.0F, -15.0F, 10.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(104, 3).addBox(-28.0F, -14.0F, 17.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(104, 0).addBox(-28.0F, -14.0F, 10.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(25, 103).addBox(-30.0F, -9.0F, 17.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(72, 92).addBox(-24.0F, -9.0F, 17.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(169, 127).addBox(-30.0F, -9.0F, 11.5F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(168, 134).addBox(-23.0F, -9.0F, 11.5F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(42, 134).addBox(-23.0F, -17.0F, 11.5F, 1.0F, 8.0F, 6.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(0, 150).addBox(-30.0F, -13.0F, 11.5F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        left_upper_leg_armor.texOffs(163, 150).addBox(-23.0F, -18.0F, 12.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        left_lower_leg = new ModelRenderer(this);
        left_lower_leg.setPos(2.75F, 7.1345F, -1.2F);
        left_leg.addChild(left_lower_leg);


        lower_leg_frame = new ModelRenderer(this);
        lower_leg_frame.setPos(12.0F, 0.0F, -19.3F);
        left_lower_leg.addChild(lower_leg_frame);
        lower_leg_frame.texOffs(44, 120).addBox(-17.25F, 0.1155F, 18.0F, 5.0F, 8.0F, 5.0F, 0.0F, false);
        lower_leg_frame.texOffs(94, 76).addBox(-13.0F, 1.7155F, 22.25F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        lower_leg_frame.texOffs(14, 92).addBox(-13.0F, 2.1155F, 17.75F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        lower_leg_frame.texOffs(59, 87).addBox(-17.5F, 2.1155F, 17.75F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        lower_leg_frame.texOffs(66, 33).addBox(-17.5F, 1.7155F, 22.25F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        lower_leg_frame.texOffs(15, 154).addBox(-15.5F, 1.7855F, 17.54F, 1.0F, 6.0F, 5.0F, 0.0F, false);
        lower_leg_frame.texOffs(142, 9).addBox(-17.5F, 0.0155F, 18.05F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        cube_r17 = new ModelRenderer(this);
        cube_r17.setPos(-14.7393F, 0.6957F, 18.6529F);
        lower_leg_frame.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.48F, 0.0F, 0.0F);
        cube_r17.texOffs(95, 160).addBox(-2.7501F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r17.texOffs(160, 130).addBox(0.6001F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, 0.0F, false);

        left_shoe = new ModelRenderer(this);
        left_shoe.setPos(-14.75F, 10.1155F, 20.5F);
        lower_leg_frame.addChild(left_shoe);
        left_shoe.texOffs(78, 19).addBox(-3.0F, -2.25F, -5.25F, 6.0F, 4.0F, 9.0F, 0.0F, false);
        left_shoe.texOffs(74, 73).addBox(-2.5F, -1.25F, -6.2F, 5.0F, 3.0F, 1.0F, 0.0F, false);

        left_knee = new ModelRenderer(this);
        left_knee.setPos(23.25F, 6.8655F, -13.3F);
        left_lower_leg.addChild(left_knee);
        left_knee.texOffs(76, 108).addBox(-30.0F, -7.25F, 17.5F, 8.0F, 2.0F, 1.0F, 0.0F, false);
        left_knee.texOffs(169, 156).addBox(-27.5F, -7.75F, 17.501F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        left_knee.texOffs(155, 78).addBox(-30.0F, -7.75F, 10.5F, 8.0F, 3.0F, 1.0F, 0.0F, false);
        left_knee.texOffs(60, 143).addBox(-30.0F, -8.75F, 9.5F, 8.0F, 5.0F, 1.0F, 0.0F, false);
        left_knee.texOffs(157, 125).addBox(-29.0F, -7.75F, 9.1F, 6.0F, 3.0F, 1.0F, 0.0F, false);
        left_knee.texOffs(37, 41).addBox(-30.0F, -7.75F, 11.5F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        left_knee.texOffs(0, 41).addBox(-23.0F, -7.75F, 11.5F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        left_knee.texOffs(141, 159).addBox(-30.0F, -7.25F, 13.5F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        left_knee.texOffs(84, 92).addBox(-23.0F, -7.25F, 13.5F, 1.0F, 2.0F, 4.0F, 0.0F, false);

        cube_r18 = new ModelRenderer(this);
        cube_r18.setPos(-29.5001F, -6.2931F, 16.0069F);
        left_knee.addChild(cube_r18);
        setRotationAngle(cube_r18, -0.7854F, 0.0F, 0.0F);
        cube_r18.texOffs(167, 97).addBox(-0.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r18.texOffs(167, 104).addBox(6.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);

        left_lower_leg_armor = new ModelRenderer(this);
        left_lower_leg_armor.setPos(23.25F, 6.3655F, -13.3F);
        left_lower_leg.addChild(left_lower_leg_armor);
        left_lower_leg_armor.texOffs(112, 23).addBox(-29.0F, -4.0F, 10.5F, 6.0F, 3.0F, 1.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(44, 120).addBox(-24.0F, 1.0F, 17.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(96, 0).addBox(-29.0F, 1.0F, 17.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(122, 143).addBox(-29.0F, -3.0F, 17.5F, 6.0F, 4.0F, 1.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(22, 63).addBox(-24.0F, -1.0F, 10.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(0, 100).addBox(-28.0F, -1.0F, 10.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(43, 0).addBox(-29.0F, -1.0F, 10.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(68, 28).addBox(-30.0F, -4.0F, 17.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(27, 58).addBox(-24.0F, -4.0F, 17.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(84, 122).addBox(-30.0F, -3.0F, 10.5F, 1.0F, 6.0F, 8.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(163, 34).addBox(-30.0F, -4.0F, 10.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(117, 162).addBox(-23.0F, -4.0F, 10.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(119, 115).addBox(-23.0F, -3.0F, 10.5F, 1.0F, 6.0F, 8.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(95, 122).addBox(-30.0F, 3.0F, 11.5F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(162, 28).addBox(-30.0F, 4.0F, 12.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(22, 63).addBox(-23.0F, 3.0F, 11.5F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        left_lower_leg_armor.texOffs(9, 135).addBox(-23.0F, 4.0F, 12.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        right_leg = new ModelRenderer(this);
        right_leg.setPos(-4.0F, 5.0F, 1.5F);


        right_upper_leg = new ModelRenderer(this);
        right_upper_leg.setPos(21.5F, 0.0F, -28.5F);
        right_leg.addChild(right_upper_leg);


        right_upper_leg_frame = new ModelRenderer(this);
        right_upper_leg_frame.setPos(8.1076F, -0.3655F, 9.0183F);
        right_upper_leg.addChild(right_upper_leg_frame);
        right_upper_leg_frame.texOffs(122, 23).addBox(-32.097F, 0.3655F, 17.0057F, 5.0F, 8.0F, 5.0F, 0.0F, false);
        right_upper_leg_frame.texOffs(130, 109).addBox(-32.597F, 0.2655F, 16.5057F, 6.0F, 1.0F, 6.0F, 0.0F, false);
        right_upper_leg_frame.texOffs(26, 172).addBox(-32.347F, 0.3655F, 21.2557F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        right_upper_leg_frame.texOffs(14, 172).addBox(-32.347F, 0.3655F, 16.7557F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        right_upper_leg_frame.texOffs(114, 123).addBox(-30.347F, 1.2855F, 21.1957F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        right_upper_leg_frame.texOffs(131, 171).addBox(-27.847F, 0.3655F, 21.2557F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        right_upper_leg_frame.texOffs(126, 171).addBox(-27.847F, 0.3655F, 16.7557F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        right_leg_piston = new ModelRenderer(this);
        right_leg_piston.setPos(-29.5879F, 7.6608F, 16.3424F);
        right_upper_leg_frame.addChild(right_leg_piston);
        setRotationAngle(right_leg_piston, -0.0698F, 0.0F, 0.0F);


        cube_r19 = new ModelRenderer(this);
        cube_r19.setPos(0.0F, -6.375F, 0.0F);
        right_leg_piston.addChild(cube_r19);
        setRotationAngle(cube_r19, -0.7854F, 0.0F, 0.0F);
        cube_r19.texOffs(65, 125).addBox(-0.8F, -0.7F, -0.7F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r20 = new ModelRenderer(this);
        cube_r20.setPos(-10.7938F, 3.1047F, 18.55F);
        right_leg_piston.addChild(cube_r20);
        setRotationAngle(cube_r20, 0.0F, 0.0F, 0.0F);
        cube_r20.texOffs(46, 60).addBox(10.0938F, -9.2797F, -19.25F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        piston_head7 = new ModelRenderer(this);
        piston_head7.setPos(-5.7938F, 0.9047F, 21.55F);
        right_leg_piston.addChild(piston_head7);


        cube_r21 = new ModelRenderer(this);
        cube_r21.setPos(5.802F, -0.885F, -21.55F);
        piston_head7.addChild(cube_r21);
        setRotationAngle(cube_r21, -0.7854F, 0.0F, 0.0F);
        cube_r21.texOffs(95, 125).addBox(-0.6F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r22 = new ModelRenderer(this);
        cube_r22.setPos(-4.35F, 0.0F, -3.5F);
        piston_head7.addChild(cube_r22);
        setRotationAngle(cube_r22, 0.0F, 0.0F, 0.0F);
        cube_r22.texOffs(117, 76).addBox(9.652F, -6.885F, -18.55F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        right_upper_leg_armor = new ModelRenderer(this);
        right_upper_leg_armor.setPos(12.576F, 14.0F, 25.0107F);
        right_upper_leg.addChild(right_upper_leg_armor);
        right_upper_leg_armor.texOffs(153, 16).addBox(-38.0653F, -13.0F, -0.4866F, 8.0F, 5.0F, 1.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(169, 58).addBox(-38.0653F, -15.0F, 6.5134F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(0, 127).addBox(-34.0653F, -14.0F, -0.4866F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(124, 3).addBox(-34.0653F, -14.0F, 6.5134F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(124, 0).addBox(-38.0653F, -16.0F, 6.5134F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(169, 28).addBox(-38.0653F, -15.0F, -0.4866F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(85, 165).addBox(-38.0653F, -18.0F, 1.5134F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(0, 124).addBox(-38.0653F, -16.0F, -0.4866F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(154, 139).addBox(-38.0653F, -13.0F, 6.5134F, 8.0F, 4.0F, 1.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(104, 123).addBox(-38.0653F, -9.0F, 6.5134F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(83, 122).addBox(-32.0653F, -9.0F, 6.5134F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(0, 135).addBox(-38.0653F, -17.0F, 0.5134F, 1.0F, 8.0F, 6.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(170, 32).addBox(-38.0653F, -9.0F, 0.5134F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(170, 10).addBox(-31.0653F, -9.0F, 0.5134F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        right_upper_leg_armor.texOffs(46, 60).addBox(-31.0653F, -12.0F, 0.5134F, 1.0F, 3.0F, 6.0F, 0.0F, false);

        right_lower_leg = new ModelRenderer(this);
        right_lower_leg.setPos(0.0F, 7.1345F, -1.2F);
        right_leg.addChild(right_lower_leg);


        right_lower_leg_frame = new ModelRenderer(this);
        right_lower_leg_frame.setPos(0.0F, 0.0F, 0.0F);
        right_lower_leg.addChild(right_lower_leg_frame);
        right_lower_leg_frame.texOffs(15, 140).addBox(-0.7393F, 1.7855F, -1.636F, 1.0F, 8.0F, 5.0F, 0.0F, false);
        right_lower_leg_frame.texOffs(88, 171).addBox(-2.7393F, 2.1155F, -1.526F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        right_lower_leg_frame.texOffs(83, 171).addBox(1.7607F, 2.1155F, -1.526F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        right_lower_leg_frame.texOffs(143, 23).addBox(-2.7393F, 0.0155F, -1.226F, 5.0F, 1.0F, 5.0F, 0.0F, false);
        right_lower_leg_frame.texOffs(148, 167).addBox(1.7607F, 1.7155F, 2.974F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        right_lower_leg_frame.texOffs(118, 9).addBox(-2.7393F, 1.7155F, 2.974F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        right_lower_leg_frame.texOffs(113, 44).addBox(-2.4893F, 0.8655F, -1.276F, 5.0F, 9.0F, 5.0F, 0.0F, false);

        cube_r23 = new ModelRenderer(this);
        cube_r23.setPos(0.0107F, 0.6956F, -0.6471F);
        right_lower_leg_frame.addChild(cube_r23);
        setRotationAngle(cube_r23, 0.48F, 0.0F, 0.0F);
        cube_r23.texOffs(67, 161).addBox(0.6001F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r23.texOffs(152, 161).addBox(-2.7501F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, 0.0F, false);

        right_shoe = new ModelRenderer(this);
        right_shoe.setPos(0.0F, 10.1155F, 1.2F);
        right_lower_leg_frame.addChild(right_shoe);
        right_shoe.texOffs(72, 78).addBox(-2.9893F, -2.25F, -5.226F, 6.0F, 4.0F, 9.0F, 0.0F, false);
        right_shoe.texOffs(103, 86).addBox(-2.4893F, -1.25F, -6.176F, 5.0F, 3.0F, 1.0F, 0.0F, false);

        right_lower_leg_armor = new ModelRenderer(this);
        right_lower_leg_armor.setPos(34.076F, 6.3655F, -2.2893F);
        right_lower_leg.addChild(right_lower_leg_armor);
        right_lower_leg_armor.texOffs(80, 160).addBox(-37.0653F, -4.0F, -0.4866F, 6.0F, 3.0F, 1.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(16, 124).addBox(-32.0653F, 1.0F, 6.5134F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(123, 16).addBox(-37.0653F, 1.0F, 6.5134F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(158, 7).addBox(-37.0653F, -3.0F, 6.5134F, 6.0F, 4.0F, 1.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(79, 49).addBox(-32.0653F, -1.0F, -0.4866F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(13, 108).addBox(-36.0653F, -1.0F, -0.4866F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(31, 63).addBox(-37.0653F, -1.0F, -0.4866F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(113, 59).addBox(-38.0653F, -4.0F, 6.5134F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(80, 112).addBox(-32.0653F, -4.0F, 6.5134F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(103, 123).addBox(-38.0653F, -3.0F, -0.4866F, 1.0F, 6.0F, 8.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(74, 165).addBox(-38.0653F, -4.0F, -0.4866F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(165, 66).addBox(-31.0653F, -4.0F, -0.4866F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(123, 1).addBox(-31.0653F, -3.0F, -0.4866F, 1.0F, 6.0F, 8.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(154, 153).addBox(-31.0653F, 3.0F, 0.5134F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(113, 154).addBox(-38.0653F, 3.0F, 0.5134F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(7, 164).addBox(-31.0653F, 4.0F, 1.5134F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        right_lower_leg_armor.texOffs(163, 161).addBox(-38.0653F, 4.0F, 1.5134F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        right_knee = new ModelRenderer(this);
        right_knee.setPos(34.076F, 6.8655F, -2.2893F);
        right_lower_leg.addChild(right_knee);
        right_knee.texOffs(157, 88).addBox(-38.0653F, -7.25F, 6.5134F, 8.0F, 2.0F, 1.0F, 0.0F, false);
        right_knee.texOffs(74, 171).addBox(-35.5653F, -7.75F, 6.5144F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        right_knee.texOffs(157, 83).addBox(-38.0653F, -7.75F, -0.4866F, 8.0F, 3.0F, 1.0F, 0.0F, false);
        right_knee.texOffs(45, 152).addBox(-38.0653F, -8.75F, -1.4866F, 8.0F, 5.0F, 1.0F, 0.0F, false);
        right_knee.texOffs(159, 23).addBox(-37.0653F, -7.75F, -1.9866F, 6.0F, 3.0F, 1.0F, 0.0F, false);
        right_knee.texOffs(0, 78).addBox(-38.0653F, -7.75F, 0.5134F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        right_knee.texOffs(68, 19).addBox(-31.0653F, -7.75F, 0.5134F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        right_knee.texOffs(0, 161).addBox(-38.0653F, -7.25F, 2.5134F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        right_knee.texOffs(106, 160).addBox(-31.0653F, -7.25F, 2.5134F, 1.0F, 2.0F, 4.0F, 0.0F, false);

        cube_r24 = new ModelRenderer(this);
        cube_r24.setPos(-37.5761F, -6.2931F, 4.9962F);
        right_knee.addChild(cube_r24);
        setRotationAngle(cube_r24, -0.7854F, 0.0F, 0.0F);
        cube_r24.texOffs(107, 167).addBox(-0.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r24.texOffs(160, 167).addBox(6.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        right_arm.render(matrixStack, buffer, packedLight, packedOverlay);
        left_arm.render(matrixStack, buffer, packedLight, packedOverlay);
        left_leg.render(matrixStack, buffer, packedLight, packedOverlay);
        right_leg.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}