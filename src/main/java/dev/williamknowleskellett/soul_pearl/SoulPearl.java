package dev.williamknowleskellett.soul_pearl;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.entity.EntityType.EntityFactory;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoulPearl implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("soul_pearl");
	public static final Item SOUL_PEARL_ITEM = new SoulPearlItem(new Item.Settings().maxCount(16));
	public static final Identifier SOUL_PEARL_ID = new Identifier("soul_pearl", "soul_pearl");
	public static final Item ESSENCE_ITEM = new EssenceItem(new Item.Settings().maxCount(1));
	public static final Identifier ESSENCE_ID = new Identifier("soul_pearl", "essence");

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

		// Register the items
		Registry.register(Registries.ITEM, SOUL_PEARL_ID, SOUL_PEARL_ITEM);
		// ModelPredicateProviderRegistry.register(SOUL_PEARL_ITEM, new Identifier("caged"), (stack, world, entity, seed) -> SoulPearlItem.isSoul((ItemStack)stack) ? 1.0f : 0.0f);
		Registry.register(Registries.ITEM, ESSENCE_ID, ESSENCE_ITEM);
		
        DispenserBlock.registerBehavior(ESSENCE_ITEM, new ProjectileDispenserBehavior(){

            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new EssenceEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
            }
        });
	}
}