package dev.williamknowleskellett.together_pearl;

import dev.williamknowleskellett.soul_pearl.SoulPearl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class TogetherPearlClient implements ClientModInitializer {
	@Override

	public void onInitializeClient() {
		EntityRendererRegistry.register(SoulPearl.ESSENCE_ENTITY_TYPE, (context) ->new FlyingItemEntityRenderer(context));
	}
}