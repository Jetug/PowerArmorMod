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
import net.minecraft.server.packs.resources.Resource;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static com.jetug.power_armor_mod.common.util.constants.Resources.POWER_ARMOR_MODEL_LOCATION;

public class PlayerHeadLayer extends GeoLayerRenderer<PowerArmorEntity> {

    public PlayerHeadLayer(IGeoRenderer<PowerArmorEntity> entityRenderer) {
        super(entityRenderer);
    }

    private void addImage(BufferedImage buff1, BufferedImage buff2, int x, int y) {
        Graphics2D g2d = buff1.createGraphics();
        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2d.drawImage(buff2, x, y, null);
        g2d.dispose();
    }

    private BufferedImage createImageFromBytes(byte[] imageData) {
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

    //private void

    private final HashMap<String, ResourceLocation> playerTextures = new HashMap<>();

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, PowerArmorEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(!entity.isInvisible() && entity.isVehicle() && entity.getControllingPassenger() instanceof AbstractClientPlayer clientPlayer ) {
            var originalPlayerTexture = clientPlayer.getSkinTextureLocation();
            var minecraft = Minecraft.getInstance();
            var tag = clientPlayer.getUUID().toString();
            ResourceLocation finalTextureLocation = null;

            if(!playerTextures.containsKey(tag)) {
                try {
                    Resource resource = minecraft.getResourceManager().getResource(originalPlayerTexture);
                    NativeImage nativeimage = NativeImage.read(resource.getInputStream());
                    var imageArr = nativeimage.asByteArray();
                    var scaledImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
                    var playerSkin = createImageFromBytes(imageArr);

                    cropImage(playerSkin, 32, 16);
                    addImage(scaledImage, playerSkin, 0, 0);

                    File file = new File("C:/Users/Jetug/Desktop/result/original.png");
                    ImageIO.write(playerSkin, "png", file);

                    File outputFile = new File("C:/Users/Jetug/Desktop/result/image"+clientPlayer.getUUID()+".png");
                    ImageIO.write(scaledImage, "png", outputFile);

                    var nativeImage = getNativeImage(scaledImage);
                    var dynamicTexture = new DynamicTexture(nativeImage);

                    finalTextureLocation = new ResourceLocation(Global.MOD_ID, tag);
                    minecraft.getTextureManager().register(finalTextureLocation, dynamicTexture);

                    playerTextures.put(tag, finalTextureLocation);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                finalTextureLocation = playerTextures.get(tag);
            }

            var cameo = RenderType.armorCutoutNoCull(finalTextureLocation);
            //var cameo = RenderType.armorCutoutNoCull(originalPlayerTexture);

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
}