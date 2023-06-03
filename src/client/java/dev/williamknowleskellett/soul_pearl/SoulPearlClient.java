package dev.williamknowleskellett.soul_pearl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class SoulPearlClient implements ClientModInitializer {
	@Override

	public void onInitializeClient() {
		EntityRendererRegistry.register(SoulPearl.ESSENCE_ENTITY_TYPE, (context) ->new FlyingItemEntityRenderer<EssenceEntity>(context));
	}
}