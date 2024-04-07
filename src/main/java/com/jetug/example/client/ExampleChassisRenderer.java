package com.jetug.example.client;

import com.jetug.chassis_core.client.render.renderers.ChassisRenderer;
import com.jetug.example.common.entities.ExampleChassis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ExampleChassisRenderer extends ChassisRenderer<ExampleChassis> {
    public ExampleChassisRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ExampleChassisModel());
    }
}
