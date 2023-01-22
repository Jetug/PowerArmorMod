package com.jetug.power_armor_mod.client.render.layers;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Level;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

import static com.jetug.power_armor_mod.common.util.constants.Resources.POWER_ARMOR_MODEL_LOCATION;

public class PlayerHeadLayer extends GeoLayerRenderer<PowerArmorEntity> {
    private final HashMap<String, ResourceLocation> playerTextures = new HashMap<>();
    private IGeoRenderer<PowerArmorEntity> entityRenderer;

    public PlayerHeadLayer(IGeoRenderer<PowerArmorEntity> entityRenderer) {
        super(entityRenderer);
        this.entityRenderer = entityRenderer;
    }


    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, PowerArmorEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(!entity.isInvisible() && entity.isVehicle() && entity.getControllingPassenger() instanceof AbstractClientPlayer clientPlayer ) {

            var texture = getHeadResourceLocation(clientPlayer, entity);
            if (texture == null) return;

            var cameo = RenderType.armorCutoutNoCull(texture);
            int overlay = OverlayTexture.NO_OVERLAY;

            matrixStackIn.pushPose();
            matrixStackIn.scale(1.0f, 1.0f, 1.0f);
            matrixStackIn.translate(0.0d, 0.0d, 0.0d);
            this.getRenderer().render(
                    this.getEntityModel().getModel(POWER_ARMOR_MODEL_LOCATION),
                    entity,
                    partialTicks,
                    cameo,
                    matrixStackIn,
                    bufferIn,
                    bufferIn.getBuffer(cameo),
                    packedLightIn,
                    overlay,
                    1f, 1f, 1f, 1f);
            matrixStackIn.popPose();
        }
    }

    @Nullable
    private ResourceLocation getHeadResourceLocation(AbstractClientPlayer clientPlayer, PowerArmorEntity entity) {
        var minecraft = Minecraft.getInstance();
        var tag = clientPlayer.getUUID().toString();

        if (playerTextures.containsKey(tag)) {
            return playerTextures.get(tag);
        } else {
            var playerHead = getPlayerHead(clientPlayer);
            var entityTextureRL = entityRenderer.getTextureLocation(entity);
            var entityTexture = resourceToBufferedImage(entityTextureRL);

            if (playerHead == null || entityTexture == null) return null;

            playerHead = extendImage(playerHead, entityTexture.getWidth(), entityTexture.getHeight());

            var nativeImage = getNativeImage(playerHead);
            var dynamicTexture = new DynamicTexture(nativeImage);
            var finalTextureLocation = new ResourceLocation(Global.MOD_ID, tag);

            minecraft.getTextureManager().register(finalTextureLocation, dynamicTexture);
            playerTextures.put(tag, finalTextureLocation);
            return finalTextureLocation;
        }
    }

    @Nullable
    private static BufferedImage getPlayerHead(AbstractClientPlayer clientPlayer) {
        var playerSkin = getPlayerSkin(clientPlayer);
        if(playerSkin == null) return null;
        cropImage(playerSkin, 32, 16);
        return playerSkin;
    }

    @Nullable
    private static BufferedImage getPlayerSkin(AbstractClientPlayer clientPlayer) {
        var originalPlayerTexture = clientPlayer.getSkinTextureLocation();
        return resourceToBufferedImage(originalPlayerTexture);
    }

    @Nullable
    private static BufferedImage resourceToBufferedImage(ResourceLocation resourceLocation) {
        try {
            var resource = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
            var nativeImage = NativeImage.read(resource.getInputStream());
            var imageArr = nativeImage.asByteArray();
            return createImageFromBytes(imageArr);
        }
        catch (IOException e) {
            Global.LOGGER.log(Level.ERROR, e);
            return null;
        }
    }
    private static BufferedImage extendImage(BufferedImage image, int width, int height){
        var scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        addImage(scaledImage, image, 0, 0);
        return scaledImage;
    }

    private static void addImage(BufferedImage buff1, BufferedImage buff2, int x, int y) {
        Graphics2D g2d = buff1.createGraphics();
        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.drawImage(buff2, x, y, null);
        g2d.dispose();
    }

    private static BufferedImage createImageFromBytes(byte[] imageData) {
        var bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static NativeImage getNativeImage(BufferedImage img) {
        NativeImage nativeImage = new NativeImage(img.getWidth(), img.getHeight(), true);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                var color = img.getRGB(x, y);
                nativeImage.setPixelRGBA(x, y, color);
            }
        }

        return nativeImage;
    }

    public static void cropImage(BufferedImage img, int xPos, int yPos) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if(x >= xPos || y >= yPos)
                    img.setRGB(x, y, (new Color(0.0f, 0.0f, 0.0f, 0.0f)).getRGB());
            }
        }
    }
}