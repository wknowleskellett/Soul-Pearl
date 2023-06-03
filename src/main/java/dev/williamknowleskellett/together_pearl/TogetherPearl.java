package dev.williamknowleskellett.together_pearl;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.entity.EntityType.EntityFactory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TogetherPearl implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("together_pearl");
	public static final Item CAGED_PEARL_ITEM = new CagedPearlItem(new Item.Settings().maxCount(16));
	public static final Identifier CAGED_PEARL_ID = new Identifier("together_pearl", "caged_pearl");
	public static final Item ESSENCE_ITEM = new EssenceItem(new Item.Settings().maxCount(1));
	public static final Identifier ESSENCE_ID = new Identifier("together_pearl", "essence");

	// Register the item entity
	public static final EntityType<EssenceEntity> ESSENCE_ENTITY_TYPE = Registry.register(
		Registries.ENTITY_TYPE, 
		ESSENCE_ID, 
		Builder.create(new EntityFactory<EssenceEntity>() {
			@Override
			public EssenceEntity create(EntityType<EssenceEntity> entityType, World world) {
				return new EssenceEntity(entityType, world);
			}
		}, SpawnGroup.MISC).setDimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(10).build(ESSENCE_ID.toString())
	);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// LOGGER.info("Hello Fabric world!");

		// Register the items
		Registry.register(Registries.ITEM, CAGED_PEARL_ID, CAGED_PEARL_ITEM);
		// ModelPredicateProviderRegistry.register(CAGED_PEARL_ITEM, new Identifier("caged"), (stack, world, entity, seed) -> CagedPearlItem.isCaged((ItemStack)stack) ? 1.0f : 0.0f);
		Registry.register(Registries.ITEM, ESSENCE_ID, ESSENCE_ITEM);
	}
}