package com.jetug.chassis_core.common.foundation.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class JetParticle extends TextureSheetParticle {
	private final SpriteSet sprites;

	JetParticle(ClientLevel clientLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet sprites) {
		super(clientLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

		this.friction = 0.96F;
		this.speedUpWhenYMotionIsBlocked = true;
		this.sprites = sprites;
		this.quadSize *= 0.75F;
		this.hasPhysics = true;
		this.setSpriteFromAge(sprites);
	}

	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public int getLightColor(float p_172146_) {
		float f = ((float) this.age + p_172146_) / (float) this.lifetime;
		f = Mth.clamp(f, 0.0F, 1.0F);
		int i = super.getLightColor(p_172146_);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int) (f * 15.0F * 16.0F);
		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
	}

	public void tick() {
		super.tick();
		this.setSpriteFromAge(this.sprites);
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprites) {
			this.sprite = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType p_172162_, ClientLevel p_172163_, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
			var glowparticle = new JetParticle(p_172163_, pX, pY, pZ, 0.0D, 0.0D, 0.0D, this.sprite);
			glowparticle.setColor(1.0F, 0.9F, 1.0F);
			glowparticle.setParticleSpeed(pX * 0.25D, pY * 0.25D, pZ * 0.25D);
			glowparticle.setLifetime(p_172163_.random.nextInt(2) + 2);
			return glowparticle;
		}
	}
}