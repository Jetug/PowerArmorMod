package com.jetug.power_armor_mod.client.render.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import java.util.Objects;

import static com.jetug.power_armor_mod.client.render.utils.ParticleUtils.showJetpackParticles;
import static com.jetug.power_armor_mod.common.data.constants.Bones.LEFT_JET_LOCATOR;
import static com.jetug.power_armor_mod.common.data.constants.Bones.RIGHT_JET_LOCATOR;

public class SteamArmorRenderer extends ArmorChassisRenderer{
    public SteamArmorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer,
                                  int packedLight, int packedOverlay,
                                  float red, float green, float blue, float alpha) {
        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        if ((Objects.equals(bone.name, LEFT_JET_LOCATOR) || Objects.equals(bone.name, RIGHT_JET_LOCATOR))
                && animatable.isDashing()) {
            showJetpackParticles(getWorldPos(bone, poseStack));
        }
    }
}
